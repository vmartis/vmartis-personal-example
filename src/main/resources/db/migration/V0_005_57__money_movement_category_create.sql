CREATE TABLE money_movement_category
(
    id         bigserial                   NOT NULL,
    created    timestamp without time zone NOT NULL,
    label      character varying(100)      NOT NULL,
    valid_from date                        NOT NULL,
    valid_to   date,
    item_order integer                     NOT NULL,
    type       character varying(20)       NOT NULL,
    CONSTRAINT money_movement_category_pkey PRIMARY KEY (id),
    CONSTRAINT money_movement_category_item_order_uc UNIQUE (item_order) DEFERRABLE INITIALLY DEFERRED
);

CREATE INDEX IF NOT EXISTS idx_money_movement_category_label ON money_movement_category USING btree (label);
