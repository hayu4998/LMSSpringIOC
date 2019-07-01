package com.gcit.lms;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gcit.lms.service.AdminService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String lmsHome(Locale locale, Model model) {
		model.addAttribute("sizeofauthors", adminService.readAuthorsByName(null).size());
		return "lmsadmin";
	}
	
}
