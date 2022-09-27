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
	 * 기부 단체 리스트
	 * form에서 get으로
	 */
	@GetMapping("/list")
	public String list(Model model) {
		
		List<DonationInfoDTO> donationList = service.getDonationBoardList();
		
		model.addAttribute("donationList", donationList);
		
		log.info("============= DONATION LIST PAGE ============");
		
		return "donationUser/donation-user-list";
	}
	
	/*
	 * 기부 단체가 올린 정보들
	 * form에서 get으로 
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
	 * 검색 ajax
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
	 * file 다운로드
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
	 * 현금 결제 페이지
	 * form에서 post로
	 */
	@GetMapping("/payment-cash/{c_board_num}")
	public String paymentCash(@PathVariable("c_board_num") int c_board_num, Model model) {
		
		log.info("============= PAYMENT CASH PAGE ===============");
		
		model.addAttribute("c_board_num", c_board_num);
		
		return "donationUser/donation-user-cash";
	}
	
	/*
	 * 포인트 결제 페이지
	 * form에서 post로
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
	 * 현금 결제 완료 후 기부완료 페이지
	 * redirect하고 main으로
	 */
	@PostMapping("/complete-cash/{c_board_num}")
	public String completeCash(@ModelAttribute DonationPaymentCashDTO paymentInfo, RedirectAttributes redirect, 
								HttpServletRequest request,
								@PathVariable("c_board_num") int c_board_num, Model model) {
		
		HttpSession session = request.getSession(false);
		UserDTO user = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = user.getU_sys_id();
		
		if(!service.checkCreditCard(paymentInfo)) {
			redirect.addFlashAttribute("creditCardError", "신용카드 정보를 다시 확인해주세요.");
			return "redirect:/donationUser/payment-cash/"+c_board_num;
		}
		
		log.info("============= COMPLETE CASH PAGE ===============");
		
		/*
		 * 기부 완료 및 결제정보 저장
		 */
					
		if(service.saveDonationPaymentCash(paymentInfo, c_board_num, u_sys_id, DonationType.CASH)) {
			log.info("=============  SAVE DONATION PAYMENT ORG SUCCESS - CASH ===============");
			
			return "redirect:/donationUser/completeInfo-cash";
		}
		else {
			log.error("============[ERROR] SAVE DONATION COMPLETE INFO ORG ERROR - CASH ============");
		}
		
		
		log.error("============[ERROR] SAVE DONATION PAYMENT ERROR - CASH============");
		
		redirect.addFlashAttribute("dbError", "기부에 실패하였습니다. 다시 시도해 주세요.");
		
		return "redirect:/donationUser/payment-cash/"+c_board_num;
	}
	
	/*
	 * 포인트 결제 완료 후 기부완료 페이지
	 * redirect하고 main으로
	 */
	@PostMapping("/complete-point/{c_board_num}")
	public String completePoint(@ModelAttribute("pointDTO") DonationPaymentPointDTO paymentInfo,
								HttpServletRequest request, @PathVariable("c_board_num") int c_board_num, Model model,
								RedirectAttributes redirect) {
		
		
		HttpSession session = request.getSession(false);
		UserDTO user = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = user.getU_sys_id();
		
		// 빈 값 넘어옴ㄴ --> typemis
		
		Integer donationPoint = paymentInfo.getAmount_of_point();
		
		
		if(!service.checkAvailablePoint(donationPoint, u_sys_id)) {
			
			log.info("=============[ERROR] DONATION POINT FAIL - POINT ===============");

			redirect.addFlashAttribute("pointFail", "pointFail");
			// redierct해서 binding 에러 정보는 없어짐
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
		
		// redierct해서 binding 에러 정보는 없어짐
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
