SELECT students.name, students.snum
FROM students
JOIN major ON students.snum = major.snum
JOIN register ON students.snum = register.snum
JOIN courses ON register.course_number = courses.number
WHERE courses.name = 'database' AND major.level IN ('MS', 'PhD')
ORDER BY students.snum;