CREATE TABLE region
(
    id         bigserial                   NOT NULL,
    created    timestamp without time zone NOT NULL,
    label      character varying(100)      NOT NULL,
    valid_from date                        NOT NULL,
    valid_to   date,
    item_order integer                     NOT NULL,
    CONSTRAINT region_pkey PRIMARY KEY (id),
    CONSTRAINT region_item_order_uc UNIQUE (item_order) DEFERRABLE INITIALLY DEFERRED
);

CREATE INDEX IF NOT EXISTS idx_region_label ON region USING btree (label);