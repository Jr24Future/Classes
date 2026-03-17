SELECT courses.number, courses.name, COUNT(register.snum) AS student_count
FROM courses
LEFT JOIN register ON courses.number = register.course_number
GROUP BY courses.number, courses.name
ORDER BY courses.number;