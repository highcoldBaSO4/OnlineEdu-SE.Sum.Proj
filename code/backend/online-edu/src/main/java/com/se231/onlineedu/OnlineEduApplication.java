package com.se231.onlineedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * OnlineEduApplication class
 *
 * Entry class
 *
 * @author Yuxuan Liu
 *
 * @date 2019/07/01
 */
@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class OnlineEduApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OnlineEduApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(OnlineEduApplication.class);
	}


}
