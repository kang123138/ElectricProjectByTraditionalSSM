package mq_1018.mq_1018_comsumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Hello world!
 *
 */
//订阅模式1:consumer在服务器上初始化一个永久保存的存储空间
public class App3 {
	public static void main(String[] args) throws Exception {
		xl();
	}

	public static void xl() throws Exception {
		// 创建连接
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		// ①创建连接时设置客户端id
		connection.setClientID("1");
		connection.start();

		// 创建 会话
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// 创建消息对象-订阅模式
		Topic topic = session.createTopic("office-topic");

		// 接收端
		// ②创建consumer的时候，改为了创建永久的订阅者，第二个参数指定了连接时的客户端id
		MessageConsumer consumer = session.createDurableSubscriber(topic, "1");

		// 接收消息
		consumer.setMessageListener(new MessageListener() {// 消息监听器，监听mq服务器的变化
			@Override
			public void onMessage(Message message) {
				// 打印结果
				TextMessage textMessage = (TextMessage) message;
				String text = "";
				try {
					text = textMessage.getText();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.err.println("夏雷老师，听到了" + text + ",不领工资了");
			}
		});
		// 等待接收消息
		System.in.read();
	}
}
