package com.atguigu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.bean.T_MALL_USER_ACCOUNT;
import com.atguigu.mapper.LoginMapper;
import com.atguigu.util.MyRoutingDataSource;

@Service
public class LoginServiceImp implements LoginServiceInf {

	@Autowired
	private LoginMapper loginMapper;
	
	@Override
	public T_MALL_USER_ACCOUNT login(T_MALL_USER_ACCOUNT user) {
		MyRoutingDataSource.setKey("1");
		return loginMapper.select_user(user);
	}

	@Override
	public T_MALL_USER_ACCOUNT login2(T_MALL_USER_ACCOUNT user) {
		MyRoutingDataSource.setKey("2");
		return loginMapper.select_user(user);
	}

}
