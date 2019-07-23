package eMarket.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class ItemValidator implements Validator {
		
	public boolean supports(Class<?> clazz) {
        return ItemFormDto.class.equals(clazz);
    }

	@Override
	public void validate(Object target, Errors errors) {
		ItemFormDto dto = (ItemFormDto) target;
		
		// TODO: test 19
		
		// TODO: test 20 
		
	}
	
	
}
