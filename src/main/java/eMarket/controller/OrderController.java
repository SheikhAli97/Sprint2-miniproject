/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Order;

@Controller
@RequestMapping("/order")
public class OrderController {
	
    @RequestMapping("/")
    public String orderMaster(Model model) {
    	// TODO: add the corresponding attribute in the model

    	// TODO: return the appropriate view
        return "";
    }
   
    // TODO: define the corresponding request mapping
    public String orderDetail(/* TODO: declare this parameter as a model attribute */Order order, /* TODO define a request parameter*/ int orderId) {
    	// TODO: if the order exists (orderId > -1) then 
    	//			fetch an existingOrder with id orderId from the list of orders in the object EMarketApp.getStore() 
    	//			and 
    	//			initialize the fields (also known as class attributes or class variables) of the Order object with those of existingOrder 
    	//			and 
    	//			update the cost of the order
    	//		else
    	//			initialise the identifier of the model attribute order
    	//			add the new object order to the list of orders of the object EMarketApp.getStore()
    	
    	// TODO: return the appropriate view
    	return "";
    }   

    // TODO: define the corresponding request mapping
    public String delete(/* TODO define a request parameter*/ int orderId, Model model) {
    	// TODO remove the order with id==orderId from the list of orders in the object EMarketApp.getStore()
    	// TODO insert appropriate attribute in model 
    	// TODO: return the appropriate view
    	return "";
    }   
   

    
}
