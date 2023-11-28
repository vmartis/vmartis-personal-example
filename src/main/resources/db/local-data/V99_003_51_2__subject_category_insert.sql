INSERT INTO subject_category (id, created, label, valid_from, valid_to, item_order) VALUES (1, '2019-07-27 18:09:07.814000', 'Firma', '2019-06-01', null, 1);
INSERT INTO subject_category (id, created, label, valid_from, valid_to, item_order) VALUES (2, '2019-07-27 18:09:07.814000', 'Obchod', '2019-06-01', null, 2);
INSERT INTO subject_category (id, created, label, valid_from, valid_to, item_order) VALUES (3, '2019-07-27 18:09:07.814000', 'Dodávateľ - zelenina', '2019-06-01', null, 3);
INSERT INTO subject_category (id, created, label, valid_from, valid_to, item_order) VALUES (4, '2019-07-27 18:09:07.814000', 'Dodávateľ - drogéria', '2019-06-01', null, 4);
INSERT INTO subject_category (id, created, label, valid_from, valid_to, item_order) VALUES (5, '2019-07-27 18:09:07.814000', 'Dodávateľ - IT', '2019-06-01', null, 5);

SELECT pg_catalog.setval('subject_category_id_seq', 5, true);