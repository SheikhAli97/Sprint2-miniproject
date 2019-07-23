package eMarket.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import eMarket.EMarketApp;

public class IndexFormDto {
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate date = EMarketApp.getSystemDate();

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
}
