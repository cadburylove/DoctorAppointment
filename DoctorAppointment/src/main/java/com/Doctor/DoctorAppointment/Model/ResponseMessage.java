package com.Doctor.DoctorAppointment.Model;

import lombok.Data;

@Data
public class ResponseMessage {

	public static final long NO_ERROR = 500000L;
	private String message;
	private long errorCode;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}
	public static long getNoError() {
		return NO_ERROR;
	}
	
	
}
