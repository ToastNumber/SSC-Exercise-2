package sscex2.janitor;

import java.util.Random;

class RandomDataProvider {
	private static final String[] forenameSet = { "Ashley", "Cameron", "Drew", "Jamie", "Jesse", "Jordan", "Quinn", "Reese", "Logan", "Taylor", "Devin", "Corey", "Carson", "Emerson", "Harper", "Jaiden", "Mason", "Tristan" };
	private static final String[] surnameSet = { "Smith", "Jones", "Taylor", "Brown", "Williams", "Wilson", "Johnson", "Davies", "Robinson", "Wright", "Thompson", "Evans", "Walker", "White", "Roberts", "Green", "Hall", "Wood", "Jackson", "Clarke" };
	private static final String[] titleSet = { "Ms", "Miss", "Mrs", "Mr", "Dr", "Prof" };
	private static final String[] registrationSet = { "Normal", "Repeat", "External" };

	private static final String[] streetNameSet = { "Dale Road", "Bristol Road", "Grange Road", "Tiverton Road", "Dawlish Road" };
	private static final String streetArea = "Selly Oak";
	private static final String city = "Birmingham";
	private static final String country = "England";

	public static int numTitles() {
		return titleSet.length;
	}

	public static int numRegistrationTypes() {
		return registrationSet.length;
	}

	public static String getTitle(int index) {
		return titleSet[index];
	}

	public static String getRegType(int index) {
		return registrationSet[index];
	}

	private static String randomElementOf(String[] set) {
		return set[(new Random()).nextInt(set.length)];
	}

	public static String randomForename() {
		return randomElementOf(forenameSet);
	}

	public static String randomSurname() {
		return randomElementOf(surnameSet);
	}

	public static int randomStudentTitleID() {
		int titleID;

		do {
			titleID = randomTitleID();
		} while (titleSet[titleID].equals("Dr") || titleSet[titleID].equals("Prof")
				|| titleSet[titleID].equals("Mrs"));

		return titleID;
	}
	
	public static int randomTitleID() {
		return (new Random()).nextInt(titleSet.length);
	}

	public static int randomRegistrationTypeID() {
		return (new Random()).nextInt(registrationSet.length);
	}

	private static String randomLetter() {
		int a = 65;
		return "" + (char) (a + (new Random()).nextInt(26));
	}

	private static int randomDigit() {
		return (new Random()).nextInt(10);
	}

	private static String randomPostCode() {
		return String.format("B%d%d%d%s%s", randomDigit(), randomDigit(), randomDigit(), randomLetter(),
				randomLetter());
	}

	public static String randomAddress() {
		return String.format("%d %s, %s, %s, %s, %s", (new Random()).nextInt(99) + 1,
				randomElementOf(streetNameSet), streetArea, city, country, randomPostCode());
	}
	
	public static String randomEmail(String foreName, String familyName, String append) {
		return String.format("%s.%s%s@gmail.com", foreName, familyName, append);
	}

	/**
	 * Not uniformly distributed. Days will only be between 1-28.
	 * @return a random date between 1992-01-01 and 1996-12-31, but 
	 */
	public static String randomDate() {
		int year = 1992 + (new Random()).nextInt(5);
		int month = 1 + (new Random()).nextInt(12);
		int day = 1 + (new Random()).nextInt(28);
		
		return String.format("%04d-%02d-%02d", year, month, day);
	}

	public static void main(String[] args) {
		System.out.println("Title ID: " + randomTitleID());
		System.out.println("Forename: " + randomForename());
		System.out.println("Surname: " + randomSurname());
		System.out.println("Registration Type ID: " + randomRegistrationTypeID());
		System.out.println("Address: " + randomAddress());
		System.out.println("DOB: " + randomDate());
	}
}
