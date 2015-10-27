Part 1: Data Definition
=======================
Below are the constraints for the tables in the database.

Student
-------
| Field       | Type          | Constraints  | Validation            |
| ----------- | ------------- | ------------ | --------------------- |
| studentID   | INTEGER       | PRIMARY KEY  | studentID >= 0 |
| titleID     | INTEGER       | FOREIGN KEY  |				               |
| foreName    | VARCHAR(255)  | NOT NULL     |					             |
| familyName  | VARCHAR(255)  | NOT NULL     |					             |
| dateOfBirth | DATE          | NOT NULL     |	   		               |

Lecturer
--------
| Field       | Type          | Constraints | Validation |
| ----------- | ------------- | ----------- | ---------- |
| lecturerID  | INTEGER       | PRIMARY KEY | lecturerID >= 0 |
| titleID     | INTEGER       | FOREIGN KEY | |
| foreName    | VARCHAR(255)  | NOT NULL    | |
| familyName  | VARCHAR(255)  | NOT NULL    | |

StudentRegistration
-------------------
| Field               | Type    | Constraints | Validation |
| ------------------- | ------- | ----------- | ---------- |
| studentID           | INTEGER | FOREIGN KEY | |
| yearOfStudy         | INTEGER | NOT NULL    | yearOfStudy >= 1 |
| registrationTypeID  | INTEGER | FOREIGN KEY | |

StudentContact
--------------
| Field         | Type         | Constraints       | Validation |
| ------------- | ------------ | ----------------- | ---------- |
| studentID     | INTEGER      | FOREIGN KEY       | |
| eMailAddress  | VARCHAR(255) | NOT NULL UNIQUE | LIKE '_%@%' AND NOT LIKE '% %' |
| postalAddress | VARCHAR(255) | NOT NULL          | |

The validation for `eMailAddress` specifies that the email should be of the form a@b, and should not have any spaces.

NextOfKinContact
----------------
| Field         | Type         | Constraints | Validation |
| -----------   | ------------ | ----------- | ---------- |
| studentID     | INTEGER      | FOREIGN KEY | |
| name          | VARCHAR(255) | NOT NULL    | |
| eMailAddress  | VARCHAR(255) | NOT NULL UNIQUE  | LIKE '_%@%' AND NOT LIKE '% %' |
| postalAddress | VARCHAR(255) | NOT NULL    | |

LecturerContact
---------------
| Field        | Type         | Constraints   | Validation |
| ------------ | ------------ | ------------- | ---------- |
| lecturerID   | INTEGER      | FOREIGN KEY   | |
| office       | INTEGER      | NOT NULL     | |
| eMailAddress | VARCHAR(255) | NOT NULL UNIQUE    | LIKE '_%@%' AND NOT LIKE '% %' |

Tutor
-----
| Field      | Type    | Constraints   | Validation |
| ---------- | ------- | ------------- | ---------- |
| studentID  | INTEGER | FOREIGN KEY   | |
| lecturerID | INTEGER | FOREIGN KEY   | |

Titles
------
| Field        | Type         | Constraints     | Validation |
| ------------ | ------------ | --------------- | ---------- |
| titleID      | INTEGER      | PRIMARY KEY     | titleID >= 0 |
| titleString  | VARCHAR(255) | NOT NULL UNIQUE | |

RegistrationType
----------------
| Field              | Type         | Constraints     | Validation |
| ------------------ | ------------ | --------------- | ---------- |
| registrationTypeID | INTEGER      | PRIMARY KEY     | registrationType >= 0 |
| description        | VARCHAR(255) | NOT NULL UNIQUE | |


















----------
