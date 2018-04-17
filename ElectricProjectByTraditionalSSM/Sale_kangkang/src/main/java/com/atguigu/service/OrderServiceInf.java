package com.atguigu.service;

import com.atguigu.bean.OBJECT_T_MALL_ORDER;
import com.atguigu.bean.T_MALL_ADDRESS;

import exception.OverSaleException;

public interface OrderServiceInf {

	void save_order(T_MALL_ADDRESS get_address, OBJECT_T_MALL_ORDER order);

	void pay_success(OBJECT_T_MALL_ORDER order) throws OverSaleException;

}
