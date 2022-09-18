package com.sh.confing;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sh.interceptor.UserLoginCheckInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer{
	/*
	 * 로그인 체크(session) 인터셉터 등록
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserLoginCheckInterceptor())
			.order(1)
			.addPathPatterns("/**")
			.excludePathPatterns("", "/home", "/join/**", "/login/**", 
								"/charityOrg/home", "/donationUser/list", "/donationUser/information/**",
								"/volunteerOrg/home", "/volunteerUser/list", "/volunteerUser/information/**",
								"/js/**", "/css/**", "/*.ico",
								"/error");
		
	}
	
}
