package com.sh.controller;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.util.UriUtils;

import com.sh.domain.UserDTO;
import com.sh.domain.VolunteerFileDTO;
import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.service.FileService;
import com.sh.service.VolunteerUserService;
import com.sh.webDomain.VolunteerApplyFormDTO;
import com.sh.webDomain.DonationPaymentInfoDTO;
import com.sh.webDomain.SessionConst;
import com.sh.webDomain.DonationSearchFormDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/volunteerUser")
@Slf4j
public class VolunteerUserController {

	private final VolunteerUserService service;
	private final FileService fileService;
	
	/*
	 * 봉사목록 및 검색 페이지
	 * 검색하고 다시 돌아오는 페이지
	 * 
	 * 검색에 따라 가져오기
	 * 처음에도 뿌려주고 싶은데 흐음
	 */
	@GetMapping("/list")
	public String list(Model model) {
		List<VolunteerInfoDTO> list = service.getVolunteerBoardList();
		
		model.addAttribute("vList", list);
		
		log.info("==========GET VOLUNTEER LIST========");
		
		return "volunteerUser/volunteer-user-list";
	}
	
	/*
	 * 봉사목록을 클릭해서 정보 보는 페이지
	 */
	@GetMapping("/information/{v_board_num}")
	public String information(@PathVariable("v_board_num") int v_board_num, Model model) {
		
		VolunteerInfoDTO vBInfo = service.readVolunteerBoardInfo(v_board_num);
		
		VolunteerOrgDTO vOInfo = service.getVolunteerOrgInfo(v_board_num);
		
		model.addAttribute("vBInfo", vBInfo);
		model.addAttribute("vOInfo", vOInfo);
		
		// file
		VolunteerFileDTO fileInfo= fileService.getVolunteerFile(v_board_num);
		if(fileInfo != null) {
			model.addAttribute("fileInfo", fileInfo);
			log.info("======== FILE INFO : "+ fileInfo.toString() + "=========");
			
		}
		
		log.info("========== READ VOLUNTEER INFO ========");
		
		return "volunteerUser/volunteer-user-information";
	}
	
	/*
	 * 검색 후 ajax로 다시 뿌려주
	 */
	@PostMapping(value ="/search",
			produces={
					MediaType.APPLICATION_JSON_UTF8_VALUE,
					MediaType.APPLICATION_XML_VALUE
			})
	@ResponseBody
	public ResponseEntity<List<VolunteerInfoDTO>> search(@RequestBody DonationSearchFormDTO searchForm){
		List<VolunteerInfoDTO> searchList = service.searchBoardList(searchForm);
		
		log.info(searchForm.toString());
		return  new ResponseEntity<List<VolunteerInfoDTO>>(searchList, HttpStatus.OK);
	}
	
	
	/*
	 * file 다운로드
	 */
	@GetMapping("/file/{v_board_num}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable("v_board_num") int v_board_num) throws MalformedURLException{
		
		VolunteerFileDTO fileInfo = fileService.getVolunteerFile(v_board_num);
		
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
	
	
	
	/*
	 * 봉사 날짜 및 신청하는 페이지 
	 * form에서 post로
	 */
	@GetMapping("/apply")
	public String apply(@RequestParam("v_start_date") String v_start_date,
						@RequestParam("v_end_date") String v_end_date,
						@RequestParam("v_board_num") int v_board_num,
						@RequestParam("r_num") int r_num,
						Model model) {
		
		
		List<LocalDate> dateList = service.availableDateList(v_start_date, v_end_date);
		
		List<Integer> avNum = service.getApplyVolunteerNum(v_board_num, dateList, r_num);
		
		LinkedHashMap<LocalDate, Integer> dateInfo = service.ListToMap(dateList, avNum);
		
		log.info(dateList.toString());
		log.info(avNum.toString());
		
		model.addAttribute("dateInfo", dateInfo);
		model.addAttribute("v_board_num", v_board_num);
		
		log.info("=============== APPLY VOLUNTEER =============");
		log.info("========== v_start_date : {}, v_end_date : {}, v_board_num : {}, r_num : {} =========", v_start_date, v_end_date, v_board_num, r_num);
		log.info("========== " + dateInfo.toString() + " ===============");
		
		return "volunteerUser/volunteer-user-apply";
	}
		
	/*
	 * 봉사 신청 후 메인 페이지
	 * redirect 후 메인으로
	 */
	@PostMapping("/complete")
	public String complete(@ModelAttribute VolunteerApplyFormDTO applyInfo, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		UserDTO sessionInfo = (UserDTO)session.getAttribute(SessionConst.COMMON_USER);
		String u_sys_id = sessionInfo.getU_sys_id();
		
		log.info("========== VOLUNTEER APPLY  ===========");
		log.info("=============== "+applyInfo.toString() + " ================");
		
		
		// 여기서 봉사 신청 테이블 db 저장
		if(service.saveApplyVolunteerInfo(applyInfo, u_sys_id)) {
			
			log.info("========== VOLUNTEER APPLY SUCCESS ===========");
			
			if(service.saveCompleteVolunteerInfo(applyInfo, u_sys_id)) {
				
				log.info("========== VOLUNTEER COMPETE SUCCESS ===========");
				
				if(service.saveCompletVolunteerPointCertificate(applyInfo, u_sys_id)) {
					
					log.info("============ SAVE POINT CERTIFICATE SUCCESS ===========");
					
					return "redirect:/volunteerUser/completeConfirm";
				}
				else {
					
					log.error("========== [ERROR] VOLUNTEER COMPLETE FAIL ===========");
				}
			}
			else {
				
				log.error("========== [ERROR] VOLUNTEER COMPLETE FAIL ===========");
				
			}
		}
		
		
		log.error("========== [ERROR] VOLUNTEER APPLY FAIL ===========");
		
		return "redirect:/volunteerUser/apply";
		
		
		
	}
	
	@GetMapping("completeConfirm")
	public String completeConfirm(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		
		UserDTO user = (UserDTO) session.getAttribute(SessionConst.COMMON_USER);
		String name_kr = user.getName_kr();
		
		model.addAttribute("name_kr", name_kr);
		
		return "volunteerUser/volunteer-user-complete";
	}
	

}

