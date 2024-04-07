package com.Doctor.DoctorAppointment.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Data
@Slf4j
public class QueryMaster {

	PreparedStatement prepstmt = null;
	StringBuffer logString = null;

	private int totalRows;
	private int startRow;
	private int endRow;
	PreparedStatement prepstmt1 = null;
	ResultSet rs = null;

	public synchronized ResultSet select(String query, List<Object> param, Connection con) {

		try {
			logString = new StringBuffer("DBUser: " + con.getMetaData().getUserName() + " | " + query)
					.append("  with values ::");
			if (endRow > 0) {
				int PageSize = (endRow - startRow) + 1;
				int PageNumber = 0;
				if (PageSize == 25) {
					PageNumber = endRow / 25;
				} else if (PageSize == 50) {
					PageNumber = endRow / 50;
				} else if (PageSize == 75) {
					PageNumber = endRow / 75;
				} else if (PageSize == 100) {
					PageNumber = endRow / 100;
				} else {
					PageNumber = endRow / 25;
				}
				String TotalRowQuery = "Select count(*) From ( " + query + " ) r";
				prepstmt1 = con.prepareStatement(TotalRowQuery);
				String paginationQuery = query + " LIMIT " + PageSize + " OFFSET ((" + (PageNumber - 1) + ")*"
						+ PageSize + ") ";
				prepstmt = con.prepareStatement(paginationQuery);
			} else {
				prepstmt = con.prepareStatement(query);
			}
			if (Objects.nonNull(param)) {
				int q = param.size();
				for (int j = 1; j <= q; j++) {
					Object temp = param.get(j - 1);
					logString.append(temp).append("|");
					if (endRow > 0) {
						prepstmt.setObject(j, temp);
						prepstmt1.setObject(j, temp);
					} else {
						prepstmt.setObject(j, temp);
					}

				}
			}

			if (endRow > 0) {
				ResultSet rs_1 = prepstmt1.executeQuery();
				if (rs_1.next()) {
					totalRows = rs_1.getInt(1);
				}

				rs = prepstmt.executeQuery();
			} else {
				rs = prepstmt.executeQuery();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (param != null)
					param.clear();
				param = null;
				query = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rs;

	}

	/**
	 * This method is used to run 'select' queries.
	 * 
	 * @param query The select query which needs to be run on the database
	 * @param param List of objects which form the parameters passed in the query
	 * @param con   Database connection object
	 * @param db    Defines which DB needs to be accessed
	 * @param log   Variable of log4j, used for logging into respective classes
	 * @return rs - ResultSet obtained after running the query
	 * @author spotlite
	 */
	public synchronized ResultSet select(String query, List<Object> param, PreparedStatement prepstmt) {

		ResultSet rs = null;
		try {
			if (Objects.nonNull(param)) {
				int q = param.size();
				for (int j = 1; j <= q; j++) {
					Object temp = param.get(j - 1);
					prepstmt.setObject(j, temp);
				}
			}

			rs = prepstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (param != null)
					param.clear();
				param = null;
				query = null;
				if (prepstmt != null) {
					prepstmt.clearParameters();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	/**
	 * @param query The select query which needs to be run on the database
	 * @param param List of objects which form the parameters passed in the query
	 * @param log   TODO
	 * @param db    Defines which DB needs to be accessed
	 * @param log   Variable of log4j, used for logging into respective classes
	 * @return updateinsertResult (int) - number of rows which got inserted/updated
	 */
	public int updateInsert(String query, ArrayList<Object> param, Connection con) {
		PreparedStatement prepstmt = null;
		int updateinsertResult = 0;
		boolean conToBeClosed = false;
		try {

			logString = new StringBuffer(" DBUser: " + con.getMetaData().getUserName());
			prepstmt = con.prepareStatement(query);

			if (param != null) {
				int q = param.size();
				for (int j = 1; j <= q; j++) {
					Object temp = param.get(j - 1);
					prepstmt.setObject(j, temp);
				}
			}
			updateinsertResult = prepstmt.executeUpdate();
			logString.setLength(0);
			logString.append("result of updateInsert ::").append(updateinsertResult);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				if (prepstmt != null) {
					prepstmt.close();
				}
				if (param != null)
					param.clear();
				param = null;
				query = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return updateinsertResult;
	}

	/**
	 * This method closes preparedStatment variable, used for clearing resource.
	 */
	public void closeStatement() {
		try {
			if (prepstmt != null)
				prepstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeRsPreparedStmt() {
		this.closeRs();
		this.rs = null;
		this.closeStatement();
		this.prepstmt = null;
	}

	private void closeRs() {
		try {
			if (this.rs != null)
				this.rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkExists(String tableName, Map<String, Object> param, Connection con, Logger log)
			throws Exception {
		boolean result = false;
		boolean conToBeClosed = false;
		ResultSet rs = null;
		StringBuilder query = new StringBuilder();
		try {

			if (Objects.nonNull(param) && Objects.nonNull(tableName)) {
				if (param.size() > 0) {
					query.append("select *  from ");
					query.append(tableName + " where 1=1 ");
					String paramVal = param.entrySet().stream().map(entry -> " AND " + entry.getKey() + "= ? ")
							.collect(Collectors.joining(""));
					query.append(paramVal);
					prepstmt = con.prepareStatement(query.toString());
					logString = new StringBuffer("DBUser: " + con.getMetaData().getUserName());
					if (Objects.nonNull(param)) {
						int i = 1;
						for (Map.Entry<String, Object> entry : param.entrySet()) {
							Object temp = entry.getValue();
							logString.append(temp).append("|");
							prepstmt.setObject(i, temp);
							i++;
						}

					}
					log.info(logString.toString());
					log.info("Query Fired " + prepstmt);
					logString.setLength(0);
					rs = prepstmt.executeQuery();
					if (Objects.nonNull(rs)) {
						if (rs.next()) {
							result = true;
						}
					}

				}
			}
		} finally {
			if (prepstmt != null) {
				prepstmt.close();
			}
			if (param != null)
				param.clear();
			param = null;
			query = null;
		}

		return result;
	}

}
