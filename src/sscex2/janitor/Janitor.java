package sscex2.janitor;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
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
	 * Deletes all the tables in the database specified by Tables.xml.
	 * 
	 * @param conn
	 *            the connection to the database
	 * @throws SQLException
	 */
	private static void wipeContentsOfDB(Connection conn) throws SQLException, ParserConfigurationException,
			SAXException, IOException {
		System.out.printf("Wiping contents of database [%s] ...%n", Util.DB);
		
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
		
		System.out.println("Wiping successful.\n");		
	}
	
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
		System.out.println("Setting up tables ...");
		
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
		
		System.out.println("Tables set up successfully.\n");
	}

	/**
	 * Populates the tables in the database with random data.
	 * 
	 * @param conn
	 *            the connection to the database
	 * @throws SQLException
	 */
	private static void populateTables(Connection conn) throws SQLException {
		System.out.println("Populating tables with test data ...");
		
		final int studentIDStart = 1000000;
		
		Random rand = new Random();
		
		PreparedStatement stmt;
		
		// Populate table [Titles]
		System.out.println("Populating table [Titles] ...");
		for (int i = 0; i < RandomDataProvider.numTitles(); ++i) {
			stmt = conn.prepareStatement("INSERT INTO Titles VALUES (?, ?);");
			stmt.setInt(1, i);
			stmt.setString(2, RandomDataProvider.getTitle(i));
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [Titles] populated.");
		
		// Populate table [RegistrationType]
		System.out.println("Populating table [RegistrationType] ...");
		for (int i = 0; i < RandomDataProvider.numRegistrationTypes(); ++i) {
			stmt = conn.prepareStatement("INSERT INTO RegistrationType VALUES (?, ?);");
			stmt.setInt(1, i);
			stmt.setString(2, RandomDataProvider.getRegType(i));
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [RegistrationType] populated.");
		
		// Populate table [Student]
		final String[] forenames = { "Ashley", "Cameron", "Drew", "Jamie", "Jesse", "Jordan", "Quinn", "Reese", "Logan", "Devin"};
		final String[] familyNames  = { "Smith", "Jones", "Taylor", "Brown", "Williams", "Wilson", "Johnson", "Davies", "Robinson", "Wright"};
		
		System.out.println("Populating table [Student] ...");
		for (int i = 0; i < forenames.length; ++i) {
			String forename = forenames[i];
			for (int j = 0; j < familyNames.length; ++j) {
				String familyName = familyNames[j];
				int studentID = studentIDStart + (i * forenames.length) + j;
				int titleID = RandomDataProvider.randomStudentTitleID();
				String dateOfBirth = RandomDataProvider.randomDate();
				
				stmt = conn.prepareStatement("INSERT INTO Student VALUES (?, ?, ?, ?, ?);");
				stmt.setInt(1, studentID);
				stmt.setInt(2, titleID);
				stmt.setString(3, forename);
				stmt.setString(4, familyName);
				stmt.setDate(5, Date.valueOf(dateOfBirth));
				stmt.executeUpdate();
				stmt.close();
			}
		}
		System.out.println("Table [Student] populated.");
		
		// Populate table [Lecturer]
		final String[] lecturerInserts = {
				"INSERT INTO Lecturer VALUES (10000, 4, 'John', 'Smith');",
				"INSERT INTO Lecturer VALUES (10001, 4, 'David', 'Lee');",
				"INSERT INTO Lecturer VALUES (10002, 4, 'Emma', 'Williams');",
				"INSERT INTO Lecturer VALUES (10003, 5, 'Rebecca', 'Trotter');",
				"INSERT INTO Lecturer VALUES (10004, 4, 'Ricky', 'Merchant');"
		};
		
		System.out.println("Populating table [Lecturer] ...");
		for (int i = 0; i < lecturerInserts.length; ++i) {
			stmt = conn.prepareStatement(lecturerInserts[i]);
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [Lecturer] populated.");
		
		// Populate table [LecturerContact]
		final String[] lecturerContactInserts = {
			"INSERT INTO LecturerContact VALUES (10000, 123, 'JohnSmith@bham.ac.uk');",
			"INSERT INTO LecturerContact VALUES (10001, 234, 'DavidLee@bham.ac.uk');",
			"INSERT INTO LecturerContact VALUES (10002, 345, 'EmmaWilliams@bham.ac.uk');",
			"INSERT INTO LecturerContact VALUES (10003, 456, 'RebeccaTrotter@bham.ac.uk');",
			"INSERT INTO LecturerContact VALUES (10004, 567, 'RickyMerchant@bham.ac.uk');"
		};
		System.out.println("Populating table [LecturerContact] ...");
		for (int i = 0; i < lecturerContactInserts.length; ++i) {
			stmt = conn.prepareStatement(lecturerContactInserts[i]);
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [LecturerContact] populated.");
		
		// Populate table [StudentRegistration]
		System.out.println("Populating table [StudentRegistration] ...");
		for (int i = 0; i < forenames.length * familyNames.length; ++i) {
			int studentID = studentIDStart + i;
			int yearOfStudy = 1 + rand.nextInt(5);
			int registrationTypeID = RandomDataProvider.randomRegistrationTypeID();
			
			stmt = conn.prepareStatement("INSERT INTO StudentRegistration VALUES (?,?,?);");
			stmt.setInt(1, studentID);
			stmt.setInt(2, yearOfStudy);
			stmt.setInt(3, registrationTypeID);
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [StudentRegistration] populated.");
		
		// Populate table [StudentContact]
		System.out.println("Populating table [StudentContact] ...");
		for (int i = 0; i < forenames.length * familyNames.length; ++i) {
			int studentID = studentIDStart + i;
			String emailAddress = RandomDataProvider.randomEmail(forenames[i/forenames.length], familyNames[i % forenames.length], "" + studentID % 100);
			String address = RandomDataProvider.randomAddress();
			
			stmt = conn.prepareStatement("INSERT INTO StudentContact VALUES (?,?,?);");
			stmt.setInt(1, studentID);
			stmt.setString(2, emailAddress);
			stmt.setString(3, address);
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [StudentContact] populated.");
		
		// Populate table [NextOfKinContact]
		System.out.println("Populating table [NextOfKinContact] ...");
		for (int i = 0; i < forenames.length * familyNames.length; ++i) {
			int studentID = studentIDStart + i;
			String forename = RandomDataProvider.randomForename();
			String familyName = RandomDataProvider.randomFamilyName();
			String email = RandomDataProvider.randomEmail(forename, familyName, "" + (studentID + 50) % 100);
			String address = RandomDataProvider.randomAddress();
			
			stmt = conn.prepareStatement("INSERT INTO NextOfKinContact VALUES (?,?,?,?);");
			stmt.setInt(1, studentID);
			stmt.setString(2, forename + " " + familyName);
			stmt.setString(3, email);
			stmt.setString(4, address);
			stmt.executeUpdate();
			stmt.close();
		}
		System.out.println("Table [NextOfKinContact] populated.");

		for (int i = 0; i < forenames.length * familyNames.length; ++i) {
			int sid = studentIDStart + i;
			
			stmt = conn.prepareStatement("INSERT INTO Tutor VALUES (?, ?);");
			stmt.setInt(1, sid);
			// To give tutor 10000 6 tutees
			stmt.setInt(2, i < 6 ? 10000 : 10001 + i/25);
			stmt.executeUpdate();
			stmt.close();
		}
		
		System.out.println("Table [NextOfKinContact] populated.");
		
		System.out.println("Tables are now populated.");
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
			wipeContentsOfDB(conn);
			setupTables(conn, SCHEMA_PATH);
			populateTables(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
