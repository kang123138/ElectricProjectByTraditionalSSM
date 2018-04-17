package com.atguigu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.bean.DETAIL_T_MALL_SKU;
import com.atguigu.bean.T_MALL_SKU;
import com.atguigu.mapper.ItemMapper;

@Service
public class ItemServiceImpl implements ItemServiceInf {

	@Autowired
	private ItemMapper itemMapper;
	
	@Override
	public DETAIL_T_MALL_SKU get_sku_detail(int sku_id) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("sku_id", sku_id);
		return itemMapper.select_sku_detail(map);
	}
	@Override
	public List<T_MALL_SKU> get_sku_list_by_spu(int spu_id) {
		return itemMapper.select_sku_list_by_spu(spu_id);
	}

}
