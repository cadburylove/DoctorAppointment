package com.Doctor.DoctorAppointment.DaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.Doctor.DoctorAppointment.Controller.DoctorAppointmentController;
import com.Doctor.DoctorAppointment.Dao.DoctorAppointmentDao;
import com.Doctor.DoctorAppointment.Model.DoctorModel;
import com.Doctor.DoctorAppointment.Model.DoctorTimeAvailabilityModel;
import com.Doctor.DoctorAppointment.Model.MasterResponseModel;
import com.Doctor.DoctorAppointment.common.QueryMaster;

@Repository
public class DoctorAppointmentDaoImpl implements DoctorAppointmentDao {

	private static final Logger log = LoggerFactory.getLogger(DoctorAppointmentController.class);

	@Autowired
	DataSource ds;

	@Override
	public List<DoctorModel> getDoctorList() {
		log.info("Entering Into DoctorAppointmentDaoImpl -->getDoctorList()");
		Connection con = null;
		ResultSet rs = null;
		List<DoctorModel> docList = new ArrayList<DoctorModel>();
		StringBuilder masterDataQuery = new StringBuilder();
		QueryMaster queryMaster = new QueryMaster();
		try {
			con = ds.getConnection();
			masterDataQuery.append("SELECT * FROM DOCTOR ");
			rs = queryMaster.select(masterDataQuery.toString(), null, con);
			log.info("DoctorAppointmentDaoImpl -->Query :: " + masterDataQuery.toString());
			while (rs.next()) {
				DoctorModel bean = new DoctorModel();
				bean.setDoctor_id(rs.getInt("doctor_id"));
				bean.setDoctor_name(rs.getString("doctor_name"));
				bean.setDoctor_address(rs.getString("address"));
				docList.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("Exiting DoctorAppointmentDaoImpl -->getDoctorList() || DocList Size :: " + docList.size());
		return docList;
	}

	@Override
	public List<DoctorTimeAvailabilityModel> getDrTimeAvailabilityList() {
		log.info("Entering Into DoctorAppointmentDaoImpl -->getDrTimeAvailabilityList()");
		Connection con = null;
		ResultSet rs = null;
		List<DoctorTimeAvailabilityModel> docList = new ArrayList<DoctorTimeAvailabilityModel>();
		StringBuilder masterDataQuery = new StringBuilder();
		QueryMaster queryMaster = new QueryMaster();
		try {
			con = ds.getConnection();
			masterDataQuery.append("SELECT * FROM TIME_AVAILABILITY ");
			rs = queryMaster.select(masterDataQuery.toString(), null, con);
			log.info("DoctorAppointmentDaoImpl -->Query :: " + masterDataQuery.toString());
			while (rs.next()) {
				DoctorTimeAvailabilityModel bean = new DoctorTimeAvailabilityModel();
				bean.setDoctor_id(rs.getInt("doctor_id"));
				bean.setTime_id(rs.getInt("time_Id"));
				bean.setDays(rs.getString("days"));
				bean.setOpen_status(rs.getBoolean("open_status"));
				bean.setStart_time(rs.getTime("start_time"));
				bean.setEnd_time(rs.getTime("end_time"));
				docList.add(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info(
				"Exiting DoctorAppointmentDaoImpl -->getDrTimeAvailabilityList() || DocList Size :: " + docList.size());
		return docList;
	}

	@Override
	public int saveDrTimeAvailability(List<DoctorTimeAvailabilityModel> availabilityModels) {
		log.info("Entering Into DoctorAppointmentDaoImpl -->saveDrTimeAvailability()");
		String sql = "INSERT INTO TIME_AVAILABILITY (DOCTOR_ID, DAYS, OPEN_STATUS, START_TIME, END_TIME) VALUES (?, ?, ?, ?, ?)";
		int[] resultCounts = null;

		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			for (DoctorTimeAvailabilityModel model : availabilityModels) {
				ps.setInt(1, model.getDoctor_id());
				ps.setString(2, model.getDays());
				ps.setBoolean(3, model.isOpen_status());
				ps.setTime(4, model.getStart_time());
				ps.setTime(5, model.getEnd_time());
				ps.addBatch();
			}
			resultCounts = ps.executeBatch();
			log.info("DoctorAppointmentDaoImpl -->Query :: " + sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Exiting DoctorAppointmentDaoImpl -->saveDrTimeAvailability() ");
		return Arrays.stream(resultCounts).sum();
	}

	@Override
	public List<MasterResponseModel> getListOnFilters(List<MasterResponseModel> model) {
		log.info("Entering Into DoctorAppointmentDaoImpl -->getListOnFilters()");
		ArrayList<Object> param = new ArrayList<Object>();
		Connection conn = null;
		ResultSet rs = null;
		List<MasterResponseModel> list = new ArrayList<MasterResponseModel>();
		StringBuilder masterDataQuery = new StringBuilder();
		QueryMaster queryMaster = new QueryMaster();
		try {
			conn = ds.getConnection();
			masterDataQuery.append(" SELECT DR.DOCTOR_ID,TIME_ID, DAYS, OPEN_STATUS, START_TIME, END_TIME, "
					+ " DOCTOR_NAME,ADDRESS FROM DOCTOR DR "
					+ " JOIN TIME_AVAILABILITY TM ON TM.DOCTOR_ID = DR.DOCTOR_ID WHERE 1=1 ");

			for (MasterResponseModel filter : model) {
				if (filter.getDays() != null && !filter.getDays().isEmpty()) {
					masterDataQuery.append("AND TM.DAYS = ? ");
					param.add(filter.getDays());
				}
				if (filter.getStart_time() != null) {
					masterDataQuery.append("AND TM.START_TIME >= ? ");
					param.add(filter.getStart_time());
				}
				if (filter.getEnd_time() != null) {
					masterDataQuery.append("AND TM.END_TIME <= ? ");
					param.add(filter.getEnd_time());
				}
				if (filter.getDoctor_name() != null && !filter.getDoctor_name().isEmpty()) {
					masterDataQuery.append("AND DR.DOCTOR_NAME LIKE ? ");
					param.add("%" + filter.getDoctor_name() + "%");
				}
			}
			rs = queryMaster.select(masterDataQuery.toString(), param, conn);
			log.info("DoctorAppointmentDaoImpl -->Query :: " + masterDataQuery.toString());
			while (rs.next()) {
				MasterResponseModel bean = new MasterResponseModel();
				bean.setDoctor_id(rs.getInt("doctor_id"));
				bean.setDoctor_name(rs.getString("doctor_name"));
				bean.setDoctor_address(rs.getString("address"));
				bean.setTime_id(rs.getInt("time_Id"));
				bean.setDays(rs.getString("days"));
				bean.setOpen_status(rs.getBoolean("open_status"));
				bean.setStart_time(rs.getTime("start_time"));
				bean.setEnd_time(rs.getTime("end_time"));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("Exiting DoctorAppointmentDaoImpl -->getListOnFilters() || List Size :: " + list.size());
		return list;
	}

	@Override
	public int updateDoctorDetails(List<DoctorTimeAvailabilityModel> availabilityModels) {
		log.info("Entering Into DoctorAppointmentDaoImpl -->updateDoctorDetails()");
		QueryMaster qm = new QueryMaster();
		int totalUpdated = 0;
		Connection conn = null;
		try {
			conn = ds.getConnection();

			for (DoctorTimeAvailabilityModel availabilityModel : availabilityModels) {
				StringBuilder query = new StringBuilder();
				ArrayList<Object> params = new ArrayList<>();

				query.append("UPDATE TIME_AVAILABILITY SET ");

				if (availabilityModel.isOpen_status()) {
					query.append(" START_TIME = ?, END_TIME = ?");
					params.add(availabilityModel.getStart_time());
					params.add(availabilityModel.getEnd_time());
				} else {
					query.append(" OPEN_STATUS = ?, START_TIME = ?, END_TIME = ?");
					params.add(availabilityModel.isOpen_status());
					params.add(Time.valueOf("00:00:00"));
					params.add(Time.valueOf("00:00:00"));
				}

				query.append(" WHERE DAYS = ? AND DOCTOR_ID = ?");
				params.add(availabilityModel.getDays());
				params.add(availabilityModel.getDoctor_id());

				int updatedRows = qm.updateInsert(query.toString(), params, conn);
				log.info("DoctorAppointmentDaoImpl -->Query :: " + query.toString());
				totalUpdated += updatedRows;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("Exiting DoctorAppointmentDaoImpl -->updateDoctorDetails()");
		return totalUpdated;
	}

	@Override
	public int deleteDoctorAvailability(List<DoctorTimeAvailabilityModel> availabilityModels) {
		log.info("Entering Into DoctorAppointmentDaoImpl -->deleteDoctorAvailability()");
		StringBuilder query = new StringBuilder();
		QueryMaster qm = new QueryMaster();
		int res = 0;
		Connection conn = null;
		ArrayList<Object> params = new ArrayList<>();
		try {
			conn = ds.getConnection();
			query.append("DELETE FROM TIME_AVAILABILITY WHERE ");
			for (DoctorTimeAvailabilityModel availabilityModel : availabilityModels) {
				query.append("(DOCTOR_ID = ? AND TIME_ID = ?)");

				params.add(availabilityModel.getDoctor_id());
				params.add(availabilityModel.getTime_id());

				if (availabilityModel.getDays() != null) {
					query.append(" AND DAYS = ?");
					params.add(availabilityModel.getDays());
				}

				if (availabilityModels.indexOf(availabilityModel) < availabilityModels.size() - 1) {
					query.append(" OR ");
				}
			}
			res = qm.updateInsert(query.toString(), params, conn);
			log.info("DoctorAppointmentDaoImpl -->Query :: " + query.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("Exiting DoctorAppointmentDaoImpl -->deleteDoctorAvailability()");
		return res;
	}

}
