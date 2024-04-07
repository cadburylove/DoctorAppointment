package com.Doctor.DoctorAppointment.ServiceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Doctor.DoctorAppointment.Dao.DoctorAppointmentDao;
import com.Doctor.DoctorAppointment.Model.DoctorModel;
import com.Doctor.DoctorAppointment.Model.DoctorTimeAvailabilityModel;
import com.Doctor.DoctorAppointment.Model.MasterResponseModel;
import com.Doctor.DoctorAppointment.Model.ResponseMessage;
import com.Doctor.DoctorAppointment.Service.DoctorAppointmentService;
import com.Doctor.DoctorAppointment.common.Constants;

@Service
public class DoctorAppointmentServiceImpl implements DoctorAppointmentService {
	
	private static final Logger log = LoggerFactory.getLogger(DoctorAppointmentServiceImpl.class);
	
	@Autowired
	DoctorAppointmentDao appointmentDao;

	@Override
	public List<DoctorModel> getDoctorList() {
		log.info("Entering Into DoctorAppointmentServiceImpl -->getDoctorList()");
		return appointmentDao.getDoctorList();
	}

	@Override
	public List<DoctorTimeAvailabilityModel> getDrTimeAvailabilityList() {
		log.info("Entering Into DoctorAppointmentServiceImpl -->getDrTimeAvailabilityList()");
		return appointmentDao.getDrTimeAvailabilityList();
	}

	@Override
	public ResponseMessage saveDrTimeAvailability(List<DoctorTimeAvailabilityModel> availabilityModel) {
		log.info("Entering Into DoctorAppointmentServiceImpl -->saveDrTimeAvailability()");
		ResponseMessage resMessage = new ResponseMessage();
		try {
			if (availabilityModel.isEmpty()) {
				resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
				resMessage.setMessage("No availability data provided.");
				log.info("Exiting DoctorAppointmentServiceImpl -->saveDrTimeAvailability() || ResponseMessage :: "+resMessage.toString());
				return resMessage;
			}

			for (DoctorTimeAvailabilityModel availability : availabilityModel) {
				if (availability.getStart_time().after(availability.getEnd_time())) {
					resMessage.setMessage(
							"Invalid availability: start time must be before or equal to end time for Doctor Id :: "
									+ availability.getDoctor_id() + " For Day :: " + availability.getDays());
					resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
					log.info("Exiting DoctorAppointmentServiceImpl -->saveDrTimeAvailability() || ResponseMessage :: "+resMessage.toString());
					return resMessage;
				} else if (availability.getDoctor_id() == 0) {
					resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
					resMessage.setMessage("Please Enter Doctor ID.");
					log.info("Exiting DoctorAppointmentServiceImpl -->saveDrTimeAvailability() || ResponseMessage :: "+resMessage.toString());
					return resMessage;
				}
			}

			int res = appointmentDao.saveDrTimeAvailability(availabilityModel);
			if (res == 0) {
				resMessage.setMessage("Failed to insert the Doctor Availability Details.");
				resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
			} else {
				resMessage.setMessage("Record inserted Successfully.");
				resMessage.setErrorCode(Constants.ERROR_CODES.NO_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception :: "+e.getMessage());
		}
		log.info("Exiting DoctorAppointmentServiceImpl -->saveDrTimeAvailability() || ResponseMessage :: "+resMessage.toString());
		return resMessage;
	}

	@Override
	public List<MasterResponseModel> getListOnFilters(List<MasterResponseModel> model) {
		log.info("Entering Into DoctorAppointmentServiceImpl -->getListOnFilters()");
		return appointmentDao.getListOnFilters(model);
	}

	@Override
	public ResponseMessage updateDoctorDetails(List<DoctorTimeAvailabilityModel> model) {
		log.info("Entering Into DoctorAppointmentServiceImpl -->updateDoctorDetails()");
		ResponseMessage resMessage = new ResponseMessage();
		for (DoctorTimeAvailabilityModel availability : model) {
			if (availability.getStart_time().after(availability.getEnd_time())) {
				resMessage.setMessage(
						"Invalid availability: start time must be before or equal to end time for Doctor Id :: "
								+ availability.getDoctor_id() + " For Day :: " + availability.getDays());
				resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
				log.info("Exiting DoctorAppointmentServiceImpl -->updateDoctorDetails() || ResponseMessage :: "+resMessage.toString());
				return resMessage;
			} else if (availability.getDoctor_id() == 0) {
				resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
				resMessage.setMessage("Please Enter Doctor ID.");
				log.info("Exiting DoctorAppointmentServiceImpl -->updateDoctorDetails() || ResponseMessage :: "+resMessage.toString());
				return resMessage;
			}
		}
		int res = appointmentDao.updateDoctorDetails(model);
		if (res == 0) {
			resMessage.setMessage("Failed to Update the Doctor Availability Details.");
			resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
		} else {
			resMessage.setMessage("Record updated Successfully.");
			resMessage.setErrorCode(Constants.ERROR_CODES.NO_ERROR);
		}
		log.info("Exiting DoctorAppointmentServiceImpl -->updateDoctorDetails() || ResponseMessage :: "+resMessage.toString());
		return resMessage;
	}

	@Override
	public ResponseMessage deleteDoctorAvailability(List<DoctorTimeAvailabilityModel> model) {
		log.info("Entering Into DoctorAppointmentServiceImpl -->deleteDoctorAvailability()");
		ResponseMessage resMessage = new ResponseMessage();
		int res = appointmentDao.deleteDoctorAvailability(model);
		if (res == 0) {
			resMessage.setMessage("Failed to delete the Doctor Availability Details.");
			resMessage.setErrorCode(Constants.ERROR_CODES.INVALID);
		} else {
			resMessage.setMessage("Record deleted Successfully.");
			resMessage.setErrorCode(Constants.ERROR_CODES.NO_ERROR);
		}
		log.info("Exiting DoctorAppointmentServiceImpl -->deleteDoctorAvailability() || ResponseMessage :: "+resMessage.toString());
		return resMessage;
	}

}
