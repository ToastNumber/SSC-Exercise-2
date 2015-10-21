package sscex2.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reporter {

	/**
	 * 
	 * @param studentID
	 * @param flag
	 *            If true, then return the year of study and the personal tutor
	 *            information, but not if it is false.
	 * @return
	 */
	private static String produceReportForStudent(int studentID, boolean flag) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			String result = "";

			conn = Util.getConnection();

			String title = "";
			String foreName = "";
			String familyName = "";
			String dateOfBirth = "";
			int yearOfStudy = -1;
			String regDescription = "";
			String email = "";
			String postalAddress = "";
			String emergencyContact = "";
			String personalTutor = "";

			// Get information about actual student
			stmt = conn
					.prepareStatement(""
							+ "SELECT titleString, foreName, familyName, dateOfBirth, yearOfStudy, RegistrationType.description, eMailAddress, postalAddress "
							+ "FROM Student, Titles, StudentRegistration, RegistrationType, StudentContact "
							+ "WHERE Student.studentID = ? AND StudentRegistration.studentID = ? "
							+ "AND StudentContact.studentID = ? AND Titles.titleID = Student.titleID "
							+ "AND StudentRegistration.registrationTypeID = RegistrationType.registrationTypeID;");

			stmt.setInt(1, studentID);
			stmt.setInt(2, studentID);
			stmt.setInt(3, studentID);
			rs = stmt.executeQuery();

			if (!rs.next()) {
				return "No student found with studentID = " + studentID;
			} else {
				do {
					title = rs.getString("titleString");
					foreName = rs.getString("foreName");
					familyName = rs.getString("familyName");
					dateOfBirth = "" + rs.getDate("dateOfBirth");
					yearOfStudy = rs.getInt("yearOfStudy");
					regDescription = rs.getString("description");
					email = rs.getString("eMailAddress");
					postalAddress = rs.getString("postalAddress");
				} while (rs.next());
			}

			rs.close();
			stmt.close();

			// Get next of kin contact information
			stmt = conn.prepareStatement("" + "SELECT name, eMailAddress, postalAddress "
					+ "FROM NextOfKinContact " + "WHERE studentID = ?;");
			stmt.setInt(1, studentID);

			rs = stmt.executeQuery();
			while (rs.next()) {
				emergencyContact += rs.getString("name") + ",\n";
				emergencyContact += rs.getString("eMailAddress") + ",\n";
				emergencyContact += rs.getString("postalAddress") + "";
			}
			rs.close();
			stmt.close();

			// Get personal tutor information
			stmt = conn.prepareStatement("" + "SELECT titleString, foreName, familyName, office, eMailAddress "
					+ "FROM Lecturer, LecturerContact, Titles, Tutor "
					+ "WHERE Tutor.studentID = ? AND LecturerContact.lecturerID = Tutor.lecturerID "
					+ "AND Lecturer.lecturerID = Tutor.lecturerID " + "AND Lecturer.titleID = Titles.titleID;");
			stmt.setInt(1, studentID);

			rs = stmt.executeQuery();
			while (rs.next()) {
				personalTutor += rs.getString("titleString") + " ";
				personalTutor += rs.getString("foreName") + " " + rs.getString("familyName") + ", ";
				personalTutor += "Office: " + rs.getInt("office") + ", ";
				personalTutor += rs.getString("eMailAddress");
			}
			rs.close();
			stmt.close();

			result += String.format("Name: %s %s %s%n", title, foreName, familyName);
			result += String.format("DOB: %s%n", Util.formatDOB(dateOfBirth));
			result += String.format("Student ID: %d%n", studentID);

			if (flag) result += String.format("Year of Study: %d%n", yearOfStudy);

			result += String.format("Registration Type: %s%n", regDescription);
			result += String.format("Email: %s%n", email);
			result += String.format("Postal Address: %s%n", postalAddress);
			result += String.format("Emergency Contact: %s%n", emergencyContact);
			if (flag) result += String.format("Personal Tutor: %s", personalTutor);

			return result;
		} catch (ClassNotFoundException e) {
			System.out.println("Postgresql driver not found.");
		} catch (SQLException e) {
			System.out.println("An error occurred in the process.");
			System.out.println("Review the following message and try again:");
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
			} catch (NullPointerException e) {
			}
		}

		return null;
	}

	public static String produceReportForStudent(int studentID) {
		return produceReportForStudent(studentID, true);
	}

	public static String produceReportForLecturer(int lecturerID) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			String result = "";

			System.out.println("Producing a report for lecturer ...");
			conn = Util.getConnection();

			//Get information about the lecturer
			stmt = conn.prepareStatement(""
					+ "SELECT titleString, foreName, familyName "
					+ "FROM Lecturer, Titles "
					+ "WHERE Lecturer.lecturerID = ? "
					+ "AND Lecturer.titleID = Titles.titleID;");
			stmt.setInt(1, lecturerID);
			rs = stmt.executeQuery();
			
			if (!rs.next()) {
				return "No lecturer found with lecturerID = " + lecturerID;
			} else {
				do {
					result += String.format("Lecturer name: %s %s %s", 
							rs.getString("titleString"), rs.getString("foreName"), rs.getString("familyName"));
				} while (rs.next());
			}
			
			rs.close();
			stmt.close();
			
			//Get information about tutees
			stmt = conn.prepareStatement(""
					+ "SELECT Tutor.studentID, yearOfStudy FROM Tutor, StudentRegistration WHERE lecturerID = ? "
					+ "AND StudentRegistration.studentID = Tutor.studentID " + "ORDER BY yearOfStudy;");
			stmt.setInt(1, lecturerID);
			rs = stmt.executeQuery();

			int currentYear = -1;

			if (!rs.next()) {
				return "No tutees found.";
			}
			do {
				int studentID = rs.getInt("studentID");
				int yearOfStudy = rs.getInt("yearOfStudy");

				if (yearOfStudy > currentYear) {
					result += "\n";

					result += String.format("---Year %d---%n", yearOfStudy);
					currentYear = yearOfStudy;
				}
				
				result += produceReportForStudent(studentID, false) + "\n";
			} while (rs.next());

			return result;
		} catch (ClassNotFoundException e) {
			System.out.println("Postgresql driver not found.");
		} catch (SQLException e) {
			System.out.println("An error occurred in the process.");
			System.out.println("Review the following message and try again:");
			System.out.println(e.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
			} catch (NullPointerException e) {
			}
		}

		return null;
	}
}
