package com.sh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sh.domain.CharityFileDTO;
import com.sh.domain.VolunteerFileDTO;
import com.sh.webDomain.FileSaveDTO;

@Mapper
@Repository
public interface FileMapper {
	
	/*
	 * 봉사공고 첨부파일 저장
	 */
	int saveVolunteerFile(FileSaveDTO saveDTO);
	
	/*
	 * 봉사공고 첨부파일 가져오기
	 */
	VolunteerFileDTO getVolunteerFile(int board_num);
	
	/*
	 * 기부공고 첨부파일 저장
	 */
	int saveCharityFile(FileSaveDTO saveDTO);
						
	
	/*
	 * 기부공고 첨부파일 가져오기
	 */
	CharityFileDTO getCharityFile(int board_num);
	
}
