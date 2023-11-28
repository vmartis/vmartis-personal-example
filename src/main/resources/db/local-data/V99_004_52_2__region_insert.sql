INSERT INTO region (id, created, label, valid_from, valid_to, item_order) VALUES (1, '2019-07-27 18:09:07.814000', 'Trenčín a okolie', '2019-06-01', null, 1);
INSERT INTO region (id, created, label, valid_from, valid_to, item_order) VALUES (2, '2019-07-27 18:09:07.814000', 'Ilava a okolie', '2019-06-01', null, 2);
INSERT INTO region (id, created, label, valid_from, valid_to, item_order) VALUES (3, '2019-07-27 18:09:07.814000', 'Brno', '2019-06-01', null, 3);
INSERT INTO region (id, created, label, valid_from, valid_to, item_order) VALUES (4, '2019-07-27 18:09:07.814000', 'Bratislava', '2019-06-01', null, 4);
INSERT INTO region (id, created, label, valid_from, valid_to, item_order) VALUES (5, '2019-07-27 18:09:07.814000', 'Košice', '2019-06-01', null, 5);
INSERT INTO region (id, created, label, valid_from, valid_to, item_order) VALUES (6, '2019-07-27 18:09:07.814000', 'Liberec a okoli', '2019-06-01', null, 6);
INSERT INTO region (id, created, label, valid_from, valid_to, item_order) VALUES (7, '2019-07-27 18:09:07.814000', 'Praha', '2019-06-01', null, 7);

SELECT pg_catalog.setval('region_id_seq', 7, true);