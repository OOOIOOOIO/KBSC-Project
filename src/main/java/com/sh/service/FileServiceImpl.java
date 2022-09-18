package com.sh.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sh.domain.CharityFileDTO;
import com.sh.domain.VolunteerFileDTO;
import com.sh.mapper.FileMapper;
import com.sh.webDomain.FileSaveDTO;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileMapper mapper;
	
	@Value("${file.dir}")
	private String fileDir;
	
	// 경로 리턴
	public String getFullPath(String filename) {
		return fileDir + filename;
	}
	
	
	
	// 파일 저장
	@Override
	public boolean saveVolunteerFile(MultipartFile multipartFile, String v_sys_id) throws IllegalStateException, IOException {
		
		if(multipartFile.isEmpty()) {
			return false;
		}
		
		// original : user가 설정한 파일명
		String originalFilename = multipartFile.getOriginalFilename(); 
		
		// server에 저장할 파일명, 확장자(.~) 붙여주기
		String systemFilname = createStoreFileName(originalFilename);
		
		multipartFile.transferTo(new File(getFullPath(systemFilname)));
		
		
		
		return mapper.saveVolunteerFile(new FileSaveDTO(v_sys_id, originalFilename, systemFilname)) == 1;
		
	}
	

	@Override
	public VolunteerFileDTO getVolunteerFile(int board_num) {
		
		return mapper.getVolunteerFile(board_num);
	}



	@Override
	public boolean saveCharityFile(MultipartFile multipartFile, String c_sys_id) throws IllegalStateException, IOException {
		if(multipartFile.isEmpty()) {
			return false;
		}
		
		// original : user가 설정한 파일명
		String originalFilename = multipartFile.getOriginalFilename(); 
		
		// server에 저장할 파일명, 확장자(.~) 붙여주기
		String systemFilname = createStoreFileName(originalFilename);
		
		// 저장
		multipartFile.transferTo(new File(getFullPath(systemFilname)));
		
		return mapper.saveCharityFile(new FileSaveDTO(c_sys_id, originalFilename, systemFilname)) == 1;
		
	}



	@Override
	public CharityFileDTO getCharityFile(int board_num) {

		return mapper.getCharityFile(board_num);
	}
	
	
	/*
	 *  확장자 뽑는 메소드
	 */
	private String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf('.');
		String ext = originalFilename.substring(pos+1); // 확장자 뽑기
		
		return ext;
		
	}
	
	/*
	 *  server에 저장할 파일명 메소드
	 */
	private String createStoreFileName(String originalFilename) {
		String uuid = UUID.randomUUID().toString();
		String ext = extractExt(originalFilename);
		String systemFileName = uuid + "." + ext;
		
		return systemFileName;
	}
}
