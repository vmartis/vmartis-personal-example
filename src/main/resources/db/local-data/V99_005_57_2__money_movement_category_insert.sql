INSERT INTO money_movement_category (id, created, label, valid_from, valid_to, item_order, type) VALUES (1, '2019-07-27 18:09:07.814000', 'Tržba', '2019-06-01', null, 1, 'INCOME');
INSERT INTO money_movement_category (id, created, label, valid_from, valid_to, item_order, type) VALUES (2, '2019-07-27 18:09:07.814000', 'Iné príjmy', '2019-06-01', null, 2, 'INCOME');
INSERT INTO money_movement_category (id, created, label, valid_from, valid_to, item_order, type) VALUES (3, '2019-07-27 18:09:07.814000', 'Občerstvenie', '2019-06-01', null, 3, 'OUTCOME');
INSERT INTO money_movement_category (id, created, label, valid_from, valid_to, item_order, type) VALUES (4, '2019-07-27 18:09:07.814000', 'Zelnina', '2019-06-01', null, 4, 'OUTCOME');
INSERT INTO money_movement_category (id, created, label, valid_from, valid_to, item_order, type) VALUES (5, '2019-07-27 18:09:07.814000', 'Plyn', '2019-06-01', null, 5, 'OUTCOME');

SELECT pg_catalog.setval('money_movement_category_id_seq', 5, true);