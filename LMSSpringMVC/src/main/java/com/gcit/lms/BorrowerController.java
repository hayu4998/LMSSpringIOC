package com.gcit.lms;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BorrowerController {
	
	@RequestMapping(value = "/borrower", method = RequestMethod.GET)
	public String lmsHome(Locale locale, Model model) {
		return "lmsborrower";
	}
	
	
}
