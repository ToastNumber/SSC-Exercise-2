package sscex2.janitor;

import java.util.Random;

class RandomDataProvider {
	private static final String[] titleSet = { "Ms", "Miss", "Mrs", "Mr", "Dr", "Prof" };
	private static final String[] registrationSet = { "Normal", "Repeat", "External" };

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

	public static String randomForename() {
		final String[] forenames = { "Taylor", "Corey", "Carson", "Emerson", "Harper", "Jaiden", "Mason", "Tristan" };
		return forenames[(new Random()).nextInt(forenames.length)];
	}
	
	public static String randomFamilyName() {
		final String[] surnames = {"Thompson", "Evans", "Walker", "White", "Roberts", "Green", "Hall", "Wood", "Jackson", "Clarke"};
		return surnames[(new Random()).nextInt(surnames.length)];
	}
	
	/**
	 * @return a random postcode in the format BDDDLL (D=digit, L=letter)
	 */
	private static String randomPostCode() {
		return String.format("B%d%d%d%s%s", randomDigit(), randomDigit(), randomDigit(), randomLetter(),
				randomLetter());
	}

	/**
	 * @return a random address in the format <br>
	 * 1-99 StreetName StreetArea City Country PostCode
	 */
	public static String randomAddress() {
		final String[] streetNameSet = { "Dale Road", "Bristol Road", "Grange Road", "Tiverton Road", "Dawlish Road" };
		final String streetArea = "Selly Oak";
		final String city = "Birmingham";
		final String country = "England";
		
		return String.format("%d %s, %s, %s, %s, %s", (new Random()).nextInt(99) + 1,
				randomElementOf(streetNameSet), streetArea, city, country, randomPostCode());
	}

	/**
	 * @param foreName
	 * @param familyName
	 * @param append
	 * @return a random email address of the form FORENAME^FAMILYNAME^APPEND@student.bham.ac.uk
	 */
	public static String randomEmail(String foreName, String familyName, String append) {
		return String.format("%s.%s%s@student.bham.ac.uk", foreName, familyName, append);
	}

	/**
	 * Days will only be between 1-28.
	 * 
	 * @return a random date between 1992-01-01 and 1996-12-31.
	 */
	public static String randomDate() {
		int year = 1992 + (new Random()).nextInt(5);
		int month = 1 + (new Random()).nextInt(12);
		int day = 1 + (new Random()).nextInt(28);

		return String.format("%04d-%02d-%02d", year, month, day);
	}

}
