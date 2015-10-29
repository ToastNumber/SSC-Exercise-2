Software Systems Components A: Exercise 2 [![Build Status](https://magnum.travis-ci.com/ToastNumber/SSC-Exercise-2.svg?token=rUw2Rzx9qvYA1kcCppku&branch=master)](https://magnum.travis-ci.com/ToastNumber/SSC-Exercise-2)
=========================================
This repository contains the code for the `Software Systems Components A: Exercise 2 - A Database Application for a University` assignment.

Part 1: Data Definition
-----------------------
You can find the constraints in the file [`Constraints.md`](Constraints.md). 

Part 2: Creating and populating the Data Base
---------------------------------------------
The `Janitor` class uses the information in the `Tables` XML file to generate a clean database. The `RandomDataProvider` class provides methods for getting random names, post codes, etc. The `Janitor` class uses `RandomDataProvider` to populate the database with random data.

Part 3: An interface using JDBC
-------------------------------
The `UI` class provides a command line interface for the operations specified in the exercise.
