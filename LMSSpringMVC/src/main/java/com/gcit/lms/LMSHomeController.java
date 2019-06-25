package com.gcit.lms;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LMSHomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String lmsHome(Locale locale, Model model) {
		return "lmshome";
	}
	
}
