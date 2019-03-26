INSERT INTO registration.configuration (name, value)
SELECT * FROM (SELECT 'server.email', 'ccrcugaconfadm@gmail.com') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM registration.configuration WHERE name = 'server.email'
) LIMIT 1;

INSERT INTO registration.configuration (name, value)
SELECT * FROM (SELECT 'server.email.password', '1Qs8gZpyS/SQZp7L/7noLFw7ErIGvUCN') AS tmp
WHERE NOT EXISTS (
    SELECT name FROM registration.configuration WHERE name = 'server.email.password'
) LIMIT 1;



