package com.atguigu.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {




	@GetMapping("/main")
	public String main(){
		return "main";
	}
	
	@GetMapping("/error403")
	public String error403(){
		return "error403";
	}

}
