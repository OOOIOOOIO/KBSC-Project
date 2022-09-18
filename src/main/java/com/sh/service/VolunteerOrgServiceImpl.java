package com.sh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sh.domain.VolunteerInfoDTO;
import com.sh.domain.VolunteerOrgDTO;
import com.sh.mapper.VolunteerOrgMapper;
import com.sh.webDomain.OrgSearchFormDTO;
import com.sh.webDomain.DonationSearchFormDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VolunteerOrgServiceImpl implements VolunteerOrgService {

	private final VolunteerOrgMapper mapper;
	
	@Override
	public VolunteerOrgDTO getVolunteerOrgInfo(String v_sys_id) {
		
		return mapper.getVolunteerOrgInfo(v_sys_id);
	}

	@Override
	public boolean saveVolunteerInfo(String v_sys_id, VolunteerInfoDTO volunteerInfo) {
		
		volunteerInfo.setV_sys_id(v_sys_id);
		
		return 1 == mapper.saveVolunteerInfo(volunteerInfo);
		
	}

	@Override
	public List<VolunteerInfoDTO> getVolunteerBoardList() {
		return mapper.getVolunteerBoardList();
	}

	@Override
	public VolunteerInfoDTO readVolunteerBoardInfo(int v_board_num) {
		VolunteerInfoDTO vBInfo = mapper.readVolunteerBoardInfo(v_board_num);
		
		return vBInfo;
	}
	
	@Override
	public List<VolunteerInfoDTO> searchBoardList(OrgSearchFormDTO searchInfo) {
		
		List<VolunteerInfoDTO> searchList = mapper.searchBoardList(searchInfo);
		return searchList;
	}

}
