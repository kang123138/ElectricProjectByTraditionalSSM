package com.atguigu.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.bean.OBJECT_T_MALL_SKU;
import com.atguigu.bean.T_MALL_SKU_ATTR_VALUE;
import com.atguigu.mapper.ListMapper;

@Service
public class ListServiceImp implements ListServiceInf {

	@Autowired
	ListMapper listMapper;

	@Override
	public List<OBJECT_T_MALL_SKU> get_list_by_flbh2(int flbh2) {
		List<OBJECT_T_MALL_SKU> list_sku = listMapper.select_list_by_flbh2(flbh2);
		return list_sku;
	}

	@Override
	public List<OBJECT_T_MALL_SKU> get_list_by_attr(List<T_MALL_SKU_ATTR_VALUE> list_attr, int flbh2) {

		// 使用StringBuffer的缓冲区
		StringBuffer subSql = new StringBuffer("");

		// 根据属性集合动态拼接条件过滤的sql语句
		subSql.append(" and sku.id in ( select sku0.sku_id from ");

		
		/*
		 * SELECT tm.id tmId,tm.*,sku.id skuId, sku.*,spu.id spuId,spu.* FROM 
		t_mall_sku sku,t_mall_product spu,t_mall_trade_mark tm
		WHERE 
		sku.shp_id=spu.id 
		AND
		spu.pp_id=tm.id
		AND
		sku.id IN
		(
		SELECT sku_id FROM
		(SELECT sku_id FROM t_mall_sku_attr_value WHERE shxm_id = ? AND shxzh_id = ?) sku0,
		(SELECT sku_id FROM t_mall_sku_attr_value WHERE shxm_id = ? AND shxzh_id = ?) sku1,
		(SELECT sku_id FROM t_mall_sku_attr_value WHERE shxm_id = ? AND shxzh_id = ?) sku2,
		...
		WHERE sku0.sku_id = sku1.sku_id
		)
		
		AND 
		spu.flbh2=28;
		*/
		
		
		
		
		// 循环的前提条件是   参数不为空
		if (list_attr != null && list_attr.size() > 0) {
			
			for (int i = 0; i < list_attr.size(); i++) {
				subSql.append(
						" (select sku_id from t_mall_sku_attr_value where shxm_id = " + list_attr.get(i).getShxm_id()
								+ " and shxzh_id = " + list_attr.get(i).getShxzh_id() + ") sku" + i + " ");
				// ①第一次判断，判断是否加,
				// 如果不是参数集合的最后一个，那么就在缓冲区中添加,
				if ((i + 1) < list_attr.size() && list_attr.size() > 1) {
					subSql.append(" , ");
				}
			}
			// ②第二次判断，判断是否要加where
			// 如果参数集合的长度大于1，也就是集合中至少要有两个sql语句才会添加where
			if (list_attr.size() > 1) {
				subSql.append(" where ");
				
				
				for (int i = 0; i < list_attr.size(); i++) {
					// 在第二层循环的前提下，判断如果不是参数集合的最后一个，那么就在缓冲区中添加一个等式
					if ((i + 1) < list_attr.size()) {
						subSql.append(" sku" + i + ".sku_id=" + "sku" + (i + 1) + ".sku_id");
						// ③第三次判断，判断是否加and
						// 首先参数集合的长度要大于2，至少是3个，其次i所能取到的值只能是小于集合的长度-2（比如集合中有3个，3-2=1，也就是只能加一个and）
						if(list_attr.size()>2&&i  < (list_attr.size()- 2)){
							subSql.append(" and ");
						}
					}
				}
			}

		}

		subSql.append(" ) ");

		HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
		hashMap.put("flbh2", flbh2);
		hashMap.put("subSql", subSql.toString());

		List<OBJECT_T_MALL_SKU> list_sku = listMapper.select_list_by_attr(hashMap);
		return list_sku;
	}

}
