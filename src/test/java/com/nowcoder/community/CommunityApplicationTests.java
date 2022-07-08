package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.dao.AlphaDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(classes =CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	void testApplicationContext(){
		System.out.println(applicationContext);
//		获取容器自动装配的Bean
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);//按照类型进行配置
		System.out.println(alphaDao.select());

		alphaDao = applicationContext.getBean("alphaHibernate", AlphaDao.class);
		System.out.println(alphaDao.select());
	}

	@Test
	void testBeanManagement(){
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);//按照类型进行配置
		System.out.println(alphaService);
//		默认是单例的，只实例化一次，如果不要单例，需要在bean中添加注解@Scope("prototype") //默认参数是单例singleton
		alphaService = applicationContext.getBean(AlphaService.class);//按照类型进行配置
		System.out.println(alphaService);
	}

	@Test
	void testBeanConfig(){
		SimpleDateFormat simpleDateFormat =
				applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;
	@Autowired
	private AlphaService alphaService;
	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	void testDI(){
		System.out.println(alphaDao.select());
		System.out.println(alphaService);
		System.out.println(simpleDateFormat.format(new Date()));
	}

}
