package sscex2.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
	public static final String USER = "kjm409";
	public static final String PASS = "briswosw";
	public static final String DB = USER;
	public static final String URL = "jdbc:postgresql://dbteach2.cs.bham.ac.uk/" + DB;

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		// Load the driver
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection(URL, USER, PASS);

		return conn;
	}

	/**
	 * @param dob
	 * @return yyyy-mm-dd in the form dd/mm/yyyy
	 */
	public static String formatDOB(String dob) {
		int index = dob.indexOf("-");
		String year = dob.substring(0, index);
		dob = dob.substring(index + 1);
		
		index = dob.indexOf("-");
		String month = dob.substring(0, index);
		dob = dob.substring(index + 1);
		
		String day = dob;

		return String.format("%s/%s/%s", day, month, year);
	}
}
