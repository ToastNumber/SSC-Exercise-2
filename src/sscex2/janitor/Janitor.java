package sscex2.janitor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sscex2.util.Util;

public class Janitor {
	private static final String SCHEMA_PATH = "resources/Tables.xml";

	/**
	 * Sets up the relations in the database (but without any data).
	 * 
	 * @param conn
	 *            the connection to the database
	 * @param srcPath
	 *            the path of the xml file holding the schema
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static void setupTables(Connection conn, String srcPath) throws SQLException,
			ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(srcPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		// Create the tables but don't apply foreign keys yet
		NodeList tables = doc.getElementsByTagName("table");
		for (int i = 0; i < tables.getLength(); ++i) {
			Element currentTable = (Element) tables.item(i);
			NodeList primaryKeys = currentTable.getElementsByTagName("primarykey");
			NodeList attributes = currentTable.getElementsByTagName("attribute");

			String update = String.format("CREATE TABLE %s (%n", currentTable.getAttribute("name"));

			for (int j = 0; j < attributes.getLength(); ++j) {
				Element currentAttribute = (Element) attributes.item(j);
				String name = currentAttribute.getElementsByTagName("name").item(0).getTextContent();
				String type = currentAttribute.getElementsByTagName("type").item(0).getTextContent();

				update += name + " ";

				NodeList constraints = currentAttribute.getElementsByTagName("constraint");
				update += type + (constraints.getLength() > 0 ? " " : "");

				for (int k = 0; k < constraints.getLength(); ++k) {
					String delim = (k < constraints.getLength() - 1 ? " " : "");
					String currentConstraintString = constraints.item(k).getTextContent();

					update += currentConstraintString + delim;
				}

				if (j < attributes.getLength() - 1 || primaryKeys.getLength() > 0) {
					update += ",\n";
				}
			}

			// Apply primary key
			if (primaryKeys.getLength() > 0) {
				update += String.format("PRIMARY KEY (%s)", primaryKeys.item(0).getTextContent());
			}

			update += "\n);";
			System.out.println(update);
			System.out.println();

			PreparedStatement stmt = conn.prepareStatement(update);
			stmt.executeUpdate();
			stmt.close();
		}

		// Now the foreign key constraints
		for (int i = 0; i < tables.getLength(); ++i) {
			Element currentTable = (Element) tables.item(i);
			String tableName = currentTable.getAttribute("name");
			NodeList foreignKeys = currentTable.getElementsByTagName("foreignkey");

			for (int j = 0; j < foreignKeys.getLength(); ++j) {
				Element currentForeignKey = (Element) foreignKeys.item(j);
				String field = currentForeignKey.getElementsByTagName("field").item(0).getTextContent();
				String ref = currentForeignKey.getElementsByTagName("ref").item(0).getTextContent();

				String update = String.format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s;", tableName,
						field, ref);

				System.out.println(update);

				PreparedStatement stmt = conn.prepareStatement(update);
				stmt.executeUpdate();
				stmt.close();
			}
		}
	}

	/**
	 * Deletes all the tables in the database specified by Tables.xml.
	 * 
	 * @param conn
	 *            the connection to the database
	 * @throws SQLException
	 */
	private static void wipeContentsOfDB(Connection conn) throws SQLException, ParserConfigurationException,
			SAXException, IOException {
		File fXmlFile = new File("resources/Tables.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		NodeList tableList = doc.getElementsByTagName("table");

		for (int i = 0; i < tableList.getLength(); ++i) {
			Node currentTable = tableList.item(i);
			String tableName = ((Element) currentTable).getAttribute("name");

			String str = String.format("DROP TABLE IF EXISTS %s CASCADE;", tableName);
			PreparedStatement stmt = conn.prepareStatement(str);
			stmt.executeUpdate();
		}
	}

	private static void populateTables(Connection conn) throws SQLException {
		final int numStudents = 150;
		final int numLecturers = 10;
		final int studentIDStart = 1000000;
		final int lecturerIDStart = 10000;
		
		Random rand = new Random();
		
		PreparedStatement stmt;
		
		//Populate table: Titles
		System.out.println("Populating table [Titles] ...");
		for (int i = 0; i < RandomDataProvider.numTitles(); ++i) {
			stmt = conn.prepareStatement("INSERT INTO Titles VALUES (?, ?);");
			stmt.setInt(1, i);
			stmt.setString(2, RandomDataProvider.getTitle(i));
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [Titles] populated.");

		//Populate table: RegistrationType
		System.out.println("Populating table [RegistrationType] ...");
		for (int i = 0; i < RandomDataProvider.numRegistrationTypes(); ++i) {
			stmt = conn.prepareStatement("INSERT INTO RegistrationType VALUES (?, ?);");
			stmt.setInt(1, i);
			stmt.setString(2, RandomDataProvider.getRegType(i));
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [RegistrationType] populated.");
		
		//Populate table: Student
		System.out.println("Populating table [Student] ...");
		for (int i = 0; i < numStudents; ++i) {
			stmt = conn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?, ?);");
			stmt.setInt(1, studentIDStart + i);
			stmt.setInt(2, RandomDataProvider.randomStudentTitleID());
			stmt.setString(3, RandomDataProvider.randomForename());
			stmt.setString(4, RandomDataProvider.randomSurname());
			stmt.setDate(5, Date.valueOf(RandomDataProvider.randomDate()));
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [Student] populated.");

		//Populate table: Lecturer
		System.out.println("Populating table [Lecturer] ...");
		for (int i = 0; i < numLecturers; ++i) {
			stmt = conn.prepareStatement("INSERT INTO Lecturer VALUES (?, ?, ?, ?);");
			stmt.setInt(1, 10000 + i);
			stmt.setInt(2, RandomDataProvider.randomTitleID());
			stmt.setString(3, RandomDataProvider.randomForename());
			stmt.setString(4, RandomDataProvider.randomSurname());
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [Lecturer] populated.");
		
		//Populate table: StudentRegistration
		System.out.println("Populating table [StudentRegistration] ...");
		for (int i = 0; i < numStudents; ++i) {
			int studentID = 1000000 + i;
			stmt = conn.prepareStatement("INSERT INTO StudentRegistration VALUES (?, ?, ?)");
			stmt.setInt(1, studentID);
			
			PreparedStatement q = conn.prepareStatement("SELECT dateOfBirth FROM Student WHERE studentID = ?;");
			q.setInt(1, studentID);
			ResultSet rs = q.executeQuery();
			
			while (rs.next()) {
				String date = "" + rs.getDate("dateOfBirth");
				String year = date.substring(0, date.indexOf("-"));
				stmt.setInt(2, 2015 - Integer.valueOf(year) - 18);
			}
			
			stmt.setInt(3, RandomDataProvider.randomRegistrationTypeID());
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [StudentRegistration] populated.");
		
		//Populate table: StudentContact
		System.out.println("Populating table [StudentContact] ...");
		for (int i = 0; i < numStudents; ++i) {
			int studentID = studentIDStart + i;
			
			stmt = conn.prepareStatement("INSERT INTO StudentContact VALUES (?, ?, ?);");
			stmt.setInt(1, studentID);
			
			PreparedStatement q = conn.prepareStatement("SELECT foreName, familyName FROM Student WHERE studentID = ?;");
			q.setInt(1, studentID);
			ResultSet rs = q.executeQuery();

			//Should only run once
			while (rs.next()) {
				String foreName = rs.getString("foreName");
				String familyName = rs.getString("familyName");

				stmt.setString(2, RandomDataProvider.randomEmail(foreName, familyName, "" + (studentID % 1000)));
			}
			
			stmt.setString(3, RandomDataProvider.randomAddress());
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [StudentContact] populated.");
		
		//Populate table: NextOfKinContact
		System.out.println("Populating table [NextOfKinContact] ...");
		for (int i = 0; i < numStudents; ++i) {
			int studentID = studentIDStart + i;
			
			stmt = conn.prepareStatement("INSERT INTO NextOfKinContact VALUES (?, ?, ?, ?);");
			stmt.setInt(1, studentID);
			
			String foreName = RandomDataProvider.randomForename();
			String familyName = RandomDataProvider.randomSurname();
			stmt.setString(2, RandomDataProvider.getTitle(RandomDataProvider.randomStudentTitleID()) + " " + foreName + " " + familyName);
			stmt.setString(3, RandomDataProvider.randomEmail(foreName, familyName, String.format("%04d", rand.nextInt(1000))));
			stmt.setString(4, RandomDataProvider.randomAddress());
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("NextOfKinContact table populated.");
		
		//Populate table: LecturerContact
		System.out.println("Populating table [LecturerContact] ...");
		for (int i = 0; i < numLecturers; ++i) {
			int lecturerID = lecturerIDStart + i;
			int officeNumber = 101 + i;
			
			stmt = conn.prepareStatement("INSERT INTO LecturerContact VALUES (?, ?, ?);");
			stmt.setInt(1, lecturerID);
			stmt.setInt(2, officeNumber);
			
			PreparedStatement q = conn.prepareStatement("SELECT foreName, familyName FROM Lecturer WHERE lecturerID = ?;");
			q.setInt(1, lecturerID);
			ResultSet rs = q.executeQuery();
			
			while (rs.next()) {
				String foreName = rs.getString("foreName");
				String familyName = rs.getString("familyName");
				
				stmt.setString(3, RandomDataProvider.randomEmail(foreName, familyName, "" + officeNumber));
			}
			
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [LecturerContact] populated.");
		
		//Populate table: Tutor
		System.out.println("Populating table [Tutor]...");
		int[] tutorIDs = new int[2];
		tutorIDs[0] = rand.nextInt(numLecturers);
		tutorIDs[1] = (tutorIDs[0] + 5) % numLecturers;
		tutorIDs[0] += lecturerIDStart;
		tutorIDs[1] += lecturerIDStart;
		
		for (int i = 0; i < 2; ++i) {
			for (int j = 0; j < 5; ++j) {
				stmt = conn.prepareStatement("INSERT INTO Tutor VALUES (?, ?);");
				stmt.setInt(1, studentIDStart + (10 * i) + j);
				stmt.setInt(2, tutorIDs[i]);
				
				stmt.executeUpdate();
				stmt.close();
			}
		}
		System.out.println("Table [Tutor] populated.");
	}

	public static void main(String[] args) {
		System.out.println("Waking up Janitor ...\n");

		Connection conn = null;
		
		// Load the driver
		try {
			conn = Util.getConnection();
		} catch (ClassNotFoundException e) {
			System.err.println("Postgresql Driver not found. Exiting ...");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		// Setup tables
		try {
			System.out.printf("Wiping contents of database [%s] ...%n", Util.DB);
			wipeContentsOfDB(conn);
			System.out.println("Wiping successful.\n");

			System.out.println("Setting up tables ...");
			setupTables(conn, SCHEMA_PATH);
			System.out.println("Tables set up successfully.\n");

			System.out.println("Populating tables with test data ...");
			populateTables(conn);
			System.out.println("Tables are now populated.");

		} catch (SQLException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("Closing database connection ...");
				conn.close();
				System.out.println("Connection closed.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		System.out.println("End.");
	}
}
