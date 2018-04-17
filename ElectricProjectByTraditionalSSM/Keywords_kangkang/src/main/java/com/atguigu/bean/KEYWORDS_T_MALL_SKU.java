package com.atguigu.bean;

import org.apache.solr.client.solrj.beans.Field;

// 我们新建这个类因为solr不支持java中的引用类型T_MALL_PRODUCT等，但是我们需要spu.shp_tp，（这是在
//schema.xml中配置的）
public class KEYWORDS_T_MALL_SKU extends T_MALL_SKU {
	@Field("shp_tp")
	private String shp_tp;

	public String getShp_tp() {
		return shp_tp;
	}

	public void setShp_tp(String shp_tp) {
		this.shp_tp = shp_tp;
	}
}
