package sscex2.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Enroller {

	public static void enrolStudent(int studentID, String title, String foreName, String familyName,
			String dateOfBirth) {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			System.out.println("Attempting to register student ...");
			conn = Util.getConnection();
			stmt = conn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?, ?);");

			int titleID = getTitleID(title, conn);
			if (titleID < 0) {
				System.out.println("Title must be one of Ms, Miss, Mrs, Mr, Dr, Prof.");
				return;
			}

			stmt.setInt(1, studentID);
			stmt.setInt(2, titleID);
			stmt.setString(3, foreName);
			stmt.setString(4, familyName);
			stmt.setDate(5, Date.valueOf(dateOfBirth));

			stmt.executeUpdate();
			System.out.println("Student has been added to database.");

			stmt.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Postgresql driver not found.");
		} catch (SQLException e) {
			System.out.println("An error occurred in the process.");
			System.out.println("Review the following message and try again:");
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Date of birth must be of format yyyy-mm-dd.");
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void assignStudentToTutor(int studentID, int tutorID) {
		PreparedStatement stmt = null;
		Connection conn = null;

		try {
			System.out.println("Attempting to assign student to tutor ...");
			conn = Util.getConnection();
			stmt = conn.prepareStatement("INSERT INTO Tutor VALUES (?, ?);");
			stmt.setInt(1, studentID);
			stmt.setInt(2, tutorID);

			stmt.executeUpdate();
			System.out.println("Student has been assigned to tutor.");

			stmt.close();

		} catch (ClassNotFoundException e) {
			System.out.println("Postgresql driver not found.");
		} catch (SQLException e) {
			System.out.println("An error occurred in the process.");
			System.out.println("Review the following message and try again:");
			System.out.println(e.getMessage());
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

	private static int getTitleID(String title, Connection conn) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT titleID FROM Titles WHERE titleString = ?;");
		stmt.setString(1, title);

		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			return Integer.valueOf(rs.getString("titleID"));
		}

		return -1;
	}
}
