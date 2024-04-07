package com.Doctor.DoctorAppointment.Model;

import java.sql.Time;

import lombok.Data;

@Data
public class DoctorTimeAvailabilityModel {

	private int time_id;
	private int doctor_id;
	private String days;
	private boolean open_status;
	private Time start_time;
	private Time end_time;
	
	public int getTime_id() {
		return time_id;
	}

	public void setTime_id(int time_id) {
		this.time_id = time_id;
	}

	public int getDoctor_id() {
		return doctor_id;
	}

	public void setDoctor_id(int doctor_id) {
		this.doctor_id = doctor_id;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public boolean isOpen_status() {
		return open_status;
	}

	public void setOpen_status(boolean open_status) {
		this.open_status = open_status;
	}

	public Time getStart_time() {
		return start_time;
	}

	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}

	public Time getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}

}
