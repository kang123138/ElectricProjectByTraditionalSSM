package com.atguigu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.service.LoginServiceInf;

@Controller
public class IndexController {
	
	@Autowired
	LoginServiceInf loginServiceInf;
	
	@RequestMapping("index")
	public String login(String str) {
		loginServiceInf.log(str);
		return "";
	}

}
