SELECT major.name, major.level
FROM students
JOIN major ON students.snum = major.snum
WHERE students.name = 'Gail';