SELECT students.name, students.snum, students.ssn
FROM students
WHERE students.name BETWEEN 'Amy' AND 'Christopher'
ORDER BY students.name;