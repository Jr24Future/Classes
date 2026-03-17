LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/students.csv'
INTO TABLE students
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(snum, ssn, name, gender, dob, c_addr, c_phone, p_addr, p_phone);

LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/departments.csv'
INTO TABLE departments
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(code, name, phone, college);

LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/degrees.csv'
INTO TABLE degrees
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(name, level, department_code);

LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/courses.csv'
INTO TABLE courses
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(number, name, description, credithours, level, department_code);

LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/register.csv'
INTO TABLE register
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(snum, course_number, regtime, grade);

LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/major.csv'
INTO TABLE major
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(snum, name, level);

LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/minor.csv'
INTO TABLE minor
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS
(snum, name, level);
