SELECT degrees.name, degrees.level
FROM degrees
JOIN departments ON degrees.department_code = departments.code
WHERE departments.name = 'Computer Science'
ORDER BY degrees.level;