package eMarket.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import eMarket.EMarketApp;
import eMarket.domain.Product;

public class DealFormDto {
	private int productId = -1;
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate currentDate = EMarketApp.getSystemDate();
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate startDate = EMarketApp.getSystemDate();
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate endDate;
	private double discount = 0.0;
	List<Product> productList;
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public LocalDate getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(LocalDate currentDate) {
		this.currentDate = currentDate;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	
	
}
