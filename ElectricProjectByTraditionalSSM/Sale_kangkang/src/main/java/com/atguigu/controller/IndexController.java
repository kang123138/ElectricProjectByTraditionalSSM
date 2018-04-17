package com.atguigu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.bean.OBJECT_T_MALL_ATTR;
import com.atguigu.bean.OBJECT_T_MALL_SKU;
import com.atguigu.service.AttrServiceInf;
import com.atguigu.service.ListServiceInf;

@Controller
public class IndexController {

	@Autowired
	AttrServiceInf attrServiceInf;

	@Autowired
	ListServiceInf listServiceInf;
	
	@RequestMapping("orderErr")
	public String orderErr() {
		return "orderErr";
	}
	
	// 登录到检查订单页面
	@RequestMapping("goto_login_checkOrder")
	public String goto_login_checkOrder(HttpServletRequest request, ModelMap map) {
		return "loginOrder";
	}

	@RequestMapping("goto_login")
	public String goto_login(HttpServletRequest request, ModelMap map) {

		return "login";
	}

	@RequestMapping("index")
	public String index(HttpServletRequest request, ModelMap map) {

		/*
		 * String yh_mch = ""; Cookie[] cookies = request.getCookies(); if
		 * (cookies != null && cookies.length > 0) { for (int i = 0; i <
		 * cookies.length; i++) { String name = cookies[i].getName(); if
		 * (name.equals("yh_mch")) { yh_mch = cookies[i].getValue(); } } }
		 * map.put("yh_mch", yh_mch);
		 */
		return "index";
	}

}
