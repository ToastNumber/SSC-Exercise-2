Part 1: Data Definition
=======================
Below are the constraints for the tables in the database.

Notes
-----
Look into which fields should be `VARCHAR` and which should be `CHAR`. I think constants like titles should be CHAR and then others that can/should be inputted by user should be VARCHAR. (But then again, the user could update the titles table, so maybe just leave everything as VARCHAR).

Student
-------
| Field       | Type          | Constraints  | Validation            |
| ----------- | ------------- | ------------ | --------------------- |
| studentID   | INTEGER       | PRIMARY KEY  | CHECK (studentID >= 0) |
| titleID     | INTEGER       | FOREIGN KEY  |				               |
| foreName    | VARCHAR(255)  | NOT NULL     |					             |
| familyName  | VARCHAR(255)  | NOT NULL     |					             |
| dateOfBirth | DATE          | NOT NULL     |	   		               |

Lecturer
--------
| Field       | Type          | Constraints | Validation |
| ----------- | ------------- | ----------- | ---------- |
| lecturerID  | INTEGER       | PRIMARY KEY | CHECK (lecturerID >= 0) |
| titleID     | INTEGER       | FOREIGN KEY | |
| foreName    | VARCHAR(255)  | NOT NULL    | |
| familyName  | VARCHAR(255)  | NOT NULL    | |

StudentRegistration
-------------------
| Field               | Type    | Constraints | Validation |
| ------------------- | ------- | ----------- | ---------- |
| studentID           | INTEGER | FOREIGN KEY | |
| yearOfStudy         | INTEGER | NOT NULL    | |
| registrationTypeID  | INTEGER | FOREIGN KEY | |

StudentContact
--------------
| Field         | Type         | Constraints       | Validation |
| ------------- | ------------ | ----------------- | ---------- |
| studentID     | INTEGER      | FOREIGN KEY       | |
| eMailAddress  | VARCHAR(255) | NOT NULL UNIQUE ? | LIKE '_%@%' AND NOT LIKE '% %' |
| postalAddress | VARCHAR(255) | NOT NULL          | |
<!--eMailAddress - Since there needs to be an email address, but two students cannot have the same email, e.g. password resets etc. -->

NextOfKinContact
----------------
| Field         | Type         | Constraints | Validation |
| -----------   | ------------ | ----------- | ---------- |
| studentID     | INTEGER      | FOREIGN KEY | |
| name          | VARCHAR(255) | NOT NULL    | |
| eMailAddress  | VARCHAR(255) | NOT NULL?   | LIKE '_%@%' AND NOT LIKE '% %' |
| postalAddress | VARCHAR(255) | NOT NULL    | |

LecturerContact
---------------
| Field        | Type         | Constraints   | Validation |
| ------------ | ------------ | ------------- | ---------- |
| lecturerID   | INTEGER      | FOREIGN KEY   | |
| office       | INTEGER      | NOT NULL?     | |
| eMailAddress | VARCHAR(255) | NOT NULL?     | LIKE '_%@%' AND NOT LIKE '% %' |

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
| titleID      | INTEGER      | PRIMARY KEY     | CHECK (titleID >= 0) |
| titleString  | VARCHAR(255) | NOT NULL UNIQUE | |

RegistrationType
----------------
| Field              | Type         | Constraints     | Validation |
| ------------------ | ------------ | --------------- | ---------- |
| registrationTypeID | INTEGER      | PRIMARY KEY     | CHECK (registrationType >= 0) |
| description        | VARCHAR(255) | NOT NULL UNIQUE | |


















----------
