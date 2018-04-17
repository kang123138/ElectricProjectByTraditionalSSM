package mq_1018.mq_1018_producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Hello world!
 *
 */

//发布消息1：office-queue
public class App {
	public static void main(String[] args) throws JMSException {
		// 发送一个队列模式的消息
		// 其中的路径url不是http的协议：http://localhost:8161；我们默认使用的是tcp协议，端口号是61616（可以去文档中查看）
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();

		// 创建会话
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// 消息对象-具体队列名称，比如办公室里的队列
		Queue queue = session.createQueue("office-queue");

		// 消息内容
		TextMessage textMessage = session.createTextMessage("我渴了，来杯水...");

		// 发送端
		MessageProducer producer = session.createProducer(queue);

		// 发送消息
		producer.send(textMessage);

		// 关闭连接
		producer.close();
		session.close();
		connection.close();

	}
}
