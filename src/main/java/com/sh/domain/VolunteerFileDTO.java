package com.sh.domain;

import lombok.Data;

@Data
public class VolunteerFileDTO {
	private int v_board_num;
	private String original_file_name;
	private String system_file_name;
	
	public VolunteerFileDTO(String originalFileName, String systemFileName){
		this.original_file_name = originalFileName;
		this.system_file_name = systemFileName;
	}
}
