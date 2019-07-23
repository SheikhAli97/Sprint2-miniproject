package eMarket;

import static org.hamcrest.Matchers.*
import static org.hamcrest.Matchers.either
import org.hamcrest.core.*
import static org.hamcrest.core.StringContains.containsString
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs
import static org.hamcrest.core.IsEqual.equalTo
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.number.IsCloseTo.closeTo
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

import java.time.format.DateTimeFormatter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import app.controller.*
import eMarket.controller.IndexController
import eMarket.controller.ItemController
import eMarket.controller.OrderController
import eMarket.controller.ProductController
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration
//@SpringBootTest: loads complete application and injects all the beans which is can be slow.
//@SpringBootTest(classes=[EMarketApp.class,WebConfig.class,ItemValidator.class,ProductController.class,IndexController.class,OrderController.class,ItemController.class])
// @WebMvcTest tests the controller layer only
@WebMvcTest(controllers=[ProductController.class,IndexController.class,OrderController.class,ItemController.class])
public class Tests extends Specification {
	@Autowired
    private WebApplicationContext wac;
	
	@Autowired 
	ItemController itemController;
	
	private MockMvc mockMvc;
	private ResultActions result;
	
	def setup() {
		EMarketApp.initialise();
	}
	
	@Unroll
	def "#testId: navigation with GET HTTP requests"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "clicking on '#button' of view '#view'"
			result = this.mockMvc.perform(get("/$request"))
		then: "I should see the view #viewName"
			result//.andExpect(status().is(200))
				.andExpect(view().name(viewName))
		where:
			testId | button | view | request | viewName
			1 | 'Order' | 'index' | 'order/' | 'form/orderMaster'
			2 | 'Main Page' | 'orderMaster' | '' | 'index'
			3 | 'Add new order' | 'orderMaster' | 'order/add/' | 'form/orderDetail'
			
	}
	
	def "4: navigation with POST HTTP requests"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "when clicking on 'Add new order' in the view 'form/orderMaster'"
			result = this.mockMvc.perform(get("/order/add?orderId=0"))
		then: "I should see the view 'form/orderDetail'"
			result
				.andExpect(status().is(either(is(200)).or(is(302))))
				// the assertion on the redirect should be more specific (this was used to allow user-defined requests)
				.andExpect(view().name(either(is('form/orderDetail')).or(containsString('redirect:'))))
	}
	
	
	def "5: navigation with POST HTTP requests"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "when clicking on 'Delete' in the view 'orderMaster'"
			result = this.mockMvc.perform(get("/order/delete?orderId=0"))
		then: "I should see the view 'form/orderMaster'"
			result
				.andExpect(status().is(either(is(200)).or(is(302))))
				// the assertion on the redirect should be more specific (this was used to allow user-defined requests)
				.andExpect(view().name(either(is('form/orderMaster')).or(containsString('redirect:'))))
	}
	
	def "6: business logic (order creation)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def numberOfOrders = EMarketApp.store.orderList.size
		when: "when clicking on 'Add new order' in the view 'orderMaster'"
			result = this.mockMvc.perform(get("/order/add/"))
		then: "a new order must be created and added in the store `EMarketApp.store.orderList`. The date of the new order must be the system date, the one that appears in the view `index`."
			numberOfOrders + 1 == EMarketApp.store.orderList.size 
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			String formattedString = EMarketApp.store.orderList.last().date.format(formatter);
			formattedString == new Date().format( 'yyyyMMdd' ) 
	}
	
	def "7: business logic (order creation) "() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "the list of orders is displayed in the view `orderMaster`"
			result = this.mockMvc.perform(get("/order/"))
		then: "the information of each order in `EMarketApp.store.orderList` must be displayed"
			result
				.andExpect(status().is(200))
				.andExpect(model().attribute('orderList',EMarketApp.store.orderList))
	}
	
	
	def "8: business logic (order creation) "() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def order = EMarketApp.store.orderList.find{o -> o.id==0}
		when: "clicking on `Edit` for the order with orderId=0 in the view `orderMaster``"
			result = this.mockMvc.perform(get("/order/add?orderId=0"))
		then: "the view `orderDetail` must display the details of the chosen order (id=0)"
			result
				.andExpect(status().is(200))
				.andExpect(model().attribute('order',hasProperty('id',is(0))))
	}				
	
	
	
	def "9: business logic (order creation) "() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def numberOfOrders = EMarketApp.store.orderList.size
		when: "clicking on `Delete` for the order with orderId=0 in the view `orderMaster``"
			result = this.mockMvc.perform(get("/order/delete?orderId=0"))
		then: "the selected order must be deleted from 'EMarketApp.store.orderList'"
			numberOfOrders == EMarketApp.store.orderList.size + 1 
	}
	
	
	def "10: navigation with GET HTTP requests (2nd part)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "clicking on 'Show all orders' of view 'orderDetail'"
			result = this.mockMvc.perform(get("/order/"))
		then: "I should see the view 'form/orderMaster'"
			result
				.andExpect(status().is(200))
				.andExpect(view().name('form/orderMaster'))
	}
	
	
	def "11: navigation with GET HTTP requests (2nd part) "() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "clicking on 'Add new item' of view 'form/orderDetail'"
			result = this.mockMvc.perform(get("/item/detail?orderId=0"))
		then: "I should see the view 'form/itemDetail'"
			result
				.andExpect(status().is(200))
				.andExpect(view().name('form/itemDetail'))
	}
	
	def "12: navigation with POST HTTP requests (2nd part)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "when clicking on 'Submit' in the view 'itemDetail' with amount #amount"
			result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '1')
				.param('amount', '10')
				.param('action', 'Submit')
			)
		then: "I should see the view 'form/orderDetail'"
			result
				.andExpect(status().is(either(is(200)).or(is(302))))
				// the assertion on the redirect should be more specific (this was used to allow user-defined requests)
				.andExpect(view().name(either(is('form/orderDetail')).or(containsString('redirect:'))))
	}

	
	def "13: navigation with POST HTTP requests (2nd part)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "when clicking on 'Submit' in the view 'itemDetail' with amount ''"
			result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '1')
				.param('amount', '')
				.param('action', 'Submit')
			)
		then: "I should see the view 'form/itemDetail'"
			result
				.andExpect(status().is(is(200)))
				.andExpect(view().name('form/itemDetail'))
	}

	
	
//	def "14: navigation with POST HTTP requests (2nd part)"() {
//		given: "the controller is setup"
//			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
//		when: "when clicking on 'Cancel' in the view 'itemDetail' with amount #amount"
//			result = this.mockMvc.perform(post("/item/add/")
//				.param('orderId', '0')
//				.param('id', '-1')
//				.param('productId', '1')
//				.param('amount', '10')
//				.param('action', 'Cancel')
//			)
//		then: "I should see the view 'form/orderDetail'"
//			result
//				.andExpect(status().is(200))
//				.andExpect(view().name('form/orderDetail'))
//	}
	
	
	def "15: business logic (order item creation)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def numberOfOrders = EMarketApp.store.orderList.size
		when: "adding a new item by clicking on 'Add new item' for order in view 'orderDetail'"
			result = this.mockMvc.perform(get("/item/detail?orderId=0"))
		then: "a new object 'OrderItem' must be created and linked to the form in the view 'itemDetail'"
			result
				.andExpect(status().is(200))
				.andExpect(model().attribute('itemFormDto', hasProperty('amount',is(0))))
	}
	
	def "16: business logic (order item update)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "editing an existing item by clicking on 'Edit' in view 'orderDetail'"
			result = this.mockMvc.perform(get("/item/detail?orderId=0&itemId=0"))
		then: "the corresponding 'OrderItem' must be fetched from its corresponding 'order', which is stored in 'EMarketApp.store.orderList'"
			result
				.andExpect(status().is(200))
				.andExpect(model().attribute('itemFormDto', is(
					either(hasProperty('id',is(0)))
					.or(hasProperty('productId',is(1)))
				)))
	}
	
	
	def "17: business logic (order item delete)"() {
		given: "the controller is setup and the order with id=0 exists with an order item with id=0"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
			def numberOfOrders = EMarketApp.store.orderList.size
		when: "deleting an existing item by clicking on 'Delete' in view 'orderDetail'"
			result = this.mockMvc.perform(get("/item/delete?itemId=0&orderId=0"))
		then: "the corresponding 'OrderItem' must be deleted from its corresponding order, which is stored in 'EMarketApp.store.orderList'"
			order != null
			order.itemList.find{ it.id = 0 } == null
	}
	
	def "18: business logic (order item creation)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
			def kiwiProduct = EMarketApp.store.productList.find({ o -> o.id == 4 }) // kiwi
		when: "adding a new item by clicking on 'Submit' in view 'itemDetail'"
			result = result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '4') // Kiwi
				.param('amount', '10')
				.param('action', 'Submit'))
			// in case there are redirects
			def returnValue = result.andReturn()
			if (returnValue.response.getRedirectedUrl() != null) {
				result = this.mockMvc.perform(get("/order/add?orderId=0"))
			}
		then: "a new 'OrderItem' will be created and appended to the 'itemList' of the selected order: the 'product' must reference the corresponding product in 'EMarketApp.store.productList'; the amount must be set with the 'amount' in the form in view 'itemDetail', the 'cost' must correspond to 'amount * product.price'"
//			order != null
//			def item = order.itemList.find{ it.product.id == 4 } 
//			item != null
//			item.product == kiwiProduct
//			item.amount == 10
//			item.cost == item.product.price * item.amount

			def kiwi = EMarketApp.store.productList.find({ p -> p.id == 4 })
			result
				.andExpect(model().attribute('order',
					hasProperty('itemList', 
						hasItems(
							allOf(
								hasProperty('cost', closeTo(3.5, 0.0001)),
								hasProperty('product', is(kiwi))
							)
						)
					)
				)) 
	}
	
	def "19: business logic (validation)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
		when: "adding a new item with an empty 'amount' by clicking on 'Submit' in view 'itemDetail'"
			result = result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '4') // Kiwi
				.param('amount', '')
				.param('action', 'Submit'))
		then: "an error must be displayed next to the field 'amount'"
			result
				.andExpect(status().is(either(is(200)).or(is(302))))
				.andExpect(model().attributeHasErrors("itemFormDto"))
	}

	
	def "20: business logic (validation)"() {
		given: "the controller is setup"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
		when: "adding a new item with an empty 'amount' by clicking on 'Submit' in view 'itemDetail'"
			result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '4') // Kiwi
				.param('amount', '-10')
				.param('action', 'Submit'))
		then: "an error must be displayed next to the field 'amount'"
			result
				.andExpect(status().is(either(is(200)).or(is(302))))
				.andExpect(model().attributeHasErrors("itemFormDto"))
	}
	
	def "21-23a: business logic (discounts)"() {
		given: "there is no active deal for the product 'Kiwi' in 'EMarketApp.store.dealList'"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
		when: "adding a new item with 10 kiwis deal by clicking on 'Submit' in view 'itemDetail'"
			def result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '4') // Kiwi
				.param('amount', '10')
				.param('action', 'Submit'))
			// in case there are redirects
			def returnValue = result.andReturn()
			if (returnValue.response.getRedirectedUrl() != null) {
				result = this.mockMvc.perform(get("/order/add?orderId=0"))
			}
		then: "the resulting cost of the order item should be close to 3.5"
//			def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
//			result
//				.andExpect(model().attribute('order',samePropertyValuesAs(order)))
//			def item = order.itemList.find{ it.product.id == 0 }
//			assertThat(item.cost, closeTo((item.product.price - (deal.discount * item.product.price)) * item.amount, 0.0001))
			def kiwi = EMarketApp.store.productList.find({ p -> p.id == 4 })
			result
				.andExpect(model().attribute('order',
					hasProperty('itemList', 
						hasItems(
							allOf(
								hasProperty('cost', closeTo(3.5, 0.0001)),
								hasProperty('product', is(kiwi))
							)
						)
					)
				)) 
	}
	
	def "21-23b: business logic (discounts)"() {
		given: "there is an active deal for the product 'Banana' in 'EMarketApp.store.dealList'"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			def deal = EMarketApp.store.dealList.find({ d -> d.product.id == 0 })
		when: "adding a new item with 10 bananas by clicking on 'Submit' in view 'itemDetail'"
			result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '0') // Banana
				.param('amount', '10')
				.param('action', 'Submit'))
			// in case there are redirects
			def returnValue = result.andReturn()
			if (returnValue.response.getRedirectedUrl() != null) {
				result = this.mockMvc.perform(get("/order/add?orderId=0"))
			}
		then: "the resulting cost of the order item should be close to 1.44"
//			deal != null
//			def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
//			order != null
//			def item = order.itemList.find{ it.product.id == 0 }
//			item != null
//			item.amount == 10
//			// Hamcrest matcher
//			assertThat(item.cost, closeTo((item.product.price - (deal.discount * item.product.price)) * item.amount, 0.0001))
			def banana = EMarketApp.store.productList.find({ p -> p.id == 0 })
			result
				.andExpect(model().attribute('order',
					hasProperty('itemList', 
						hasItems(
							allOf(
								hasProperty('cost', closeTo(1.44, 0.0001)),
								hasProperty('product', is(banana))
							)
						)
					)
				)) 
	}
	


	
	def "21-23c: business logic (discounts)"() {
		given: "there is no active deal for the product 'Kiwi' in 'EMarketApp.store.dealList' and an order item with 10 kiwis is added"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '4') // Kiwi
				.param('amount', '10')
				.param('action', 'Submit'))
		when: "when the order is displayed in the view 'orderMaster'"
			def getFailing = false
			try {
				result = this.mockMvc.perform(get("/order/add?orderId=0"))
			} catch (e) {
				getFailing = true
			}
		then: "the order should contain the cost after applying discounts (close to 5.1)"
			if (getFailing) {
				// use earlier version in order not to deduct marks
				def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
				order != null
				assertThat(order.cost, closeTo(5.1, 0.0001))
			} else {
				result
				.andExpect(model().attribute('order',hasProperty('cost', closeTo(5.1, 0.0001))))
			}
	}
	
	
	def "21-23d: business logic (discounts)"() {
		given: "there is an active deal for the product 'Banana' in 'EMarketApp.store.dealList' and an order item with 10 bananas is added"
			this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
			result = result = this.mockMvc.perform(post("/item/add/")
				.param('orderId', '0')
				.param('id', '-1')
				.param('productId', '0') // Banana
				.param('amount', '10')
				.param('action', 'Submit'))
		when: "when the order is displayed in the view 'orderMaster'"
			def getFailing = false
			try {
				result = this.mockMvc.perform(get("/order/add?orderId=0"))
			} catch (e) {
				getFailing = true
			}
		then: "the order should contain the cost after applying discounts (close to 3.04)"
			if (getFailing) {
				// use earlier version in order not to deduct marks
				def order = EMarketApp.store.orderList.find({ o -> o.id == 0 })
				order != null
				assertThat(order.cost, closeTo(3.04, 0.0001))
			} else {
				result
					.andExpect(model().attribute('order',hasProperty('cost', closeTo(3.04, 0.0001))))
			}
	}
	

}
