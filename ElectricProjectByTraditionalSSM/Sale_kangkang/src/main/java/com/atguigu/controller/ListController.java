package com.atguigu.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.bean.DETAIL_T_MALL_SKU;
import com.atguigu.bean.KEYWORDS_T_MALL_SKU;
import com.atguigu.bean.MODEL_T_MALL_SKU_ATTR_VALUE;
import com.atguigu.bean.OBJECT_T_MALL_ATTR;
import com.atguigu.bean.OBJECT_T_MALL_SKU;
import com.atguigu.bean.T_MALL_SKU_ATTR_VALUE;
import com.atguigu.service.AttrServiceInf;
import com.atguigu.service.ListServiceInf;
import com.atguigu.util.JedisPoolUtils;
import com.atguigu.util.MyCacheUtil;
import com.atguigu.util.MyHttpGetUtil;
import com.atguigu.util.MyJsonUtil;
import com.atguigu.util.MyPropertiesUtil;

import redis.clients.jedis.Jedis;

@Controller
public class ListController {

	@Autowired
	ListServiceInf listServiceInf;
	
	@Autowired
	AttrServiceInf attrServiceInf;
	
	@RequestMapping("keywords")
	public String keywords(String keywords, ModelMap map) {
		// 调用keywords的关键词查询接口
		// 获得keywords系统通过solr获得的list_sku对象的字符串
		String doGet = "";
		try {
			doGet = MyHttpGetUtil.doGet(MyPropertiesUtil.getMyProperty("ws.properties", "keywords_url")+"?keywords=" + keywords);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 把得到的list_sku对象的字符串转化为list集合
		List<KEYWORDS_T_MALL_SKU> list_sku = MyJsonUtil.json_to_list(doGet, KEYWORDS_T_MALL_SKU.class);
		map.put("keywords", keywords);
		map.put("list_sku", list_sku);
		return "search";
	}
	
	@RequestMapping("goto_list")
	public String goto_list(int flbh2, ModelMap map) {

		// flbh2属性的集合
		List<OBJECT_T_MALL_ATTR> list_attr = attrServiceInf.get_attr_list(flbh2);

		/*// flbh2商品列表-- 使用mysql根据分类编号2检索商品列表
		List<OBJECT_T_MALL_SKU> list_sku = listServiceInf.get_list_by_flbh2(flbh2);*/
		
		// flbh2商品列表-- 使用redis根据分类编号2检索商品列表
		// 把二级分类id作为redis中的key存储进去redis数据库
		// key按照一定的规则存储
		String key = "class_2_" + flbh2;
		// 从redis中根据工具类获得sku库存集合
		List<OBJECT_T_MALL_SKU> list_sku = MyCacheUtil.getList(key, OBJECT_T_MALL_SKU.class);
		//判断如果从redis中根据key获取不到值，就从mysql数据库中查询
		if(list_sku == null || list_sku.size() < 1) {
			//	当通过flbh2这个key去redis取数据时，发现没有，这时要从mysql数据库中取
			list_sku = listServiceInf.get_list_by_flbh2(flbh2);
			// 将检索结果同步到redis中
			MyCacheUtil.setKey(key, list_sku);
		}

		map.put("list_attr", list_attr);
		map.put("list_sku", list_sku);
		map.put("flbh2", flbh2);
		return "list";
	}
	
	@RequestMapping("get_list_by_attr")
	public String get_list_by_attr(MODEL_T_MALL_SKU_ATTR_VALUE list_attr, int flbh2, ModelMap map) {

		/*// 根据属性查询列表的业务 - 使用mysql根据attr和flbh2检索sku集合
		List<OBJECT_T_MALL_SKU> list_sku = listServiceInf.get_list_by_attr(list_attr.getList_attr(), flbh2);*/
		// 我们需要给存入redis的数据创建key的名称，按照某种规则；
		// 这里的规则是前台选中的attr的id拼接起来
		List<T_MALL_SKU_ATTR_VALUE> list_attr2 = list_attr.getList_attr();
		String[] keys = new String[list_attr2.size()];
		// 遍历redis中选中的key
		for (int i = 0; i < list_attr2.size(); i++) {
			keys[i] = "attr_" + flbh2 + "_" + list_attr2.get(i).getShxm_id() + "_" + list_attr2.get(i).getShxzh_id() ;
		}
		// 调用工具类整合并集
		String interKeys = MyCacheUtil.interKeys(keys);
		List<OBJECT_T_MALL_SKU> list_sku = MyCacheUtil.getList(interKeys, OBJECT_T_MALL_SKU.class);
		//判断如果从redis中根据key获取不到值，就从mysql数据库中查询
		// 注意：如果选择两个属性，那么即使第一个属性不为空，最终list_sku也=null，所以我们就要判断是否key在redis已经存在
		if(list_sku == null || list_sku.size() < 1) {
			list_sku = listServiceInf.get_list_by_attr(list_attr.getList_attr(), flbh2);
			
			/*// 将检索结果同步到redis中
			// 遍历每一个传递过来的属性
			for (int i = 0; i < list_attr2.size(); i++) {
				String key = keys[i];// attr_28_1_2

				// 同步redis中之前先判断key是否存在，如存在就不用再同步了
				// 判断redis中是否存在
				boolean if_key = MyCacheUtil.if_key(key);
				
				// 如果遍历的该key不存在，就同步
				if (!if_key) {
					// 最终要往get_list_by_attr方法中的返回值就是一个sku集合
					List<OBJECT_T_MALL_SKU> list_sku_for_redis = new ArrayList<OBJECT_T_MALL_SKU>();
					// 要想办法往list_sku_for_redis添加List<T_MALL_SKU_ATTR_VALUE>类型的数据
					// 所以创建List<T_MALL_SKU_ATTR_VALUE>集合；因为get_list_by_attr方法的参数有一个就是List<T_MALL_SKU_ATTR_VALUE>
					List<T_MALL_SKU_ATTR_VALUE> list_attr_for_redis = new ArrayList<T_MALL_SKU_ATTR_VALUE>();
					// 取得List<T_MALL_SKU_ATTR_VALUE>集合中每一个T_MALL_SKU_ATTR_VALUE，（只能手动创建，不能调用get(i),因为集合创建出来就是空，会空指针异常）
					T_MALL_SKU_ATTR_VALUE attr_value = new T_MALL_SKU_ATTR_VALUE();
					// 往该集合中添加数据：主要添加shxm_id和shxzh_id;
					attr_value.setShxm_id(list_attr2.get(i).getShxm_id());
					attr_value.setShxzh_id(list_attr2.get(i).getShxzh_id());
					// 把T_MALL_SKU_ATTR_VALUE添加到List<T_MALL_SKU_ATTR_VALUE>集合中
					list_attr_for_redis.add(attr_value);
					// 从数据库中根据我们上面取得的值查询数据，放入redis中
					list_sku_for_redis = listServiceInf.get_list_by_attr(list_attr_for_redis, flbh2);
					
					// 再根据属性和属性值可以查询 出对应的sku集合
					// attr的可以和sku的集合循环 插入到redis
					 * MyCacheUtil.setKey(key, list_sku_for_redis);
					 * 
*/					for (int i = 0; i < list_attr2.size(); i++) {
						String key = keys[i];// attr_28_1_2
						MyCacheUtil.setKey(key, list_sku);
					}
			
				}
				
			
		
		map.put("list_sku", list_sku);
		return "skuList";
	}

}
