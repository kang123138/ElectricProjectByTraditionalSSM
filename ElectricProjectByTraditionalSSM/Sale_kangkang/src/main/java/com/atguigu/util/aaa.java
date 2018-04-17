package com.atguigu.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class aaa {
	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://192.168.22.45:8888/restfultest/crm/customer/0110");
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String result = EntityUtils.toString(entity, "UTF-8");
			System.out.println(result);
		} else {
			String result = EntityUtils.toString(entity, "UTF-8");
			System.err.println(result);
		}
		
		EntityUtils.consume(entity);
		httpClient.close();
	}
}
