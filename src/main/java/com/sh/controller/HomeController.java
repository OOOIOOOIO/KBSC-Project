package com.sh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sh.service.HomeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

	/*
	 * 메인 페이지(유저)
	 *
	 * 
	 * 전체 봉사 기부 정보 service로 가져와야 함
	 */
	
	private final HomeService service;
	
	@GetMapping(value = {"", "/home"})
	public String home(HttpServletRequest request, Model model) {
				
		Long volunteerPoint = service.getVolunteerPoint();
		Long volunteerTimes = service.getVolunteerTimes();
		Long donationTimes = service.getDonationTimes();
		Long volunteerHours = service.getTotalVolunteerHours();
		Long donationPoint = service.getTotalDonationAmountByPoint();
		Long donationCash = service.getTotalDonationAmountByCash();
		
		model.addAttribute("volunteerTimes", volunteerTimes);
		model.addAttribute("volunteerHours", volunteerHours);
		model.addAttribute("donationTimes", donationTimes);
		model.addAttribute("donationCash", donationCash);
		model.addAttribute("volunteerPoint", volunteerPoint);
		model.addAttribute("donationPoint", donationPoint);
		  
		log.info("============= MAIN PAGE =============");
		
		return "home/home";
	}
}
