SELECT courses.number, courses.name
FROM courses
JOIN departments ON courses.department_code = departments.code
WHERE departments.name = 'Computer Science'
ORDER BY courses.number;
