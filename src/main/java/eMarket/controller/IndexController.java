/**
 * (C) Artur Boronat, 2017
 */
package eMarket.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eMarket.EMarketApp;

@Controller
@RequestMapping("/")
public class IndexController {

	@InitBinder
    protected void initBinder(WebDataBinder binder) {
    		binder.addValidators(new IndexValidator());
    }

    @RequestMapping("/")
    public String index(@ModelAttribute("indexFormDto") IndexFormDto indexFormDto) {
    		indexFormDto.setDate(EMarketApp.getSystemDate());
        return "index";
    }
    
    @RequestMapping(value = "/setDate", method = RequestMethod.POST)
    public String setDate(@Valid @ModelAttribute("indexFormDto") IndexFormDto indexFormDto, BindingResult result, Model model) {
    		if (!result.hasErrors() ) {
    			System.out.println("changing date to " + indexFormDto.getDate().toString());
    			EMarketApp.setSystemDate(indexFormDto.getDate());
    		}
    		return "index";
    }
   
}
