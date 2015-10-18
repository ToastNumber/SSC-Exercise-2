package sscex2.janitor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Janitor {
	private static final String USER = "kjm409";
	private static final String PASS = "briswosw";
	private static final String DB = USER;
	private static final String URL = "jdbc:postgresql://dbteach2.cs.bham.ac.uk/" + DB;
	
	private static void setupTables(Connection conn) throws SQLException, ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File("resources/Tables.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		// Create the tables but don't apply the constraints yet
		NodeList tables = doc.getElementsByTagName("table");
		
		throw new SQLException("setupTables: not implemented");
	}
	
	/**
	 * Deletes all the tables in the database.
	 * @param conn the connection to the database
	 * @throws SQLException
	 */
	private static void wipeContentsOfDB(Connection conn) throws SQLException, ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File("resources/Tables.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		NodeList tableList = doc.getElementsByTagName("table");
		
		for (int i = 0; i < tableList.getLength(); ++i) {
			Node currentTable = tableList.item(i);
			String tableName = ((Element) currentTable).getAttribute("name");
			
			String str = String.format("DROP TABLE IF EXISTS %s;", tableName);
			PreparedStatement stmt = conn.prepareStatement(str);
			stmt.executeUpdate();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Waking up Janitor ...\n");
		
		// Load the driver
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Postgresql Driver not found. Exiting ...");
			System.exit(1);
		}

		
		//Setup tables
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			
			System.out.printf("Wiping contents of %s ...%n", DB);
			wipeContentsOfDB(conn);
			System.out.println("Wiping successful.\n");
			
			System.out.println("Setting up tables ...");
			setupTables(conn);
			System.out.println("Tables set up successfully.\n");
		} catch (SQLException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("End.");
	}
}




















