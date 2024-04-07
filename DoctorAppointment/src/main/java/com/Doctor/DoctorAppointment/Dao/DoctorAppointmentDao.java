package com.Doctor.DoctorAppointment.Dao;

import java.util.List;

import com.Doctor.DoctorAppointment.Model.DoctorModel;
import com.Doctor.DoctorAppointment.Model.DoctorTimeAvailabilityModel;
import com.Doctor.DoctorAppointment.Model.MasterResponseModel;

public interface DoctorAppointmentDao {

	public List<DoctorModel> getDoctorList();

	public List<DoctorTimeAvailabilityModel> getDrTimeAvailabilityList();

	public int saveDrTimeAvailability(List<DoctorTimeAvailabilityModel> availabilityModel);

	public List<MasterResponseModel> getListOnFilters(List<MasterResponseModel> model);

	public int updateDoctorDetails(List<DoctorTimeAvailabilityModel> model);

	public int deleteDoctorAvailability(List<DoctorTimeAvailabilityModel> availabilityModel);

}
