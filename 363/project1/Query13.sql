SELECT degrees.name, degrees.level, COUNT(DISTINCT students.snum) AS student_count
FROM degrees
LEFT JOIN major ON degrees.name = major.name AND degrees.level = major.level
LEFT JOIN minor ON degrees.name = minor.name AND degrees.level = minor.level
LEFT JOIN students ON students.snum = major.snum OR students.snum = minor.snum
GROUP BY degrees.name, degrees.level
ORDER BY student_count DESC, degrees.name
LIMIT 1;
