package eMarket.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import eMarket.EMarketApp;
import eMarket.domain.Deal;

public class DealValidator implements Validator {
		
	public boolean supports(Class<?> clazz) {
        return DealFormDto.class.equals(clazz);
    }

	@Override
	public void validate(Object target, Errors errors) {
		DealFormDto dto = (DealFormDto) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "", "Field cannot be empty.");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "discount", "", "Field cannot be empty.");
		
		if (dto.getProductId() < 0) {
			errors.rejectValue("productId", "", "No product selected.");
		} 
		if (dto.getDiscount() == 0.0) {
			errors.rejectValue("discount", "", "Discount has to be different from 0.0.");
		}
		if (dto.getStartDate() == null) {
			errors.rejectValue("startDate", "", "Field cannot be empty.");
		}
		if ((dto.getStartDate() != null)) {
			
			if ((dto.getEndDate() == null)) {

				// OPEN - OPEN
				Stream<Deal> deals = 
					EMarketApp.getStore().getDealList().stream().filter(d -> (((Deal) d).getProduct().getId() == dto.getProductId()));
				List<Object> elements = Arrays.asList(deals.filter(d -> d.getEndDate() == null).toArray());
				if (elements.size() > 0) {
					errors.rejectValue("startDate", "", "Dates overlap with another deal.");
				}

				// OPEN - CLOSED
				deals = 
					EMarketApp.getStore().getDealList().stream().filter(d -> (((Deal) d).getProduct().getId() == dto.getProductId()));
				elements = Arrays.asList(deals.filter(d -> d.getEndDate() != null).filter(d ->
					dto.getStartDate().compareTo(d.getEndDate()) <= 0
				).toArray());
				if (elements.size() > 0) {
					errors.rejectValue("startDate", "", "Dates overlap with another deal.");
				}
	
			} else {
				
				if (dto.getEndDate().isBefore(dto.getStartDate())) {
					errors.rejectValue("endDate", "", "End date is anterior to start date.");
				}
				
				// CLOSED - OPEN
				// deals with an end date
				Stream<Deal> deals = 
					EMarketApp.getStore().getDealList().stream().filter(d -> (((Deal) d).getProduct().getId() == dto.getProductId()));
				List<Object> elements = Arrays.asList(deals.filter(d -> d.getEndDate() == null).filter(d ->
					dto.getEndDate().compareTo(d.getStartDate()) >= 0
				).toArray());
				if (elements.size() > 0) {
					errors.rejectValue("endDate", "", "Dates overlap with another deal." + ((Deal) elements.get(0)).getStartDateAsString() + " - endDate: " + ((Deal) elements.get(0)).getEndDateAsString());
				}

				// CLOSED - CLOSED
				deals = 
					EMarketApp.getStore().getDealList().stream().filter(d -> (((Deal) d).getProduct().getId() == dto.getProductId()));
				elements = Arrays.asList(deals.filter(d -> d.getEndDate() != null).filter(d ->
					(dto.getStartDate().compareTo(d.getStartDate()) >= 0
					&&
					dto.getStartDate().compareTo(d.getEndDate()) <= 0)
					||
					(dto.getStartDate().compareTo(d.getStartDate()) <= 0
					&&
					dto.getEndDate().compareTo(d.getEndDate()) >= 0)
				).toArray());
				if (elements.size() > 0) {
					errors.rejectValue("startDate", "", "Dates overlap with another deal - startDate: " + ((Deal) elements.get(0)).getStartDateAsString() + " - endDate: " + ((Deal) elements.get(0)).getEndDateAsString());
					errors.rejectValue("endDate", "", "Dates overlap with another deal." + ((Deal) elements.get(0)).getStartDateAsString() + " - endDate: " + ((Deal) elements.get(0)).getEndDateAsString());
				}

				
				deals = 
					EMarketApp.getStore().getDealList().stream().filter(d -> (((Deal) d).getProduct().getId() == dto.getProductId()));
				elements = Arrays.asList(deals.filter(d -> d.getEndDate() != null).filter(d ->
					dto.getEndDate().compareTo(d.getEndDate()) <= 0
					&&
					dto.getEndDate().compareTo(d.getStartDate()) >= 0
				).toArray());
				if (elements.size() > 0) {
					errors.rejectValue("startDate", "", "Dates overlap with another deal." + ((Deal) elements.get(0)).getStartDateAsString() + " - endDate: " + ((Deal) elements.get(0)).getEndDateAsString());
					errors.rejectValue("endDate", "", "Dates overlap with another deal." + ((Deal) elements.get(0)).getStartDateAsString() + " - endDate: " + ((Deal) elements.get(0)).getEndDateAsString());
				}
			}
		}
		
	}
	
	
}
