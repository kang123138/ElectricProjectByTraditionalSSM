package com.atguigu.mapper;

import java.util.List;
import java.util.Map;

import com.atguigu.bean.DETAIL_T_MALL_SKU;
import com.atguigu.bean.T_MALL_SKU;

public interface ItemMapper {

	DETAIL_T_MALL_SKU select_sku_detail(Map<Object, Object> map);

	List<T_MALL_SKU> select_sku_list_by_spu(int spu_id);

}
