package com.atguigu.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.bean.KEYWORDS_T_MALL_SKU;
import com.atguigu.util.MyPropertiesUtil;

@Controller
public class KeywordsController {

	@RequestMapping(value = "keywords")
	@ResponseBody
	public List<KEYWORDS_T_MALL_SKU> keywords(String keywords) {
		// 调用solr，根据Sale项目传递过来的keywords关键字使用solr查询（在Test.java中）
		// 定义solr的server
		HttpSolrServer solr = new HttpSolrServer(MyPropertiesUtil.getMyProperty("solr.properties", "solr_sku1018"));
		// 设置响应解析器(在java和solr之间传输数据时，把java类型转换成xml格式)
		solr.setParser(new XMLResponseParser());
		
		// ========查询solr中的数据=========
		// solrQuerysolr查询对象就相当于浏览器访问solr时的Request-Handler(qt)下面的一块区域中的所有内容
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询参数为combine_item:关键字
		// combine_item是全文匹配功能的关键字，具体配置在solr安装目录下的solr搜索服务搭建.docx文件中
		solrQuery.setQuery("combine_item:" + keywords);
		// 设置查询的行数，相当于浏览器访问solr时的Request-Handler(qt)下面start, rows输入框
		solrQuery.setRows(50);
		// 把solrQuerysolr放入查询中，query就相当于浏览器访问solr时的Request-Handler(qt)下面的q输入框中的*:*，即查询参数
		QueryResponse query = null;
		try {
			query = solr.query(solrQuery);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		// 获得query对象中的数据转换为java中的对象
		List<KEYWORDS_T_MALL_SKU> list_sku = query.getBeans(KEYWORDS_T_MALL_SKU.class);
		return list_sku;
	}

}
