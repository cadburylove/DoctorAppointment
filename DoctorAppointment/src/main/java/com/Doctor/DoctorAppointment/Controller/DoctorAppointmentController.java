package com.Doctor.DoctorAppointment.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Doctor.DoctorAppointment.Model.DoctorModel;
import com.Doctor.DoctorAppointment.Model.DoctorTimeAvailabilityModel;
import com.Doctor.DoctorAppointment.Model.MasterResponseModel;
import com.Doctor.DoctorAppointment.Model.ResponseMessage;
import com.Doctor.DoctorAppointment.Service.DoctorAppointmentService;
import com.Doctor.DoctorAppointment.common.Constants;

@RestController
@RequestMapping("/doctor")
public class DoctorAppointmentController {
	
	private static final Logger log = LoggerFactory.getLogger(DoctorAppointmentController.class);
	
	@Autowired
	DoctorAppointmentService appointmentService;

	// Get the doctor List
	@RequestMapping("/getDoctorList")
	public ResponseEntity<List<DoctorModel>> getDoctorList() {
		log.info("Entering Into DoctorAppointmentController -->getDoctorList()");
		List<DoctorModel> docList = appointmentService.getDoctorList();
		log.info("Exiting DoctorAppointmentController -->getDoctorList()");
		return new ResponseEntity<>(docList, HttpStatus.OK);
	}

	// Get the time Availability list of the doctors
	@RequestMapping("/getDrTimeAvailabilityList")
	public ResponseEntity<List<DoctorTimeAvailabilityModel>> getDrTimeAvailabilityList() {
		log.info("Entering Into DoctorAppointmentController -->getDrTimeAvailabilityList()");
		List<DoctorTimeAvailabilityModel> docList = appointmentService.getDrTimeAvailabilityList();
		log.info("Exiting DoctorAppointmentController -->getDoctorList()");
		return new ResponseEntity<>(docList, HttpStatus.OK);
	}

	// save time Availability of the doctors
	@PostMapping("/saveDrTimeAvailability")
	public ResponseEntity<ResponseMessage> saveDrTimeAvailability(
			@RequestBody List<DoctorTimeAvailabilityModel> availabilityModel) {
		log.info("Entering Into DoctorAppointmentController -->saveDrTimeAvailability()");
		ResponseMessage responseMessage;
		responseMessage = appointmentService.saveDrTimeAvailability(availabilityModel);
		if (Constants.ERROR_CODES.NO_ERROR == responseMessage.getErrorCode()) {
			log.info("Exiting DoctorAppointmentController -->saveDrTimeAvailability() || ResponseMessage :: "+responseMessage.toString());
			return ResponseEntity.ok(responseMessage);
		}
		log.info("Exiting DoctorAppointmentController -->saveDrTimeAvailability() || ResponseMessage :: "+responseMessage.toString());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseMessage);
	}

	// Search ability on basis of weekday, doctor name, time
	@PostMapping("/getListOnFilters")
	public ResponseEntity<List<MasterResponseModel>> getListOnFilters(@RequestBody List<MasterResponseModel> models) {
		log.info("Entering Into DoctorAppointmentController -->getListOnFilters()");
		List<MasterResponseModel> list = appointmentService.getListOnFilters(models);
		log.info("Exiting DoctorAppointmentController -->getDoctorList()" );
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// Get the time availability list of the doctors
	@PostMapping("/updateDoctorDetails")
	public ResponseEntity<ResponseMessage> updateDoctorDetails(@RequestBody List<DoctorTimeAvailabilityModel> model) {
		log.info("Entering Into DoctorAppointmentController -->updateDoctorDetails()");
		ResponseMessage responseMessage;
		responseMessage = appointmentService.updateDoctorDetails(model);
		if (Constants.ERROR_CODES.NO_ERROR == responseMessage.getErrorCode()) {
			log.info("Exiting DoctorAppointmentController -->updateDoctorDetails() || ResponseMessage :: "+responseMessage.toString());
			return ResponseEntity.ok(responseMessage);
		}
		log.info("Exiting DoctorAppointmentController -->updateDoctorDetails() || ResponseMessage :: "+responseMessage.toString());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseMessage);
	}

	// Delete doctor availability record
	@PostMapping("/deleteDoctorAvailability")
	public ResponseEntity<ResponseMessage> deleteDoctorAvailability(
			@RequestBody List<DoctorTimeAvailabilityModel> availabilityModel) {
		log.info("Entering Into DoctorAppointmentController -->deleteDoctorAvailability()");
		ResponseMessage responseMessage;
		responseMessage = appointmentService.deleteDoctorAvailability(availabilityModel);
		if (Constants.ERROR_CODES.NO_ERROR == responseMessage.getErrorCode()) {
			log.info("Exiting DoctorAppointmentController -->deleteDoctorAvailability() || ResponseMessage :: "+responseMessage.toString());
			return ResponseEntity.ok(responseMessage);
		}
		log.info("Exiting DoctorAppointmentController -->deleteDoctorAvailability() || ResponseMessage :: "+responseMessage.toString());
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseMessage);
	}

}
