package sscex2.ui;

import java.util.Scanner;

import sscex2.util.Enroller;

@SuppressWarnings("resource")
public class UI {
	private static final String[] OPTIONS = { "Register a new student", "Assign a tutor to a student", "Produce a student report", "Quit" };

	private static int getMenuChoice() {
		int option = -1;
		boolean error = false;

		Scanner in = new Scanner(System.in);

		do {
			System.out.println("|-o-o-o-University Database-o-o-o-|");
			error = false;

			for (int i = 0; i < OPTIONS.length; ++i) {
				System.out.printf("%d. %s%n", i + 1, OPTIONS[i]);
			}

			System.out.println();
			System.out.print("Enter option: ");

			if (in.hasNextInt()) {
				option = in.nextInt();

				if (option < 1 || option > OPTIONS.length) {
					System.out.println("Please enter a valid option number.");
					error = true;
				}
			} else {
				System.out.println("Please enter an integer.");
				in.nextLine();
				error = true;
			}
			
			System.out.println();
		} while (error || option < 1 || option > OPTIONS.length);

		return option;
	}

	/**
	 * 
	 * @param option
	 * @return true if the program should quit
	 */
	private static boolean handleMenuOption(int option) {
		Scanner in = new Scanner(System.in);
		boolean error = false;

		if (option == 1) {
			int studentID = -1;
			String title = "", foreName = "", familyName = "", dateOfBirth = "";

			do {
				error = false;

				System.out.println("|-o-o-o-Registering-a-Student-o-o-o-|");
				System.out.print("Enter student ID: ");
				if (in.hasNextInt()) {
					studentID = in.nextInt();
					in.nextLine();
				} else {
					error = true;
					System.out.println("Please enter an integer.");
					in.nextLine();
					System.out.println();
					continue;
				}

				System.out.print("Enter title (e.g. Mr, Miss): ");
				title = in.nextLine();

				System.out.print("Enter forename: ");
				foreName = in.nextLine();

				System.out.print("Enter family name: ");
				familyName = in.nextLine();

				System.out.print("Enter DOB (yyyy-mm-dd): ");
				dateOfBirth = in.nextLine();

				System.out.println();
				Enroller.enrolStudent(studentID, title, foreName, familyName, dateOfBirth);
				System.out.println();
			} while (error);
		} else if (option == 2) {
			int studentID = -1;
			int tutorID = -1;
			
			do {
				error = false;
				
				System.out.println("|-o-o-o-Assigning-a-Tutor-to-a-Student-o-o-o-|");
				System.out.print("Enter student ID: ");
				
				if (in.hasNextInt()) {
					studentID = in.nextInt();
					in.nextLine();
				} else {
					error = true;
					System.out.println("Please enter an integer.");
					in.nextLine();
					System.out.println();
					continue;
				}
				
				System.out.print("Enter tutor ID: ");
				if (in.hasNextInt()) {
					tutorID = in.nextInt();
					in.nextLine();
				} else {
					error = true;
					System.out.println("Please enter an integer.");
					in.nextLine();
					System.out.println();
					continue;
				}
				
				System.out.println();
				Enroller.assignStudentToTutor(studentID, tutorID);
				System.out.println();
			} while (error);
		} else if (option == OPTIONS.length) {
			return true;
		} else {
			throw new IllegalArgumentException(option + " not a valid option");
		}

		return false;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int menuChoice = -1;
		boolean quit = false;

		do {
			menuChoice = getMenuChoice();
			quit = handleMenuOption(menuChoice);
		} while (!quit);

		in.close();
		System.out.println("Terminated");
	}
}