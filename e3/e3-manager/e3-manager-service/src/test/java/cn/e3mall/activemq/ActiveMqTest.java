package cn.e3mall.activemq;

import java.awt.font.TextMeasurer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMqTest {

	/**
	 * 点到点形式发送消息
	 * @throws Exception
	 */
	
	public void testQueueProdeucer()throws Exception{
		//1.创建一个连接工厂对象，需要指定服务的ip
		//brokerURL服务器的ip和端口号
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//2.使用工厂对象创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3.开启连接，调用Connetion对象的start方法
		connection.start();
		//4.创建一个Session对象
		//第一个参数：是否开启事务。如果true开启事务，第二个参数无意义。一般不开启事务false。
		//第二个参数：应答模式。自动应答或者手动应答。一般自动应答。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5.使用Session对象创建一个Destination对象，两种形式queue、topic，现在应该使用queue
		//参数：队列名称
		Queue queue = session.createQueue("test-quque");
		//6.使用Session对象创建一个Producer对象。
		MessageProducer producer = session.createProducer(queue);
		//7.创建一个Message对象，可以使用TextMessage
		/*TextMessage message = new ActiveMQTextMessage();
		message.setText("hello activeMq,this is my first test.");*/
		TextMessage textMessage = session.createTextMessage("hello activeMq,this is my first test.");
		//8.发送消息
		producer.send(textMessage);
		//9.关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	public void testQueueConsumer() throws Exception{
				//创建一个ConnectionFactory对象连接MQ服务器
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
				//创建一个连接对象
				Connection connection = connectionFactory.createConnection();
				//开启连接
				connection.start();
				//使用Connection对象创建一个Session对象
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				//创建一个Destination对象。queue对象
				Queue queue = session.createQueue("spring-queue");
				//使用Session对象创建一个消费者对象。
				MessageConsumer consumer = session.createConsumer(queue);
				//接收消息
				consumer.setMessageListener(new MessageListener() {
					
					@Override
					public void onMessage(Message message) {
						//打印结果
						TextMessage textMessage = (TextMessage) message;
						String text;
						try {
							text = textMessage.getText();
							System.out.println(text);
						} catch (JMSException e) {
							e.printStackTrace();
						}
						
					}
				});
				//等待接收消息
				System.in.read();
				//关闭资源
				consumer.close();
				session.close();
				connection.close();
	}
	
	
	public void tessTopicProducer() throws Exception{
		//1.创建一个连接工厂对象，需要指定服务的ip
				//brokerURL服务器的ip和端口号
				ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
				//2.使用工厂对象创建一个Connection对象
				Connection connection = connectionFactory.createConnection();
				//3.开启连接，调用Connetion对象的start方法
				connection.start();
				//4.创建一个Session对象
				//第一个参数：是否开启事务。如果true开启事务，第二个参数无意义。一般不开启事务false。
				//第二个参数：应答模式。自动应答或者手动应答。一般自动应答。
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				//5.使用Session对象创建一个Destination对象，两种形式queue、topic，现在应该使用queue
				//参数：队列名称
				Topic topic = session.createTopic("test-topic");
				//6.使用Session对象创建一个Producer对象。
				MessageProducer producer = session.createProducer(topic);
				//7.创建一个Message对象，可以使用TextMessage
				/*TextMessage message = new ActiveMQTextMessage();
				message.setText("hello activeMq,this is my first test.");*/
				TextMessage textMessage = session.createTextMessage("topic messages");
				//8.发送消息
				producer.send(textMessage);
				//9.关闭资源
				producer.close();
				session.close();
				connection.close();
	}
	
	public void tessTopicConsumer() throws Exception{
		//创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象。queue对象
		Topic topic = session.createTopic("test-topic");
		//使用Session对象创建一个消费者对象。
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		System.out.println("消费者1启动成功");
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	public void tessTopicConsumer3() throws Exception{
		//创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象。queue对象
		Topic topic = session.createTopic("test-topic");
		//使用Session对象创建一个消费者对象。
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		System.out.println("消费者3启动成功");
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	public void tessTopicConsumer2() throws Exception{
		//创建一个ConnectionFactory对象连接MQ服务器
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个连接对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个Destination对象。queue对象
		Topic topic = session.createTopic("test-topic");
		//使用Session对象创建一个消费者对象。
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				String text;
				try {
					text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
				
			}
		});
		System.out.println("消费者2启动成功");
		//等待接收消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
