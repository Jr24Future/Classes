UPDATE students
SET name = 'Scott'
WHERE ssn = 144673371;

UPDATE major
SET name = 'Computer Science', level = 'MS'
WHERE snum = (SELECT snum FROM students WHERE ssn = 144673371);

SET SQL_SAFE_UPDATES = 0;
DELETE FROM register WHERE regtime = 'Summer2024';
SET SQL_SAFE_UPDATES = 1;
