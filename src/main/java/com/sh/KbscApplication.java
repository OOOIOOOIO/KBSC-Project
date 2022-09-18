package com.sh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.sh.mapper")
public class KbscApplication {

	public static void main(String[] args) {
		SpringApplication.run(KbscApplication.class, args);
	}

}
