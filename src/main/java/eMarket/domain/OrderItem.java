package eMarket.domain;

public class OrderItem {

	public static int lastId = 0;
    private int id = -1;
    private Product product;
    private int amount;
    private double cost;
    private double discount = 0.0;
    
    public OrderItem(){
    	this.id = lastId;
    	lastId++;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
    
}
