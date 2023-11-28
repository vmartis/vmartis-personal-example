INSERT INTO "user" (id, created, active, email, firstname, surname, password) VALUES (3, '2019-11-03 10:33:00.000000', true, 'pracovnik@smartbrains.cz', 'Ján', 'Smrek', '$2a$10$RV/4Mi0Nx0RoPkWH5zaGKebZiNThSqzOi2jRZvrKSjT1O9jcwi7Ia');

SELECT pg_catalog.setval('user_id_seq', 3, true);

INSERT INTO user_permission (user_id, permission) VALUES (3, 'WORK_RECORD_PERSONAL');