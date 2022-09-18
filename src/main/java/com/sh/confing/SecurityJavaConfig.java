package com.sh.confing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter{
	
	// 기본 설정
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.cors().disable() // cors 방지
			.csrf().disable() // csrf 방지
			.formLogin().disable() // 기본 페이지 없애기
			.headers().frameOptions().disable();
		
	}
	
	// Bcrype 설정
	@Bean
	public PasswordEncoder pwBcryptEncoder() {
		return new BCryptPasswordEncoder();
	}

}
