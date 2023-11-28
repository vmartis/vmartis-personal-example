INSERT INTO contact (id, name, email, phone_number, "position", note, subject_id, created, active) VALUES (1, 'Adam Šangala', 'asangala@example-xxx.com', '+420 000 000 000', 'Predajca', 'Obchod', 2, current_timestamp, true);
INSERT INTO contact (id, name, email, phone_number, "position", note, subject_id, created, active) VALUES (2, 'Jan Kratochvíl', 'jkratochvil@example-xxx.com', '+420 111 111 111', 'Účtovník', 'Faktury', 2, current_timestamp, true);
INSERT INTO contact (id, name, email, phone_number, "position", note, subject_id, created, active) VALUES (3, 'Ondrej Malý', 'omaly@finito-c3.com', '+420 333 222 123', 'Manager', null, 2, current_timestamp, true);

SELECT pg_catalog.setval('contact_id_seq', 3, true);
