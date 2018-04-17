package com.atguigu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.mapper.IndexMapper;

@Service
public class LoginServiceImp implements LoginServiceInf {

	@Autowired
	IndexMapper indexMapper;
	
	@Override
	public void log(String string) {
		indexMapper.log(string);
	}

}
