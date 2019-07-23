package eMarket.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class IndexValidator implements Validator {
		
	public boolean supports(Class<?> clazz) {
        return IndexFormDto.class.equals(clazz);
    }

	@Override
	public void validate(Object target, Errors errors) {
		IndexFormDto dto = (IndexFormDto) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "", "Field cannot be empty.");
		
		if (dto.getDate() == null) {
			errors.rejectValue("startDate", "", "Field cannot be empty.");
		}
		
	}
	
	
}
