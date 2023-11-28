CREATE TABLE subject_category
(
    id         bigserial                   NOT NULL,
    created    timestamp without time zone NOT NULL,
    label      character varying(100)      NOT NULL,
    valid_from date                        NOT NULL,
    valid_to   date,
    item_order integer                     NOT NULL,
    CONSTRAINT subject_category_pkey PRIMARY KEY (id),
    CONSTRAINT subject_category_item_order_uc UNIQUE (item_order) DEFERRABLE INITIALLY DEFERRED
);

CREATE INDEX IF NOT EXISTS idx_subject_category_label ON subject_category USING btree (label);