package sscex2.janitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Janitor {
	private static final String URL = "jdbc:postgresql://dbteach2.cs.bham.ac.uk/";
	private static final String USER = "kjm409";
	private static final String PASS = "briswosw";
	private static final String DB = "DB1";
	
	public static void main(String[] args) {
		System.out.println("Waking up Janitor ...");
		
		// Load the driver
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Postgresql Driver not found. Exiting ...");
			System.exit(1);
		}
		
		// Create the database if it doesn't exist.
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			
			//PreparedStatement stmt = conn.prepareStatement("CREATE DATABASE ?;");
			PreparedStatement stmt = conn.prepareStatement("DROP DATABASE DB1;");
			stmt.executeUpdate();
			
			return;
			
			//System.out.println("Attempting to create database DB1.");
			//stmt.executeUpdate();
			//System.out.println("DB1 created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
