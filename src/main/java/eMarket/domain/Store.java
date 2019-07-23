package eMarket.domain;

import java.util.ArrayList;
import java.util.List;

public class Store {
	List<Product> productList = new ArrayList<>();
	List<Deal> dealList = new ArrayList<>();
	List<Order> orderList = new ArrayList<>();
	
	public void init() {
		productList = new ArrayList<>();
		dealList = new ArrayList<>();
		Product.lastId=0;
		Deal.lastId=0;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public List<Deal> getDealList() {
		return dealList;
	}

	public void setDealList(List<Deal> dealList) {
		this.dealList = dealList;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}
	
	
}
