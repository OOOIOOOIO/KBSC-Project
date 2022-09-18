package com.sh.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sh.domain.DonationCompleteUserDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.domain.VolunteerUserCompleteDTO;
import com.sh.service.UserInfoService;
import com.sh.webDomain.MyDonationInfoDTO;
import com.sh.webDomain.SessionConst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserInfoController {
	
	private final UserInfoService service;
	
	@GetMapping("/info")
	public String userInfo(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		
		UserDTO userInfo = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		
		String u_sys_id = userInfo.getU_sys_id();
		
		
		int volunteerApplyTimes = service.getVolunteerApplyTimes(u_sys_id);
		int volunteerCompleteTimes = service.getVolunteerCompleteTimes(u_sys_id);
		int donationCompleteTime = service.getDonationCompleteTimes(u_sys_id);
		Long donationAmountCash = service.getDonationAmountTotalCash(u_sys_id);
		int donationAmountPoint = service.getDonationAmountTotalPoint(u_sys_id);
		int restVolunteerPoint = service.getMyRestPoint(u_sys_id);
		
		model.addAttribute("applyTimes", volunteerApplyTimes);
		model.addAttribute("completeTimes", volunteerCompleteTimes);
		model.addAttribute("donationCompleteTime", donationCompleteTime);
		model.addAttribute("donationAmountCash", donationAmountCash);
		model.addAttribute("donationAmountPoint", donationAmountPoint);
		model.addAttribute("restVolunteerPoint", restVolunteerPoint);
		
		
		
		log.info("=============== USER INFO =============");
		log.info(u_sys_id);
		log.info(volunteerApplyTimes + "");
		log.info(volunteerCompleteTimes + "");
		
		
		return "userInfo/user-info";
	}
	
	@GetMapping("/volunteerHistory")
	public String volunteerHistory(HttpServletRequest request, Model model) {
		
		HttpSession session = request.getSession(false);
		
		UserDTO userInfo = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		
		String u_sys_id = userInfo.getU_sys_id();
		
		List<VolunteerUserCompleteDTO> completeInfo = service.getVolunteerCompleteHistory(u_sys_id); 

		
		model.addAttribute("completeInfo", completeInfo);
		
		return"/userInfo/user-volunteer-history";
	}
	
	@GetMapping("/donationHistory")
	public String donationHistory(HttpServletRequest request, Model model) {
		
		HttpSession session = request.getSession(false);
		
		UserDTO userInfo = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		
		String u_sys_id = userInfo.getU_sys_id();
		
		
		List<MyDonationInfoDTO> completeInfo = service.getDonationCompleteHistory(u_sys_id);
		
		
		
		model.addAttribute("completeInfo", completeInfo);
		
		return"/userInfo/user-donation-history";
	}


	
}





