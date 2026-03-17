CREATE TABLE students (
    snum INT,
    ssn INT PRIMARY KEY,
    name VARCHAR(20),
    gender VARCHAR(1),
    dob VARCHAR(10),
    c_addr VARCHAR(20),
    c_phone VARCHAR(10),
    p_addr VARCHAR(20),
    p_phone VARCHAR(10),
    UNIQUE (snum)
);

CREATE TABLE departments (
    code INT PRIMARY KEY,
    name VARCHAR(50) UNIQUE,
    phone VARCHAR(10),
    college VARCHAR(20)
);

CREATE TABLE degrees (
    name VARCHAR(50),
    level VARCHAR(5),
    department_code INT,
    PRIMARY KEY (name, level),
    FOREIGN KEY (department_code) REFERENCES departments(code)
);

CREATE TABLE courses (
    number INT PRIMARY KEY,
    name VARCHAR(50),
    description VARCHAR(50),
    credithours INT,
    level VARCHAR(20),
    department_code INT,
    FOREIGN KEY (department_code) REFERENCES departments(code)
);

CREATE TABLE register (
    snum INT,
    course_number INT,
    regtime VARCHAR(20),
    grade INT,
    PRIMARY KEY (snum, course_number),
    FOREIGN KEY (snum) REFERENCES students(snum),
    FOREIGN KEY (course_number) REFERENCES courses(number)
);

CREATE TABLE major (
    snum INT,
    name VARCHAR(50),
    level VARCHAR(5),
    PRIMARY KEY (snum, name, level),
    FOREIGN KEY (snum) REFERENCES students(snum),
    FOREIGN KEY (name, level) REFERENCES degrees(name, level)
);

CREATE TABLE minor (
    snum INT,
    name VARCHAR(50),
    level VARCHAR(5),
    PRIMARY KEY (snum, name, level),
    FOREIGN KEY (snum) REFERENCES students(snum),
    FOREIGN KEY (name, level) REFERENCES degrees(name, level)
);