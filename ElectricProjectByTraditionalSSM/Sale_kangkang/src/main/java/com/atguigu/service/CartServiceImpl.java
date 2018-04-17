package com.atguigu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;
import com.atguigu.mapper.CartMapper;

@Service
public class CartServiceImpl implements CartServiceInf {

	@Autowired
	private CartMapper cartMapper;
	
	// 需要设置主键返回策略，以便于更新时使用
	@Override
	public void add_cart(T_MALL_SHOPPINGCAR cart) {
		cartMapper.insert_cart(cart);
	}

	@Override
	public void update_cart(T_MALL_SHOPPINGCAR cart) {
		cartMapper.update_cart(cart);
	}

	@Override
	public boolean if_cart_exists(T_MALL_SHOPPINGCAR cart) {
		boolean flag = false;
		int i = cartMapper.select_cart_exists(cart);
		if(i > 0) {
			flag = true;
		}
		return flag;
	}

	@Override
	public List<T_MALL_SHOPPINGCAR> get_list_cart_by_user(T_MALL_USER_ACCOUNT user) {
		return cartMapper.select_list_cart_by_user(user);
	}

	@Override
	public int delete_cart(int car_id) {
		return cartMapper.delete_cart(car_id);
	}

}
