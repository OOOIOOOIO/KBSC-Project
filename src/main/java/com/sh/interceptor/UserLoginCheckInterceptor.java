package com.sh.interceptor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.servlet.HandlerInterceptor;

import com.sh.webDomain.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class UserLoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    	// 요청이 들어온 경로(인터셉터 걸린 경로)
        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉터 실행 : URI={}", requestURI);

        HttpSession session = request.getSession(false);

        
        
        if(session != null && session.getAttribute(SessionConst.CHARITY_ORG_USER) != null ) {
        	log.info("====== CHARITY_ORG_USER INTERCEPTOR ======");
        	return true;
        }
        else if(session != null && session.getAttribute(SessionConst.VOLUNTEER_ORG_USER) != null) {
        	log.info("====== VOLUNTEER_ORG_USER INTERCEPTOR ======");
        	return true;
        	
        }
        else if(session != null && session.getAttribute(SessionConst.COMMON_USER) != null) {
        	log.info("====== COMMON_USER INTERCEPTOR ======");
        	return true;
        }
        // session이 null일 경우
        else {
        	
            //로그인으로 redirect
//        	response.sendRedirect("/login/common?redirectURL=" + requestURI);
        	
        	log.info("======= 미인증 사용자 요청 ========");
        	response.sendRedirect("/login/main");
          
        	return false;
        }
        
    }
}
