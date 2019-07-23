package eMarket.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eMarket.EMarketApp;

public class Order {

	public static int lastId = 0;
    private int id = -1;
	private String user = "";
    private LocalDate date;
    private List<OrderItem> itemList = new ArrayList<>();
    private double cost = 0.0;

	public Order() { 
		date = EMarketApp.getSystemDate();
	}
	
	public String getDescription() {
		// generate a comma-separated list with the names of the products purchased
		List<String> list = itemList.stream().map(i -> i.getProduct().getName()).collect(Collectors.toList());
		return String.join(", ", list);
	}

	// updates the id
	public void setId() {
		id=lastId;
		lastId++;
	}
	
	public void updateCost() {
		cost = 0.0;
		this.getItemList().forEach(i -> cost += i.getCost());
	}


	private Product getProduct(int productId) {
		return EMarketApp.getStore().getProductList().stream().filter(p -> p.getId()==productId).findFirst().orElse(null);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<OrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderItem> itemList) {
		this.itemList = itemList;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	
}
