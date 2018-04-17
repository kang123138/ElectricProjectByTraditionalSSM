package com.atguigu.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.atguigu.bean.OBJECT_T_MALL_SKU;

import redis.clients.jedis.Jedis;

public class MyCacheUtil {

	public static String interKeys(String... keys) {
		Jedis jedis = null;
		try {
			jedis = JedisPoolUtils.getJedis();
		} catch (Exception e) {
			// 记日志
		}
		// 生成动态的key
		String k0 = "combine";
		for (int i = 0; i < keys.length; i++) {
			k0 = k0 + "_" + keys[i];
		}
		if (!jedis.exists(k0)) {
			// 如果redis中不存在k0,就把传递进来的keys一个个联合成k0
			jedis.zinterstore(k0, keys);
		}
		return k0;
	}
	
	public static <T> List<T> getList(String key, Class<T> t) {
		List<T> list = new ArrayList<T>();

		// 第三方数据调用-使用jedis获取redis连接
		Jedis jedis = null;
		try {
			jedis = JedisPoolUtils.getJedis();
		} catch (Exception e) {
			// 记日志
		}

		// 使用jedis的方法通过key查询value
		Set<String> zrange = jedis.zrange(key, 0, -1);
		
		// 得到的是存放String的字符串set集合，使用迭代器遍历
		Iterator<String> iterator = zrange.iterator();
		while(iterator.hasNext()) {
			String skuStr = iterator.next();
			// 把得到的sku的字符串转换成json对象
			T sku = MyJsonUtil.json_to_object(skuStr, t);
			// 把遍历到的sku对象放入list_sku商品列表中
			list.add(sku);
		}
		return list;
	}

	public static <T> void setKey(String key, List<T> list) {
		// 第三方数据调用-使用jedis获取redis连接
		Jedis jedis = null;
		try {
			jedis = JedisPoolUtils.getJedis();
		} catch (Exception e) {
			// 记日志
		}
		
		// 每次添加时，先把redis中key为传递过来的key干掉，原因：比如redis中已经存在了key=hhh的
		// 值但是又传递过来相同的key，但是值不同的数据，这时往redis数据库中添加，redis会重新再往后
		// 添加，而不会覆盖掉原来相同的那个key的值
		jedis.del(key);
		// 遍历集合，根据key和得到的选中的attr集合往redis中添加
		for (int i = 0; i < list.size(); i++) {
			jedis.zadd(key, i, MyJsonUtil.object_to_json(list.get(i)));
		}
	}

	public static boolean if_key(String key) {
		// 第三方数据调用
		Jedis jedis = null;
		try {
			jedis = JedisPoolUtils.getJedis();
		} catch (Exception e) {
			// 记日志
		}
		Boolean exists = jedis.exists(key);

		return exists;
	}

}
