/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Deal;
import eMarket.domain.Order;
import eMarket.domain.OrderItem;
import eMarket.domain.Product;

@Controller
@RequestMapping("/item")
public class ItemController {
    @InitBinder("itemFormDto")
    protected void initBinder(WebDataBinder binder) {
    	binder.addValidators(new ItemValidator());
    }
    
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String itemDetail(
		@ModelAttribute("itemFormDto") ItemFormDto itemFormDto, 
		@RequestParam(value="itemId", required=false, defaultValue="-1") int itemId, 
		@RequestParam(value="orderId", required=true, defaultValue="-1") int orderId
    ) {
    	if (itemId > -1) {
    		Order order = EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId() == orderId).findFirst().orElse(null);
	    	OrderItem item = order.getItemList().stream().filter(p -> p.getId()==itemId).findFirst().orElse(null);
	    	itemFormDto.setId(itemId);
	    	itemFormDto.setProductId(item.getProduct().getId());
	    	itemFormDto.setAmount(item.getAmount());
    	}
    	itemFormDto.setOrderId(orderId);
    	itemFormDto.setProductList(EMarketApp.getStore().getProductList());
    	return "form/itemDetail";
    }   
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add( @Valid @ModelAttribute("itemFormDto") ItemFormDto itemFormDto, BindingResult result, @RequestParam(value="action") String action, Model model) {
		System.out.println("adding new item");
		
		if (result.hasErrors() ) {
    		itemFormDto.setId(itemFormDto.getId());
    		itemFormDto.setAmount(itemFormDto.getAmount());
    		itemFormDto.setProductId(itemFormDto.getProductId());
        	itemFormDto.setProductList(EMarketApp.getStore().getProductList());
        	model.addAttribute("itemFormDto", itemFormDto);
    		return "form/itemDetail";
    	} else {
		
	    	Order order = EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId() == itemFormDto.getOrderId()).findFirst().orElse(null);
	    	if (!action.startsWith("Submit")) {
	    		model.addAttribute("order", order);
		    	return "form/orderDetail";
	    	} else  {
	    		if (action.startsWith("Submit")) {
		    		Optional<OrderItem> itemOp = order.getItemList().stream().filter(p -> (p.getId() == itemFormDto.getId())).findFirst();
		    		OrderItem item = null;
		    		if (itemOp.isPresent()) {
		    			// edit
		    			item = itemOp.get();
		    		} else {
		    			// create
		    			item = new OrderItem();
		    			order.getItemList().add(item);
		    		}
		    		Product product = EMarketApp.getStore().getProductList().stream().filter(p -> p.getId() == itemFormDto.getProductId()).findFirst().get();
	    			item.setProduct(product);
	    			item.setAmount(itemFormDto.getAmount());
	    			
	    			// get active deal for this product and apply discount
	    			Optional<Deal> deal = EMarketApp.getStore().getDealList().stream().filter(d -> 
	    				d.getProduct().equals(product) && 
	    					(d.getStartDate().isBefore(order.getDate()) ||  d.getStartDate().isEqual(order.getDate())) &&
	    					(d.getEndDate() != null) ? (d.getEndDate().isAfter(order.getDate()) || d.getEndDate().isEqual(order.getDate())) : false
	    				).findFirst();
	    			Double cost = item.getProduct().getPrice() * item.getAmount();
	    			if (deal.isPresent()) {
	    				item.setDiscount(deal.get().getDiscount());
	    				cost = cost - cost * deal.get().getDiscount();
	    			}
	    			item.setCost(cost);
	    			
	    			order.updateCost();
	    		} 
	    		model.addAttribute("order", order);
		    	return "form/orderDetail";
	    	}
    	}
    }   

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(
    		@RequestParam(value="itemId", required=false, defaultValue="-1") int itemId, 
    		@RequestParam(value="orderId", required=true, defaultValue="-1") int orderId,
    		Model model
    ) {
    	Order order = EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId()==orderId).findFirst().get();
    	order.getItemList().removeIf(p -> p.getId()==itemId);
    	order.updateCost();
    	model.addAttribute("order", order);
    	return "form/orderDetail";
    }   
    


    
}
