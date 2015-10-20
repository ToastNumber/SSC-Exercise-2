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
}
