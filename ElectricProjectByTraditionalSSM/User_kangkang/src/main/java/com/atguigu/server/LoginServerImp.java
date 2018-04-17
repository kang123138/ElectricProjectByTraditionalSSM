package com.atguigu.server;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.bean.T_MALL_USER_ACCOUNT;
import com.atguigu.service.LoginServiceInf;
import com.google.gson.Gson;

public class LoginServerImp implements LoginServerInf {

	@Autowired
	LoginServiceInf loginServiceInf;
	
	// Soap风格：
	public String login(T_MALL_USER_ACCOUNT user) {
		T_MALL_USER_ACCOUNT select_user = loginServiceInf.login(user);
		Gson gson = new Gson();
		return gson.toJson(select_user);
	}


	// rest风格：
	@Path("login2")// 方法名
	@GET//get协议
	@Consumes("application/x-www-form-urlencoded")// 参数类型；一般都是这个
	@Produces("application/json")
	@Override
	public String login2(@BeanParam T_MALL_USER_ACCOUNT user) {
		T_MALL_USER_ACCOUNT select_user = loginServiceInf.login2(user);
		Gson gson = new Gson();
		return gson.toJson(select_user);
	}
	
}
