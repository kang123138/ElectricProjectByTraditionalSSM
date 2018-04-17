package com.atguigu.test;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.atguigu.bean.KEYWORDS_T_MALL_SKU;
import com.atguigu.factory.MySqlSessionFactory;
import com.atguigu.mapper.ClassMapper;
import com.atguigu.util.MyPropertiesUtil;

public class Test {

	public static void main(String[] args) throws Exception {
		SqlSessionFactory myF = MySqlSessionFactory.getMyF();

		ClassMapper mapper = myF.openSession().getMapper(ClassMapper.class);

		List<KEYWORDS_T_MALL_SKU> list_sku = mapper.select_list_by_flbh2(28);
	
		System.out.println(list_sku);
		
		// =====想solr中导入sku数据========
		 
		// 定义solr的server
		HttpSolrServer solr = new HttpSolrServer(MyPropertiesUtil.getMyProperty("solr.properties", "solr_sku1018"));
		// 设置响应解析器(在java和solr之间传输数据时，把java类型转换成xml格式)
		/**
		 * 在XMLResponseParser把java类型解析为xml文本格式时出现了问题；因为schema.xml文件中的配置的字段和KEYWORDS_T_MALL_SKU中的字段需要对应
		 * 起来
		 * 做法：在KEYWORDS_T_MALL_SKU即继承的T_MALL_SKU类中所有的字段上加@Field注解
		 * 好处：①并不是java类中的属性都要存储到solr的schema.xml文件中②加上该注解后，schema.xml中和java类中的字段可以不一样，降低了耦合性
		 */
		solr.setParser(new XMLResponseParser());
		// 把从数据库查询到的数据放入solr中
		solr.addBeans(list_sku);
		solr.commit();
		
		// ========查询solr中的数据=========
		// solrQuerysolr查询对象就相当于浏览器访问solr时的Request-Handler(qt)下面的一块区域中的所有内容
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询参数为sku_mch:小明
		solrQuery.setQuery("sku_mch:小明");
		// 设置查询的行数，相当于浏览器访问solr时的Request-Handler(qt)下面start, rows输入框
		solrQuery.setRows(50);
		// 把solrQuerysolr放入查询中，query就相当于浏览器访问solr时的Request-Handler(qt)下面的q输入框中的*:*，即查询参数
		QueryResponse query = solr.query(solrQuery);
		// 获得query对象中的数据转换为java中的对象
		List<KEYWORDS_T_MALL_SKU> beans = query.getBeans(KEYWORDS_T_MALL_SKU.class);
		System.out.println(beans);
	}

}
