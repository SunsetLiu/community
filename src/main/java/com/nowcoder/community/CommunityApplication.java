package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunityApplication {
//	底层启动了Tomcat，同时也做了Spring容器的创建，Spring容器创建后，会去扫描某些包下的某些bean
//	然后将been装配到容器中，具体有哪些bean，看下面的CommunityApplication.class这个类
//	因为这个类是@SpringBootApplication注解，是配置类，可以去看看这个注解的内容
//	@Component、@controller、@service、@Repository这几个注解加到Bean上，都可以被扫描，功能都是一样的，只是语义上有所不同
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);

	}

}
