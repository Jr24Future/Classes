SELECT students.name, students.snum, students.ssn
FROM students
WHERE students.name LIKE '%n%' OR students.name LIKE '%N%'
ORDER BY students.snum;