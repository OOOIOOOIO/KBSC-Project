package com.sh.controller;

import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sh.domain.CharityOrgDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.service.JoinService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/join")
@Slf4j
public class JoinController {

	private final JoinService service;
	
	/*
	 * 회원가입 분기 페이지
	 */
	@GetMapping("/main")
	public String main() {
		
		return "/join/join-main";
	}
	
	/*
	 * 일반회원 회원가입 페이지
	 */
	@GetMapping("/common")
	public String commonJoin(Model model) {
		UserDTO commonUser = new UserDTO();
		model.addAttribute("commonUser", commonUser);
		
		log.info("============ COMMON JOIN PAGE ===========");
		
		return "join/join-common";
	}
	
	/*
	 * 봉사단체 회원가입 페이지
	 */
	@GetMapping("/volunteer")
	public String volunteerJoin(Model model) {
		VolunteerOrgDTO volunteerUser = new VolunteerOrgDTO();
		model.addAttribute("volunteerUser", volunteerUser);
		
		log.info("============ VOLUNTEER JOIN PAGE ===========");
		
		return "join/join-volunteer";
	}
	
	/*
	 * 기부단체 회원가입 페이지
	 */
	@GetMapping("/charity")
	public String donationJoin(Model model) {
		CharityOrgDTO charityUser = new CharityOrgDTO();
		model.addAttribute("charityUser", charityUser);
		
		
		log.info("============ CHARITY JOIN PAGE ===========");
		
		return "join/join-charity";
	}
	
	/*
	 * 회원가입 후 메인 페이지
	 * commonUser
	 * volunteer
	 * charity
	 * 
	 * pw 암호화
	 * 에러
	 *  
	 * @ModelAttribute("객체명") 
	 * RedirectAttributes redirectAttributes --> 로그인할 때
	 * BindingResult bindresult --> Bean Validation으로 해결하지 못 한 에러(글로벌 에러) 설정
	 * 
	 * redirect 후 메인으로
	 */
	@PostMapping("/complete-common")
	public String completeCommon(@Validated @ModelAttribute("commonUser") UserDTO commonUser, BindingResult bindingResult) {
		
		String name_kr = commonUser.getName_kr();
		String name_en = commonUser.getName_en();
		String user_id = commonUser.getUser_id();
		String user_pw = commonUser.getUser_pw();
		String user_phone = commonUser.getUser_phone();
		String user_email = commonUser.getUser_email();
		String user_birth = commonUser.getUser_birth();
		String zipcode = commonUser.getZipcode();
		String addr = commonUser.getAddr();
		String addr_detail = commonUser.getAddr_detail();
		String user_job = commonUser.getUser_job();
		log.info("============================"+commonUser.toString());
		
		if(ObjectUtils.isEmpty(name_kr)) {
			bindingResult.rejectValue("name_kr", "required");
		}
		if(ObjectUtils.isEmpty(name_en)) {
			bindingResult.rejectValue("name_en", "required");
		}
		
		if(ObjectUtils.isEmpty(user_id) || !(Pattern.matches("^[a-zA-Z0-9]{4,12}$", user_id))) {
			bindingResult.rejectValue("user_id", "required");
		}
		if(!ObjectUtils.isEmpty(user_id)) {
			if(service.cuCheckId(user_id)) {
				bindingResult.rejectValue("user_id", "duplicate");
			}
		}
			
		
		if(ObjectUtils.isEmpty(user_pw) || !(Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", user_pw))) {
			bindingResult.rejectValue("user_pw", "required");
		}
		if(ObjectUtils.isEmpty(user_phone) || !(Pattern.matches("^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", user_phone))) {
			bindingResult.rejectValue("user_phone", "required");
		}
		if(!ObjectUtils.isEmpty(user_phone)) {
			if(service.cuCheckId(user_phone)) {
				bindingResult.rejectValue("user_phone", "duplicate");
			}
		}
		if(ObjectUtils.isEmpty(user_email) || !(Pattern.matches("^([0-9a-zA-Z_\\.-]+)@([0-9a-zA-Z_-]+)(\\.[0-9a-zA-Z_-]+){1,2}$", user_email))) {
			bindingResult.rejectValue("user_email", "required");
		}
		if(!ObjectUtils.isEmpty(user_email)) {
			if(service.cuCheckId(user_email)) {
				bindingResult.rejectValue("user_email", "duplicate");
			}
		}
		if(ObjectUtils.isEmpty(user_birth) || !(Pattern.matches("^[0-9]{8}$", user_birth))) {
			bindingResult.rejectValue("user_birth", "required");
		}
		if(ObjectUtils.isEmpty(zipcode)) {
			bindingResult.rejectValue("zipcode", "required");
		}
		if(ObjectUtils.isEmpty(addr)) {
			bindingResult.rejectValue("addr", "required");
		}
		if(ObjectUtils.isEmpty(addr_detail)) {
			bindingResult.rejectValue("addr_detail", "required");
		}
		if(ObjectUtils.isEmpty(user_job)) {
			bindingResult.rejectValue("user_job", "required");
		}
		
		if(bindingResult.hasErrors()) {
			log.info("====== JOIN FAIL1 ======");
			log.info(bindingResult.toString());
			
			return "join/join-common";
		}
		
		
		if(service.cuJoin(commonUser)) {
			log.info("====== JOIN SUCCESS ======");
			
			return "redirect:/login/main";
		}
		else {
			log.info("====== JOIN FAIL2 ======");
			bindingResult.reject("joinFail");
			return "join/join-common";
		}
		
	}
	@PostMapping("/complete-volunteer")
	public String completeVolunteer(@Validated @ModelAttribute("volunteerUser") VolunteerOrgDTO volunteerUser, BindingResult bindingResult) {
		
		String name_kr = volunteerUser.getName_kr();
		String name_en = volunteerUser.getName_en();
		String user_id = volunteerUser.getUser_id();
		String user_pw = volunteerUser.getUser_pw();
		String user_phone = volunteerUser.getUser_phone();
		String user_email = volunteerUser.getUser_email();
		String user_birth = volunteerUser.getUser_birth();
		String zipcode = volunteerUser.getZipcode();
		String addr = volunteerUser.getAddr();
		String addr_detail = volunteerUser.getAddr_detail();
		String org_name = volunteerUser.getOrg_name();
		String org_phone = volunteerUser.getOrg_phone();
		String business_num = volunteerUser.getBusiness_num();
		
		if(ObjectUtils.isEmpty(name_kr)) {
			bindingResult.rejectValue("name_kr", "required");
		}
		if(ObjectUtils.isEmpty(name_en)) {
			bindingResult.rejectValue("name_en", "required");
		}
		
		if(ObjectUtils.isEmpty(user_id) || !(Pattern.matches("^[a-zA-Z0-9]{4,12}$", user_id))) {
			bindingResult.rejectValue("user_id", "required");
		}
		if(!ObjectUtils.isEmpty(user_id)) {
			if(service.voCheckId(user_id)) {
				bindingResult.rejectValue("user_id", "duplicate");
			}
		}
			
		
		if(ObjectUtils.isEmpty(user_pw) || !(Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", user_pw))) {
			bindingResult.rejectValue("user_pw", "required");
		}
		if(ObjectUtils.isEmpty(user_phone) || !(Pattern.matches("^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", user_phone))) {
			bindingResult.rejectValue("user_phone", "required");
		}
		if(!ObjectUtils.isEmpty(user_phone)) {
			if(service.voCheckId(user_phone)) {
				bindingResult.rejectValue("user_phone", "duplicate");
			}
		}
		if(ObjectUtils.isEmpty(user_email) || !(Pattern.matches("^([0-9a-zA-Z_\\.-]+)@([0-9a-zA-Z_-]+)(\\.[0-9a-zA-Z_-]+){1,2}$", user_email))) {
			bindingResult.rejectValue("user_email", "required");
		}
		if(!ObjectUtils.isEmpty(user_email)) {
			if(service.voCheckId(user_email)) {
				bindingResult.rejectValue("user_email", "duplicate");
			}
		}
		if(ObjectUtils.isEmpty(user_birth) || !(Pattern.matches("^[0-9]{8}$", user_birth))) {
			bindingResult.rejectValue("user_birth", "required");
		}
		if(ObjectUtils.isEmpty(zipcode)) {
			bindingResult.rejectValue("zipcode", "required");
		}
		if(ObjectUtils.isEmpty(addr)) {
			bindingResult.rejectValue("addr", "required");
		}
		if(ObjectUtils.isEmpty(addr_detail)) {
			bindingResult.rejectValue("addr_detail", "required");
		}
		if(ObjectUtils.isEmpty(org_name)) {
			bindingResult.rejectValue("org_name", "required");
		}
		if(ObjectUtils.isEmpty(org_phone) || !(Pattern.matches("^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))-(\\d{3,4})-(\\d{4})$", org_phone))) {
			bindingResult.rejectValue("org_phone", "required");
		}
		if(ObjectUtils.isEmpty(business_num) || !(Pattern.matches("([0-9]{3})-?([0-9]{2})-?([0-9]{5})", business_num))) {
			bindingResult.rejectValue("business_num", "required");
		}
		
		if(bindingResult.hasErrors()) {
			log.info("====== JOIN FAIL1 ======");
			log.info(bindingResult.toString());
			
			return "join/join-volunteer";
		}
		
		if(service.voJoin(volunteerUser)) {
			log.info("====== JOIN SUCCESS ======");
			
			return "redirect:/login/main";
		}
		else {
			log.info("====== JOIN FAIL2 ======");
			bindingResult.reject("joinFail");
			return "join/join-volunteer";
		}
		
		
	}
	@PostMapping("/complete-charity")
	public String completeCharity(@Validated @ModelAttribute("charityUser") CharityOrgDTO charityUser, BindingResult bindingResult) {
		
		
		String name_kr = charityUser.getName_kr();
		String name_en = charityUser.getName_en();
		String user_id = charityUser.getUser_id();
		String user_pw = charityUser.getUser_pw();
		String user_phone = charityUser.getUser_phone();
		String user_email = charityUser.getUser_email();
		String user_birth = charityUser.getUser_birth();
		String zipcode = charityUser.getZipcode();
		String addr = charityUser.getAddr();
		String addr_detail = charityUser.getAddr_detail();
		String org_name = charityUser.getOrg_name();
		String org_phone = charityUser.getOrg_phone();
		String business_num = charityUser.getBusiness_num();
		
		if(ObjectUtils.isEmpty(name_kr)) {
			bindingResult.rejectValue("name_kr", "required");
		}
		if(ObjectUtils.isEmpty(name_en)) {
			bindingResult.rejectValue("name_en", "required");
		}
		
		if(ObjectUtils.isEmpty(user_id) || !(Pattern.matches("^[a-zA-Z0-9]{4,12}$", user_id))) {
			bindingResult.rejectValue("user_id", "required");
		}
		if(!ObjectUtils.isEmpty(user_id)) {
			if(service.coCheckId(user_id)) {
				bindingResult.rejectValue("user_id", "duplicate");
			}
		}
			
		
		if(ObjectUtils.isEmpty(user_pw) || !(Pattern.matches("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", user_pw))) {
			bindingResult.rejectValue("user_pw", "required");
		}
		if(!ObjectUtils.isEmpty(user_phone)) {
			if(service.coCheckId(user_phone)) {
				bindingResult.rejectValue("user_phone", "duplicate");
			}
		}
		if(ObjectUtils.isEmpty(user_email) || !(Pattern.matches("^([0-9a-zA-Z_\\.-]+)@([0-9a-zA-Z_-]+)(\\.[0-9a-zA-Z_-]+){1,2}$", user_email))) {
			bindingResult.rejectValue("user_email", "required");
		}
		if(!ObjectUtils.isEmpty(user_email)) {
			if(service.coCheckId(user_email)) {
				bindingResult.rejectValue("user_email", "duplicate");
			}
		}
		if(ObjectUtils.isEmpty(user_birth) || !(Pattern.matches("^[0-9]{8}$", user_birth))) {
			bindingResult.rejectValue("user_birth", "required");
		}
		if(ObjectUtils.isEmpty(zipcode)) {
			bindingResult.rejectValue("zipcode", "required");
		}
		if(ObjectUtils.isEmpty(addr)) {
			bindingResult.rejectValue("addr", "required");
		}
		if(ObjectUtils.isEmpty(addr_detail)) {
			bindingResult.rejectValue("addr_detail", "required");
		}
		if(ObjectUtils.isEmpty(org_name)) {
			bindingResult.rejectValue("org_name", "required");
		}
		if(ObjectUtils.isEmpty(org_phone) || !(Pattern.matches("^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))-(\\d{3,4})-(\\d{4})$", org_phone))) {
			bindingResult.rejectValue("org_phone", "required");
		}
		if(ObjectUtils.isEmpty(business_num) || !(Pattern.matches("([0-9]{3})-?([0-9]{2})-?([0-9]{5})", business_num))) {
			bindingResult.rejectValue("business_num", "required");
		}
		
		
		if(bindingResult.hasErrors()) {
			log.info("====== JOIN FAIL1 ======");
			log.info(bindingResult.toString());
			
			return "join/join-charity";
		}
		
		if(service.coJoin(charityUser)) {
			log.info("====== JOIN SUCCESS ======");
			
			return "redirect:/login/main";
		}
		else {
			log.info("====== JOIN FAIL2 ======");
			bindingResult.reject("joinFail");
			return "join/join-common";
		}
		
	}
	
}
