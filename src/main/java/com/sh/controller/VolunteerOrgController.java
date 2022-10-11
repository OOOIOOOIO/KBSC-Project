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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.sh.domain.CharityFileDTO;
import com.sh.domain.VolunteerFileDTO;
import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.service.FileService;
import com.sh.service.HomeService;
import com.sh.service.VolunteerOrgService;
import com.sh.webDomain.OrgSearchFormDTO;
import com.sh.webDomain.SessionConst;
import com.sh.webDomain.DeleteBoardDTO;
import com.sh.webDomain.DonationSearchFormDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/volunteerOrg")
@Slf4j
public class VolunteerOrgController {
	
	private final VolunteerOrgService service;
	private final HomeService homeService;
	private final FileService fileService;
	
	/*
	 * 봉사단체 홈
	 */
	@GetMapping("/home")
	public String home(HttpServletRequest request, Model model) {
		
		log.info("========= VOLUNTEER HOME =========");
		
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
		
		return "home/voOrgHome";
	}
	
	/*
	 * 올린 봉사 리스트 보기
	 */
	@GetMapping("/list")
	public String list(Model model) {
		
		log.info("=========== BOARD LIST PAGE ===========");
		
		List<VolunteerInfoDTO> list = service.getVolunteerBoardList();
		
		model.addAttribute("vList", list);
		
		
		return "volunteerOrg/volunteer-org-list";
	}
	
	
	/*
	 * 봉사목록을 클릭해서 정보 보는 페이지
	 * 여기에 수정 삭제가 있어야지?
	 * 수정 /modify/{board_num}
	 * 수정완료 /{board_num}
	 * 삭제 /{board_num}
	 */
	@GetMapping("/information/{v_board_num}")
	public String information(@PathVariable("v_board_num") int v_board_num,
							Model model,
							HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		VolunteerOrgDTO userInfo = (VolunteerOrgDTO)session.getAttribute(SessionConst.VOLUNTEER_ORG_USER);
		String v_sys_id = userInfo.getV_sys_id();
		
		// 봉사단체 정보 가져오기
		VolunteerInfoDTO vBInfo = service.readVolunteerBoardInfo(v_board_num);
		VolunteerOrgDTO vOInfo = service.getVolunteerOrgInfo(v_sys_id);
		
		
		model.addAttribute("vBInfo", vBInfo);
		model.addAttribute("vOInfo", vOInfo);
		model.addAttribute("v_board_num", v_board_num);
		
		// file
		VolunteerFileDTO fileInfo= fileService.getVolunteerFile(v_board_num);
		if(fileInfo != null) {
			model.addAttribute("fileInfo", fileInfo);
			log.info("======== FILE INFO : "+ fileInfo.toString() + "=========");
			
		}
		
		log.info("========== READ VOLUNTEER INFO ========");
		
		return "volunteerOrg/volunteer-org-information";
	}
	

	/*
	 * 봉사 공고 등록
	 */
	@GetMapping("/regist")
	public String regist(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		
		VolunteerOrgDTO userInfo = (VolunteerOrgDTO)session.getAttribute(SessionConst.VOLUNTEER_ORG_USER);
		String v_sys_id = userInfo.getV_sys_id();
		
		// 봉사단체 정보 가져오기
		VolunteerOrgDTO vOInfo = service.getVolunteerOrgInfo(v_sys_id);
		
		model.addAttribute("vOInfo", vOInfo);
		
		log.info("=========== REGIST PAGE ===========");
		
		return "volunteerOrg/volunteer-org-regist";
	}
	
	/*
	 * 봉사 공고 등록
	 * getter로 modelAttribute에 넣고
	 * RequestParam으로 파라미터 가져오기
	 */
	@PostMapping("/regist")
	public String registInfo(@ModelAttribute VolunteerInfoDTO volunteerInfo,
							@RequestParam MultipartFile file, 
							HttpServletRequest request) throws IllegalStateException, IOException {
		
		HttpSession session = request.getSession(false);
		VolunteerOrgDTO userInfo = (VolunteerOrgDTO)session.getAttribute(SessionConst.VOLUNTEER_ORG_USER);
		String v_sys_id = userInfo.getV_sys_id();
		String org_name = userInfo.getOrg_name();
		volunteerInfo.setOrg_name(org_name);
		
		//게시물
		if(!service.saveVolunteerInfo(v_sys_id, volunteerInfo)) {
			
			log.error("============== [ERROR] BOARD REGIST FAIL =============");
			
			return "redirect:/volunteerOrg/regist";
		}
		
		//file 저장
		if(fileService.saveVolunteerFile(file, v_sys_id)) {
			log.info("==== FILE UPLOAD : {} ===", file.getOriginalFilename());
		}
		
		log.info("============= BOARD REGIST SUCCESS =============");
		
		return "redirect:/volunteerOrg/home";
	}
	
	/*
	 * 수정 사실 information/{}이랑 같은데 흐음 어떻게 못하나...
	 */
	@GetMapping("/modify/{v_board_num}")
	public String modify(@PathVariable("v_board_num") int v_board_num,
						Model model,
						HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		VolunteerOrgDTO userInfo = (VolunteerOrgDTO)session.getAttribute(SessionConst.VOLUNTEER_ORG_USER);
		String v_sys_id = userInfo.getV_sys_id();
		
		// 봉사단체 정보 가져오기
		VolunteerInfoDTO vBInfo = service.readVolunteerBoardInfo(v_board_num);
		VolunteerOrgDTO vOInfo = service.getVolunteerOrgInfo(v_sys_id);
		
		
		model.addAttribute("vBInfo", vBInfo);
		model.addAttribute("vOInfo", vOInfo);
		model.addAttribute("v_board_num", v_board_num);
		
		log.info("------"+ v_board_num);
		log.info("========== MODIFY VOLUNTEER INFO ========");
		
		return "volunteerOrg/volunteer-org-modify";
	}
	
	/*
	 * 수정 완료
	 */
	@PostMapping("/modify")
	public String modifyComplete(@ModelAttribute VolunteerInfoDTO volunteerInfo,
							@RequestParam MultipartFile file,
							HttpServletRequest request) throws IllegalStateException, IOException  {
		
		HttpSession session = request.getSession(false);
		VolunteerOrgDTO userInfo = (VolunteerOrgDTO)session.getAttribute(SessionConst.VOLUNTEER_ORG_USER);
		String v_sys_id = userInfo.getV_sys_id();
		String org_name = userInfo.getOrg_name();
		volunteerInfo.setOrg_name(org_name);
		volunteerInfo.setV_sys_id(v_sys_id);
		
		// 실패시
		if(!service.updateVolunteerInfo(volunteerInfo)) {
			log.info("======== UPDATE VOLUNTEER BOARD FAIL ========");
			return "redirect:/volunteer/modify/"+volunteerInfo.getV_board_num();
		}
		
		//file 저장
		if(fileService.saveVolunteerFile(file, v_sys_id)) {
			log.info("==== FILE UPLOAD : {} ===", file.getOriginalFilename());
		}
		
		log.info("============= BOARD UPDATE SUCCESS =============");
		
		return "redirect:/volunteerOrg/list";
	}
	
	
	/*
	 * 삭제
	 */
	@PostMapping(value = "/delete",
				produces = {MediaType.APPLICATION_JSON_UTF8_VALUE,
				MediaType.TEXT_PLAIN_VALUE})
	@ResponseBody
	public ResponseEntity<String> delete(@RequestBody DeleteBoardDTO board_num) {
		
		if(service.deleteVolunteerInfo(board_num.getBoard_num())) {
			log.info("=========== DELETE SUCCESS ============");
			log.info("---------------" + board_num.getBoard_num());
			
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		log.info("---------------" + board_num.getBoard_num());
		log.info("=========== DELETE FAIL ============");
		
		return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		
		
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
		String contentDisposition = "attachment; filSename=\"" + encodedOriginalFileName + "\"";
		
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(urlResource);
		
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
	public ResponseEntity<List<VolunteerInfoDTO>> search(@RequestBody OrgSearchFormDTO searchForm, HttpServletRequest request){
		HttpSession session = request.getSession(false);
		VolunteerOrgDTO userInfo = (VolunteerOrgDTO)session.getAttribute(SessionConst.VOLUNTEER_ORG_USER);
		String v_sys_id = userInfo.getV_sys_id();
		searchForm.setSys_id(v_sys_id);
		
		List<VolunteerInfoDTO> searchList = service.searchBoardList(searchForm);
		
		log.info(searchForm.toString());
		return  new ResponseEntity<List<VolunteerInfoDTO>>(searchList, HttpStatus.OK);
	}	
	
}
