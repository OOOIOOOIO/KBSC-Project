package com.sh.webDomain;

import lombok.Data;

@Data
public class FileSaveDTO {
	private String sys_id;
	private String original_file_name;
	private String system_file_name;
	private int board_num;
	
	public FileSaveDTO(String sys_id, String original_file_name, String system_file_name) {
		this.sys_id = sys_id;
		this.original_file_name = original_file_name;
		this.system_file_name = system_file_name;
	}
	
	
}
