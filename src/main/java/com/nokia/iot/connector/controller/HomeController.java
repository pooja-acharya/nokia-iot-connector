package com.nokia.iot.connector.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	 
	    @RequestMapping("/")
	    String index() {
	        return "index";
	    }
}
