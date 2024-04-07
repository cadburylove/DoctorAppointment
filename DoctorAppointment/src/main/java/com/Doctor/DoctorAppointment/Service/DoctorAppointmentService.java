package com.Doctor.DoctorAppointment.Service;

import java.util.List;

import com.Doctor.DoctorAppointment.Model.DoctorModel;
import com.Doctor.DoctorAppointment.Model.DoctorTimeAvailabilityModel;
import com.Doctor.DoctorAppointment.Model.MasterResponseModel;
import com.Doctor.DoctorAppointment.Model.ResponseMessage;

public interface DoctorAppointmentService {

	public List<DoctorModel> getDoctorList();

	public List<DoctorTimeAvailabilityModel> getDrTimeAvailabilityList();

	public ResponseMessage saveDrTimeAvailability(List<DoctorTimeAvailabilityModel> availabilityModel);

	public List<MasterResponseModel> getListOnFilters(List<MasterResponseModel> models);

	public ResponseMessage updateDoctorDetails(List<DoctorTimeAvailabilityModel> model);

	public ResponseMessage deleteDoctorAvailability(List<DoctorTimeAvailabilityModel> model);

}
