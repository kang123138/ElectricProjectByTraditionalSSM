package com.atguigu.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.mapper.IndexMapper;

// 订阅模式
public class MyMessageListener2 implements MessageListener {
	
	@Autowired
	private IndexMapper indexMapper;
	
	@Override
	public void onMessage(Message message) {
		// 消息内容-- 判断是什么消息
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			// 根据消息内容执行对应的任务
			indexMapper.log(text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
}