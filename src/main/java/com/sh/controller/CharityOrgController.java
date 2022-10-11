package com.sh.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.sh.domain.CharityFileDTO;
import com.sh.domain.CharityOrgDTO;
import com.sh.domain.DonationInfoDTO;
import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerFileDTO;
import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.service.CharityOrgService;
import com.sh.service.FileService;
import com.sh.service.HomeService;
import com.sh.webDomain.DeleteBoardDTO;
import com.sh.webDomain.OrgSearchFormDTO;
import com.sh.webDomain.SessionConst;
import com.sh.webDomain.VolunteerSearchFormDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequiredArgsConstructor
@RequestMapping("/charityOrg")
@Slf4j
public class CharityOrgController {
	
	private final CharityOrgService service;
	private final HomeService homeService;
	private final FileService fileService;
	
	/*
	 * 봉사단체 홈
	 */
	@GetMapping("/home")
	public String home(HttpServletRequest request, Model model) {
		
		log.info("========= CHARITY HOME =========");
		
		Long volunteerPoint = homeService.getVolunteerPoint();
		Long volunteerTimes = homeService.getVolunteerTimes();
		Long donationTimes = homeService.getDonationTimes();
		Long volunteerHours = homeService.getTotalVolunteerHours();
		Long donationPoint = homeService.getTotalDonationAmountByPoint();
		Long donationCash = homeService.getTotalDonationAmountByCash();
		
		model.addAttribute("volunteerPoint", volunteerPoint);
		model.addAttribute("volunteerTimes", volunteerTimes);
		model.addAttribute("donationTimes", donationTimes);
		model.addAttribute("volunteerHours", volunteerHours);
		model.addAttribute("donationPoint", donationPoint);
		model.addAttribute("donationCash", donationCash);
		
		
		return "home/charityOrgHome";
	}


	/*
	 * 올린 봉사 리스트 보기
	 */
	@GetMapping("list")
	public String list(Model model) {
		
		log.info("=========== BOARD LIST PAGE ===========");
		
		List<DonationInfoDTO> list = service.getDonationBoardList();
		
		model.addAttribute("donationList", list);
		
		return "charityOrg/charity-org-list";
	}
	
	
	/*
	 * 봉사목록을 클릭해서 정보 보는 페이지
	 */
	@GetMapping("/information/{c_board_num}")
	public String information(@PathVariable("c_board_num") int c_board_num, 
							Model model, 
							HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		CharityOrgDTO userInfo = (CharityOrgDTO)session.getAttribute(SessionConst.CHARITY_ORG_USER);
		String c_sys_id = userInfo.getC_sys_id();
		
		Long donationTimes = service.getDonationTimes(c_board_num);
		Long donationCash = service.getDonationAmountCash(c_board_num);
		Long donationPoint= service.getDonationAmountPoint(c_board_num);
		
		DonationInfoDTO donationInfo = service.readDonationBoardInfo(c_board_num);
		CharityOrgDTO cOInfo = service.getCharityOrgInfo(c_sys_id);
		
		
		model.addAttribute("donationInfo", donationInfo);
		model.addAttribute("cOInfo", cOInfo);
		model.addAttribute("donationTimes", donationTimes);
		model.addAttribute("donationCash", donationCash);
		model.addAttribute("donationPoint", donationPoint);
		model.addAttribute("c_board_num", c_board_num);
		
		// file
		CharityFileDTO fileInfo= fileService.getCharityFile(c_board_num);
		if(fileInfo != null) {
			model.addAttribute("fileInfo", fileInfo);
			log.info("======== FILE INFO : "+ fileInfo.toString() + "=========");
			
		}
		
		log.info("========== READ VOLUNTEER INFO ========");
		log.info("======== BOARD NUM : "+ c_board_num + " =========");
		log.info("======== BOARD INFO : "+ donationInfo.toString() + " =========");
		log.info("======== VOLUNTEER ORG INFO : "+ cOInfo.toString() + " =========");
		
		return "charityOrg/charity-org-information";
	}
	
	/*
	 * 봉사 공고 등록
	 */
	@GetMapping("/regist")
	public String regist(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		
		CharityOrgDTO userInfo = (CharityOrgDTO)session.getAttribute(SessionConst.CHARITY_ORG_USER);
		String c_sys_id = userInfo.getC_sys_id();
		
		// 봉사단체 정보 가져오기
		CharityOrgDTO cOInfo = service.getCharityOrgInfo(c_sys_id);
		
		model.addAttribute("cOInfo", cOInfo);
		log.info("=========== REGIST PAGE ===========");
		log.info(cOInfo.toString());
		
		return "charityOrg/charity-org-regist";
	}
	
	/*
	 * 봉사 공고 등록
	 */
	@PostMapping("/regist")
	public String registInfo(@ModelAttribute DonationInfoDTO donationInfo,
							@RequestParam MultipartFile file,
							HttpServletRequest request) throws IllegalStateException, IOException {
		
		HttpSession session = request.getSession(false);
		CharityOrgDTO userInfo = (CharityOrgDTO)session.getAttribute(SessionConst.CHARITY_ORG_USER);
		String c_sys_id = userInfo.getC_sys_id();
		String org_name = userInfo.getOrg_name();
		donationInfo.setOrg_name(org_name);
		
		
		if(!service.saveDonationInfo(donationInfo,c_sys_id)) {
			
			log.error("============== [ERROR] BOARD REGIST FAIL =============");
			
			return "redierct:/charityOrg/regist";
		}
		
		//file 저장
		if(fileService.saveCharityFile(file, c_sys_id)) {
			log.info("==== FILE UPLOAD : {} ===", file.getOriginalFilename());
		}
		
		log.info("============= BOARD REGIST SUCCESS =============");
		
		return "redirect:/charityOrg/home";
	}
	
	/*
	 * 수정 사실 information/{}이랑 같은데 흐음 어떻게 못하나...
	 */
	@GetMapping("/modify/{c_board_num}")
	public String modify(@PathVariable("c_board_num") int c_board_num, 
							Model model, 
							HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		CharityOrgDTO userInfo = (CharityOrgDTO)session.getAttribute(SessionConst.CHARITY_ORG_USER);
		String c_sys_id = userInfo.getC_sys_id();
		
		Long donationTimes = service.getDonationTimes(c_board_num);
		Long donationCash = service.getDonationAmountCash(c_board_num);
		Long donationPoint= service.getDonationAmountPoint(c_board_num);
		
		DonationInfoDTO donationInfo = service.readDonationBoardInfo(c_board_num);
		CharityOrgDTO cOInfo = service.getCharityOrgInfo(c_sys_id);
		
		
		model.addAttribute("donationInfo", donationInfo);
		model.addAttribute("cOInfo", cOInfo);
		model.addAttribute("donationTimes", donationTimes);
		model.addAttribute("donationCash", donationCash);
		model.addAttribute("donationPoint", donationPoint);
		model.addAttribute("c_board_num", c_board_num);
		
		
		log.info("========== READ VOLUNTEER INFO ========");
		log.info("======== BOARD NUM : "+ c_board_num + " =========");
		log.info("======== BOARD INFO : "+ donationInfo.toString() + " =========");
		log.info("======== VOLUNTEER ORG INFO : "+ cOInfo.toString() + " =========");
		
		return "charityOrg/charity-org-modify";
	}
	
	@PostMapping("/modify")
	public String modifyComlpete(@ModelAttribute DonationInfoDTO donationInfo,
							@RequestParam MultipartFile file,
							HttpServletRequest request) throws IllegalStateException, IOException {
		
		HttpSession session = request.getSession(false);
		CharityOrgDTO userInfo = (CharityOrgDTO)session.getAttribute(SessionConst.CHARITY_ORG_USER);
		String c_sys_id = userInfo.getC_sys_id();
		String org_name = userInfo.getOrg_name();
		donationInfo.setOrg_name(org_name);
		
		// 실패시
		if(!service.updateDonationInfo(donationInfo, c_sys_id)) {
			
			log.error("============== [ERROR] BOARD REGIST FAIL =============");
			
			return "redierct:/charityOrg/modify"+donationInfo.getC_board_num();
		}
		
		//file 저장
		if(fileService.saveCharityFile(file, c_sys_id)) {
			log.info("==== FILE UPLOAD : {} ===", file.getOriginalFilename());
		}
		
		log.info("============= BOARD REGIST SUCCESS =============");
		
		return "redirect:/charityOrg/list";
	}	
	
	/*
	 * 삭제
	 */
	@PostMapping(value = "/delete",
				produces = {MediaType.APPLICATION_JSON_UTF8_VALUE,
				MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public ResponseEntity<String> delete(@RequestBody DeleteBoardDTO board_num) {
		
		if(service.deleteDonationInfo(board_num.getBoard_num())) {
			log.info("=========== DELETE SUCCESS ============");
			log.info("---------------" + board_num.getBoard_num());
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		log.info("---------------" + board_num.getBoard_num());
		log.info("=========== DELETE FAIL ============");
		
		return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		
		
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
	public ResponseEntity<List<DonationInfoDTO>> search(@RequestBody OrgSearchFormDTO searchForm, HttpServletRequest request){
		
		
		HttpSession session = request.getSession(false);
		CharityOrgDTO userInfo = (CharityOrgDTO)session.getAttribute(SessionConst.CHARITY_ORG_USER);
		String c_sys_id = userInfo.getC_sys_id();
		searchForm.setSys_id(c_sys_id);
		
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
	
}



