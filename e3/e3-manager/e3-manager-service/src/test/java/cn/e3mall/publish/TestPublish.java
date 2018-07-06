package cn.e3mall.publish;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPublish {

	public void publishService() throws Exception{
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		System.out.println("start!!!");
		System.in.read();
		System.out.println("end!!!");
	}
}
