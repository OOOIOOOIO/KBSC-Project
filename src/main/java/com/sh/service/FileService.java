package com.sh.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.sh.domain.CharityFileDTO;
import com.sh.domain.VolunteerFileDTO;

public interface FileService {
	/*
	 * 파일 경로
	 */
	String getFullPath(String fileName);
	
	/*
	 * 봉사단체 파일 저장
	 */
	boolean saveVolunteerFile(MultipartFile multipartFile, String v_sys_id) throws IllegalStateException, IOException;
	
	/*
	 * 봉사단체 파일 가져오기
	 */
	VolunteerFileDTO getVolunteerFile(int boardNum);

	/*
	 * 기부단체 파일 저장
	 */
	boolean saveCharityFile(MultipartFile multipartFile, String c_sys_id) throws IllegalStateException, IOException;
	
	/*
	 * 기부단체 파일 가져오기
	 */
	CharityFileDTO getCharityFile(int boardNum);
	
}
