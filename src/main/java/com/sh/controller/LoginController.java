package com.sh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.service.LoginService;
import com.sh.webDomain.LoginFormDTO;
import com.sh.webDomain.SessionConst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
@Slf4j
public class LoginController {
	
	private final LoginService service;
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		session.invalidate();
		
		
		log.info("============ LOGOUT =============");
		
		return "redirect:/home";
	}
	
	/*
	 * 일반, 봉사, 기업 선택
	 * 로그인 페이지
	 * 
	 * 
	 */
	@GetMapping("/main")
	public String login() {
		
		return "login/login-main";
	}
	
	/*
	 * 일반 유저 로그인 페이지
	 * login하지 않은 회원은 다시 로그인하러
	 * 딱 걸린 requestURI가 쿼리스트링으로 붙어서 온다.
	 */
	@GetMapping("/common")
	public String common(Model model) {

		LoginFormDTO loginForm = new LoginFormDTO();
		model.addAttribute("loginForm", loginForm);
		
		log.info("========= COMMON LOGIN PAGE ==========");
		
		return "login/login-common";
	}

	/*
	 * 봉사단체 유저 로그인 페이지
	 */
	@GetMapping("/volunteer")
	public String volunteer(Model model) {
		LoginFormDTO loginForm = new LoginFormDTO();
		model.addAttribute("loginForm", loginForm);

		log.info("========= VOLUNTEER ORG LOGIN PAGE ==========");
		
		return "login/login-volunteer";
	}
	
	/*
	 * 일반 유저 로그인 페이지
	 */
	@GetMapping("/charity")
	public String charity(Model model) {
		LoginFormDTO loginForm = new LoginFormDTO();
		model.addAttribute("loginForm", loginForm);
		
		log.info("========= CHARITY ORG LOGIN PAGE ==========");
		
		return "login/login-charity";
	}
	
	/*
	 * 로그인 후 메인 페이지
	 * 세션 넣기
	 * 필터, 서블릿 유효성
	 * 홈에서 검증해서 @@@님 환영행ㅇ~~~~
	 * 
	 * form에서 post로
	 * 
	 * 일반 유저가 로그인 후 메인 페이지로
	 * interceptor에서 걸린 uri가 붙어서 파라미터에 붙어서 온다.(/~~~)
	 */
	@PostMapping("/common")
	public String loginCommon(@ModelAttribute("loginForm") LoginFormDTO loginForm,
								BindingResult bindingResult,
								@RequestParam(defaultValue = "/") String redirectURL,
								HttpServletRequest request) {
		
		
		String user_id = loginForm.getUser_id();
		String user_pw = loginForm.getUser_pw();
		
		if(ObjectUtils.isEmpty(user_id)) {
			bindingResult.rejectValue("user_id", "login_id");
		}
		if(!ObjectUtils.isEmpty(user_id)) {
			if(!service.cuCheckId(user_id)) {
				bindingResult.rejectValue("user_id", "login_id");
			}
		}
		if(ObjectUtils.isEmpty(user_pw)) {
			bindingResult.rejectValue("user_pw", "login_pw");
		}
		
		
		if(bindingResult.hasErrors()) {
			log.info("========== COMMON USER LOGIN FAIL ==============");
			return "login/login-common";
		}
		
		// 성공, 
		if(service.cuLogin(loginForm)) {
			
			HttpSession session = request.getSession(true);
			
			UserDTO user = service.cuUserInfo(user_id);
			
			session.setAttribute(SessionConst.COMMON_USER, user);
			
			log.info("=========== COMMON USER LOGIN SUECCESS ============");
			
			
			return "redirect:" + redirectURL;
		}
		

		
		// 실패
		bindingResult.reject("loginFail");
		log.info("========== COMMON USER LOGIN FAIL ==============");
		
		return "login/login-common";
	}
	
	@PostMapping("/volunteer")
	public String loginVolunteer(@ModelAttribute("loginForm") LoginFormDTO loginForm,
								BindingResult bindingResult,
								HttpServletRequest request) {
		
		String user_id = loginForm.getUser_id();
		String user_pw = loginForm.getUser_pw();
		
		if(ObjectUtils.isEmpty(user_id)) {
			bindingResult.rejectValue("user_id", "login_id");
		}
		if(!ObjectUtils.isEmpty(user_id)) {
			if(!service.voCheckId(user_id)) {
				bindingResult.rejectValue("user_id", "login_id");
			}
		}
		if(ObjectUtils.isEmpty(user_pw)) {
			bindingResult.rejectValue("user_pw", "login_pw");
		}
		
		
		if(bindingResult.hasErrors()) {
			log.info("========== VOlUNTEER ORG LOGIN FAIL ==============");
			return "login/login-volunteer";
		}
		
		log.info(loginForm.toString());
		// 성공, 
		if(service.voLogin(loginForm)) {
			
			HttpSession session = request.getSession(true);
			
			VolunteerOrgDTO user = service.voUserInfo(user_id);
			
			session.setAttribute(SessionConst.VOLUNTEER_ORG_USER, user);
			
			log.info("=========== VOlUNTEER ORG LOGIN SUECCESS ============");
			
			
			return "redirect:/volunteerOrg/home";
		}
		

		
		// 실패
		bindingResult.reject("loginFail");
		log.info("========== VOlUNTEER ORG LOGIN FAIL ==============");
		
		return "login/login-volunteer";
	}
	
	@PostMapping("/charity")
	public String loginCharity(@ModelAttribute("loginForm") LoginFormDTO loginForm,
								BindingResult bindingResult,
								HttpServletRequest request) {
		
		String user_id = loginForm.getUser_id();
		String user_pw = loginForm.getUser_pw();
		
		if(ObjectUtils.isEmpty(user_id)) {
			bindingResult.rejectValue("user_id", "login_id");
		}
		if(!ObjectUtils.isEmpty(user_id)) {
			if(!service.coCheckId(user_id)) {
				bindingResult.rejectValue("user_id", "login_id");
			}
		}
		if(ObjectUtils.isEmpty(user_pw)) {
			bindingResult.rejectValue("user_pw", "login_pw");
		}
		
		
		if(bindingResult.hasErrors()) {
			log.info("========== CHARITY ORG LOGIN FAIL ==============");
			return "login/login-charity";
		}
		
		// 성공, 
		if(service.coLogin(loginForm)) {
			
			HttpSession session = request.getSession(true);
			
			CharityOrgDTO user = service.coUserInfo(user_id);
			
			session.setAttribute(SessionConst.CHARITY_ORG_USER, user);
			
			log.info("=========== CHARITY ORG LOGIN SUECCESS ============");
			
			
			return "redirect:/charityOrg/home";
		}
		

		
		// 실패
		bindingResult.reject("loginFail");
		log.info("========== CHARITY ORG LOGIN FAIL ==============");
		
		return "login/login-charity";
	}
}
