package eMarket.domain;

public class Product {

	public static int lastId = 0;
    private int id = -1;
    private String name;
    private String description;
    private double price = 0.0;
    
    
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Product(){}
    
    public Product(int id, String name, String description, Double price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public void setId() {
		this.id = lastId;
		lastId++;
	}

}
