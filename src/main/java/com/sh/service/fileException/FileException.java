package com.sh.service.fileException;

public class FileException extends RuntimeException{

	public FileException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public FileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 이제 file service, controller 예외 처리하기 
	 */
}
