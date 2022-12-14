package com.sh.controller;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import com.sh.domain.CharityFileDTO;
import com.sh.domain.CharityOrgDTO;
import com.sh.domain.DonationInfoDTO;
import com.sh.domain.DonationPaymentCashDTO;
import com.sh.domain.DonationPaymentPointDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerInfoDTO;
import com.sh.service.DonationUserService;
import com.sh.service.FileService;
import com.sh.webDomain.DonationPaymentInfoDTO;
import com.sh.webDomain.VolunteerSearchFormDTO;
import com.sh.webDomain.DonationType;
import com.sh.webDomain.SessionConst;
import com.sh.webDomain.DonationSearchFormDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/donationUser")
@Slf4j
public class DonationUserController {
	
	private final DonationUserService service;
	private final FileService fileService;
	
	/*
	 * ?????? ?????? ?????????
	 * form?????? get??????
	 */
	@GetMapping("/list")
	public String list(Model model) {
		
		List<DonationInfoDTO> donationList = service.getDonationBoardList();
		
		model.addAttribute("donationList", donationList);
		
		log.info("============= DONATION LIST PAGE ============");
		
		return "donationUser/donation-user-list";
	}
	
	/*
	 * ?????? ????????? ?????? ?????????
	 * form?????? get?????? 
	 */
	@GetMapping("/information/{c_board_num}")
	public String information(@PathVariable("c_board_num") int c_board_num, Model model) {
		
		Long donationTimes = service.getDonationTimes(c_board_num);
		Long donationCash = service.getDonationAmountCash(c_board_num);
		Long donationPoint= service.getDonationAmountPoint(c_board_num);
		
		DonationInfoDTO donationInfo = service.getDonationBoardInfo(c_board_num);
		CharityOrgDTO cOInfo = service.getCharityOrgInfo(c_board_num);
		
		model.addAttribute("donationInfo", donationInfo);
		model.addAttribute("cOInfo", cOInfo);
		model.addAttribute("c_board_num", c_board_num);
		model.addAttribute("donationTimes", donationTimes);
		model.addAttribute("donationCash", donationCash);
		model.addAttribute("donationPoint", donationPoint);
		
		
		// file
		CharityFileDTO fileInfo= fileService.getCharityFile(c_board_num);
		if(fileInfo != null) {
			model.addAttribute("fileInfo", fileInfo);
			log.info("======== FILE INFO : "+ fileInfo.toString() + "=========");
			
		}
		
		log.info("============= DONATION INFO PAGE ============");
		log.info("============= GET DONATION INFO ============");
		
		
		return "donationUser/donation-user-information";
	}

	
	/*
	 * ?????? ajax
	 */
	@PostMapping(value ="/search",
			produces={
					MediaType.APPLICATION_JSON_UTF8_VALUE,
					MediaType.APPLICATION_XML_VALUE
			})
	@ResponseBody
	public ResponseEntity<List<DonationInfoDTO>> search(@RequestBody VolunteerSearchFormDTO searchForm){
		List<DonationInfoDTO> searchList = service.searchBoardList(searchForm);
		
		log.info(searchForm.toString());
		return  new ResponseEntity<List<DonationInfoDTO>>(searchList, HttpStatus.OK);
	}
	
	/*
	 * file ????????????
	 */
	@GetMapping("/file/{c_board_num}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable("c_board_num") int c_board_num) throws MalformedURLException{
		
		CharityFileDTO fileInfo = fileService.getCharityFile(c_board_num);
		
		String systemFileName = fileInfo.getSystem_file_name();
		String originalFileName = fileInfo.getOriginal_file_name();
		
		log.info("================= "+originalFileName);
		log.info("================= "+systemFileName);
		
		UrlResource urlResource = new UrlResource("file:"+fileService.getFullPath(systemFileName));
		
		log.info(urlResource.toString());
		
		String encodedOriginalFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);
		String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";
		
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(urlResource);
		
	}
	
	
	/*/
	 * ?????? ?????? ?????????
	 * form?????? post???
	 */
	@GetMapping("/payment-cash/{c_board_num}")
	public String paymentCash(@PathVariable("c_board_num") int c_board_num, Model model) {
		
		log.info("============= PAYMENT CASH PAGE ===============");
		
		model.addAttribute("c_board_num", c_board_num);
		
		return "donationUser/donation-user-cash";
	}
	
	/*
	 * ????????? ?????? ?????????
	 * form?????? post???
	 */
	@GetMapping("/payment-point/{c_board_num}")
	public String paymentPoint(@PathVariable("c_board_num") int c_board_num, Model model, HttpServletRequest request) {
		
		log.info("============= PAYMENT POINT PAGE ===============");
		
		HttpSession session = request.getSession(false);
		UserDTO user = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = user.getU_sys_id();
		
		int myPoint = service.getMyRestPoint(u_sys_id);
		
		model.addAttribute("myPoint", myPoint);
		model.addAttribute("c_board_num", c_board_num);
		return "donationUser/donation-user-point";
	}
	
	
	/*
	 * ?????? ?????? ?????? ??? ???????????? ?????????
	 * redirect?????? main??????
	 */
	@PostMapping("/complete-cash/{c_board_num}")
	public String completeCash(@ModelAttribute DonationPaymentCashDTO paymentInfo, RedirectAttributes redirect, 
								HttpServletRequest request,
								@PathVariable("c_board_num") int c_board_num, Model model) {
		
		HttpSession session = request.getSession(false);
		UserDTO user = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = user.getU_sys_id();
		
		if(!service.checkCreditCard(paymentInfo)) {
			redirect.addFlashAttribute("creditCardError", "???????????? ????????? ?????? ??????????????????.");
			return "redirect:/donationUser/payment-cash/"+c_board_num;
		}
		
		log.info("============= COMPLETE CASH PAGE ===============");
		
		/*
		 * ?????? ?????? ??? ???????????? ??????
		 */
					
		if(service.saveDonationPaymentCash(paymentInfo, c_board_num, u_sys_id, DonationType.CASH)) {
			log.info("=============  SAVE DONATION PAYMENT ORG SUCCESS - CASH ===============");
			
			return "redirect:/donationUser/completeInfo-cash";
		}
		else {
			log.error("============[ERROR] SAVE DONATION COMPLETE INFO ORG ERROR - CASH ============");
		}
		
		
		log.error("============[ERROR] SAVE DONATION PAYMENT ERROR - CASH============");
		
		redirect.addFlashAttribute("dbError", "????????? ?????????????????????. ?????? ????????? ?????????.");
		
		return "redirect:/donationUser/payment-cash/"+c_board_num;
	}
	
	/*
	 * ????????? ?????? ?????? ??? ???????????? ?????????
	 * redirect?????? main??????
	 */
	@PostMapping("/complete-point/{c_board_num}")
	public String completePoint(@ModelAttribute("pointDTO") DonationPaymentPointDTO paymentInfo,
								HttpServletRequest request, @PathVariable("c_board_num") int c_board_num, Model model,
								RedirectAttributes redirect) {
		
		
		HttpSession session = request.getSession(false);
		UserDTO user = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = user.getU_sys_id();
		
		// ??? ??? ???????????? --> typemis
		
		Integer donationPoint = paymentInfo.getAmount_of_point();
		
		
		if(!service.checkAvailablePoint(donationPoint, u_sys_id)) {
			
			log.info("=============[ERROR] DONATION POINT FAIL - POINT ===============");

			redirect.addFlashAttribute("pointFail", "pointFail");
			// redierct?????? binding ?????? ????????? ?????????
			return "redirect:/donationUser/payment-point/"+c_board_num;
		}
		
		
		log.info("============= COMPLETE POINT PAGE===============");
		
		if(service.saveDonationPaymentPoint(paymentInfo, c_board_num, u_sys_id, DonationType.POINT)) {
			log.info("=============  SAVE DONATION PAYMENT ORG SUCCESS - POINT ===============");
			return "redirect:/donationUser/completeInfo-point";
		}
		else {
			log.error("============[ERROR] SAVE DONATION COMPLETE INFO ORG ERROR - POINT ============");
		}
			
		
		log.error("============[ERROR] SAVE DONATION PAYMENT ERROR - POINT============");
		
		// redierct?????? binding ?????? ????????? ?????????
		redirect.addFlashAttribute("dbFail", "dbFail");
		
		return "redirect:/donationUser/payment-point/"+c_board_num;
	}

	@GetMapping("completeInfo-cash")
	public String completeInfoCash(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		
		UserDTO user = (UserDTO) session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = user.getU_sys_id();
		
		DonationPaymentInfoDTO paymentInfo = service.getRecentDonationCashInfo(u_sys_id);
		
		model.addAttribute("paymentInfo", paymentInfo);
		
		return "donationUser/donation-user-complete-cash";
	}
	
	@GetMapping("completeInfo-point")
	public String completeInfoPoint(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		
		UserDTO user = (UserDTO) session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = user.getU_sys_id();
		
		DonationPaymentInfoDTO paymentInfo = service.getRecentDonationPointInfo(u_sys_id);
		
		log.info("================" + paymentInfo.toString());
		
		model.addAttribute("paymentInfo", paymentInfo);
		
		return "donationUser/donation-user-complete-point";
	}

	
}
