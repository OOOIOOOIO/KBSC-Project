package com.sh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sh")
@MapperScan(basePackages = "com.sh.mapper")
public class KbscApplication {

	public static void main(String[] args) {
		SpringApplication.run(KbscApplication.class, args);
	}

}
