package com.project.chatop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.project.chatop.security.RsaKeyProperties;




@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SpringSecurityAuthTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityAuthTestApplication.class, args);
	}

}
