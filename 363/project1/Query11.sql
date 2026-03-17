SELECT COUNT(DISTINCT students.snum) AS female_count
FROM students
LEFT JOIN major ON students.snum = major.snum
LEFT JOIN minor ON students.snum = minor.snum
JOIN degrees ON (major.name = degrees.name AND major.level = degrees.level)
  OR (minor.name = degrees.name AND minor.level = degrees.level)
WHERE (degrees.name = 'Software Engineering') AND students.gender = 'F';