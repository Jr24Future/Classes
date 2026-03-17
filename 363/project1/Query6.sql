SELECT students.snum, students.name
FROM students
JOIN minor ON students.snum = minor.snum
ORDER BY students.snum;
