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

INSERT INTO registration.admin (username, password)
SELECT * FROM (SELECT 'admin', '{noop}password') AS tmp
WHERE NOT EXISTS (
    SELECT username FROM registration.admin WHERE username = 'admin'
) LIMIT 1;


