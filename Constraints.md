Part 1: Data Definition
=======================
Below are the constraints for the tables in the database.

Student
-------
| Field       | Type          | Constraints |
| ----------- | ------------- | ----------- |
| studentID   | INTEGER       | PRIMARY KEY |
| titleID     | INTEGER       | FOREIGN KEY |
| foreName    | VARCHAR(255)  | NOT NULL    |
| familyName  | VARCHAR(255)  | NOT NULL    |
| dateOfBirth | DATE          | NOT NULL    |

Lecturer
--------
| Field       | Type          | Constraints |
| ----------- | ------------- | ----------- |
| lecturerID  | INTEGER       | PRIMARY KEY |
| titleID     | INTEGER       | FOREIGN KEY |
| foreName    | VARCHAR(255)  | NOT NULL    |
| familyName  | VARCHAR(255)  | NOT NULL    |

StudentRegistration
-------------------
| Field               | Type         | Constraints |
| ------------------- | ------------ | ----------- |
| studentID           | INTEGER      | FOREIGN KEY |
| yearOfStudy         | INTEGER      | NOT NULL    |
| registrationTypeID  | VARCHAR(255) | FOREIGN KEY |

StudentContact
--------------
| Field         | Type         | Constraints       |
| ------------- | ------------ | ----------------- |
| studentID     | INTEGER      | FOREIGN KEY       |
| eMailAddress  | VARCHAR(255) | NOT NULL UNIQUE ? |
| postalAddress | VARCHAR(255) | NOT NULL          |  
<!--eMailAddress - Since there needs to be an email address, but two students cannot have the same email, e.g. password resets etc. -->

NextOfKinContact
----------------
| Field         | Type         | Constraints |
| -----------   | ------------ | ----------- |
| studentID     | INTEGER      | FOREIGN KEY |
| name          | VARCHAR(255) | NOT NULL    |
| eMailAddress  | VARCHAR(255) | NOT NULL?   |
| postalAddress | VARCHAR(255) | NOT NULL    |

LecturerContact
---------------
| Field        | Type         | Constraints   |
| ------------ | ------------ | ------------- |
| lecturerID   | INTEGER      | FOREIGN KEY   |
| office       | INTEGER      | NOT NULL?     |
| eMailAddress | VARCHAR(255) | NOT NULL?     |

Tutor
-----
| Field      | Type    | Constraints   |
| ---------- | ------- | ------------- |
| studentID  | INTEGER | FOREIGN KEY   |
| lecturerID | INTEGER | FOREIGN KEY   |


Titles
------
| Field        | Type         | Constraints     |
| ------------ | ------------ | --------------- |
| titleID      | INTEGER      | PRIMARY KEY     |
| titleString  | VARCHAR(255) | NOT NULL UNIQUE |

RegistrationType
----------------
| Field              | Type         | Constraints     |
| ------------------ | ------------ | --------------- |
| registrationTypeID | INTEGER      | PRIMARY KEY     |
| description        | VARCHAR(255) | NOT NULL UNIQUE |


















----------
