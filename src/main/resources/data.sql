INSERT INTO registration.configuration (name, value)
SELECT * FROM (SELECT 'server.email', 'glycomic@uga.edu') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM registration.configuration WHERE name = 'server.email'
) LIMIT 1;

INSERT INTO registration.configuration (name, value)
SELECT * FROM (SELECT 'server.email.password', 'miOeR$5q1Y') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM registration.configuration WHERE name = 'server.email.password'
) LIMIT 1;



