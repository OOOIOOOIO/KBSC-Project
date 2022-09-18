package com.sh.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgApplyDTO;
import com.sh.domain.VolunteerOrgCompleteDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.domain.VolunteerPointCertificateOrgDTO;
import com.sh.domain.VolunteerPointCertificateUserDTO;
import com.sh.domain.VolunteerUserApplyDTO;
import com.sh.domain.VolunteerUserCompleteDTO;
import com.sh.mapper.VolunteerUserMapper;
import com.sh.webDomain.VolunteerApplyFormDTO;
import com.sh.webDomain.SessionConst;
import com.sh.webDomain.VolunteerPoint;
import com.sh.webDomain.DonationSearchFormDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VolunteerUserServiceImpl implements VolunteerUserService {

	
	private final VolunteerUserMapper mapper;
	
	@Override
	public List<VolunteerInfoDTO> getVolunteerBoardList() {
		List<VolunteerInfoDTO> list = mapper.getVolunteerBoardList();
		
		return list;
	}

	@Override
	public VolunteerInfoDTO readVolunteerBoardInfo(int v_board_num) {
		VolunteerInfoDTO vBInfo = mapper.readVolunteerBoardInfo(v_board_num);
		
		return vBInfo;
	}

	@Override
	public VolunteerOrgDTO getVolunteerOrgInfo(int v_board_num) {
		
		VolunteerOrgDTO vOInfo = mapper.getVolunteerOrgInfo(v_board_num);
		
		return vOInfo;
	}
	
	@Override
	public List<Integer> getApplyVolunteerNum(int v_board_num, List<LocalDate> dateList, int r_num) {

		List<Integer> avNum = new ArrayList<>();
		
		for(int i = 0; i < dateList.size(); i++) {
			
			int applyNum = mapper.getApplyVolunteerNum(v_board_num, dateList.get(i));
			
			if(r_num > applyNum) {
				avNum.add(r_num - applyNum);
				
			}
			else {
				avNum.add(0);
			}
			
		}
		
		return avNum;
	} 

	@Override
	public List<LocalDate> availableDateList(String v_start_date, String v_end_date) {
		
		LocalDate start = LocalDate.parse(v_start_date);
		LocalDate end = LocalDate.parse(v_end_date).plusDays(1);
		
		List<LocalDate> dateList =  start.datesUntil(end).collect(Collectors.toList());
				
		
		return dateList;
	}

	@Override
	public LinkedHashMap<LocalDate, Integer> ListToMap(List<LocalDate> dateList, List<Integer> avNum) {
		
		LinkedHashMap<LocalDate, Integer> dateInfo = new LinkedHashMap<>();
		
		for(int i = 0; i < dateList.size(); i++) {
			dateInfo.put(dateList.get(i), avNum.get(i));
		}
		
		return dateInfo;
	}

	@Override
	public boolean saveApplyVolunteerInfo(VolunteerApplyFormDTO applyInfo, String u_sys_id) {
		
		int v_board_num = applyInfo.getV_board_num();
		List<String> dateList = applyInfo.getV_date();
		
		VolunteerUserApplyDTO volunteerUserApplyInfo = mapper.getVolunteerBoardApplyInfoUser(v_board_num);
		VolunteerOrgApplyDTO volunteerOrgApplyInfo = mapper.getVolunteerBoardApplyInfoOrg(v_board_num);

		volunteerUserApplyInfo.setV_board_num(v_board_num);
		volunteerUserApplyInfo.setU_sys_id(u_sys_id);
		
		volunteerOrgApplyInfo.setV_board_num(v_board_num);
		volunteerOrgApplyInfo.setU_sys_id(u_sys_id);
		
		for(String date : dateList) {
			volunteerUserApplyInfo.setV_date(date);
			volunteerOrgApplyInfo.setV_date(date);
			
			int resultUser = mapper.saveApplyedVolunteerInfoUser(volunteerUserApplyInfo);
			int resultOrg = mapper.saveApplyedVolunteerInfoOrg(volunteerOrgApplyInfo);
			
			if(resultUser != resultOrg) {
				return false;
			}
		}
		
		return true;
		
	}

	@Override
	public boolean saveCompleteVolunteerInfo(VolunteerApplyFormDTO applyInfo, String u_sys_id) {
		int v_board_num = applyInfo.getV_board_num();
		
		List<VolunteerUserCompleteDTO> volunteerCompleteInfoUser = mapper.getVolunteerBoardCompleteInfoUser(v_board_num, u_sys_id);
		List<VolunteerOrgCompleteDTO> volunteerCompleteInfoOrg = mapper.getVolunteerBoardCompleteInfoOrg(v_board_num, u_sys_id);
		
		for(int i = 0; i < volunteerCompleteInfoOrg.size(); i++) {
			VolunteerUserCompleteDTO user = volunteerCompleteInfoUser.get(i); 
			VolunteerOrgCompleteDTO org = volunteerCompleteInfoOrg.get(i);
			
			int resultUser = mapper.saveCompleteVolunteerInfoUser(user);
			int resultOrg = mapper.saveCompleteVolunteerInfoOrg(org);
			
			if(resultUser != resultOrg) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean saveCompletVolunteerPointCertificate(VolunteerApplyFormDTO pcInfo, String u_sys_id) {
		int v_board_num = pcInfo.getV_board_num();
		String certificateCode = UUID.randomUUID().toString();
		int v_time = mapper.getVolunteerTime(v_board_num);

		List<VolunteerPointCertificateUserDTO> users = mapper.getVolunteerCompleteUserInfo(v_board_num, u_sys_id);
		List<VolunteerPointCertificateOrgDTO> orgs = mapper.getVolunteerCompleteOrgInfo(v_board_num, u_sys_id);
		
		for(int i = 0; i < users.size(); i++) {
			
			VolunteerPointCertificateUserDTO user = users.get(i);
			VolunteerPointCertificateOrgDTO org = orgs.get(i);
			
			user.setCertificate_code(certificateCode);
			user.setU_sys_id(u_sys_id);
			org.setCertificate_code(certificateCode);
			org.setU_sys_id(u_sys_id);
			
			if(v_time > VolunteerPoint.ZERO && v_time <= VolunteerPoint.UNDER_3_HOUR) {
				user.setV_point(VolunteerPoint.UNDER_3_HOUR);
				org.setV_point(VolunteerPoint.UNDER_3_HOUR);
			}
			else if(v_time > VolunteerPoint.UNDER_3_HOUR && v_time <= VolunteerPoint.UNDER_5_HOUR) {
				user.setV_point(VolunteerPoint.UNDER_5_HOUR);
				org.setV_point(VolunteerPoint.UNDER_5_HOUR);
			}
			else {
				user.setV_point(VolunteerPoint.OVER_5_HOUR);
				org.setV_point(VolunteerPoint.OVER_5_HOUR);
			}
			
			int resultUser = mapper.saveCompleteVolunteerPointCertificateUser(user);
			int resultOrg = mapper.saveCompleteVolunteerPointCertificateOrg(org);
			
			if(resultUser != resultOrg) {
				return false;
			}
					
					
		}
		return true;
	}

	
	@Override
	public List<VolunteerInfoDTO> searchBoardList(DonationSearchFormDTO searchInfo) {
		
		List<VolunteerInfoDTO> searchList = mapper.searchBoardList(searchInfo);
		return searchList;
	}
	


	


}
