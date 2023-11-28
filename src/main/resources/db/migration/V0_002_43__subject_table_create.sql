CREATE TABLE IF NOT EXISTS subject
(
    id      bigserial                   NOT NULL,
    role    character varying(10)       NOT NULL,
    created timestamp without time zone NOT NULL,
    active  boolean DEFAULT true        NOT NULL,
    CONSTRAINT subject_pkey PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS subject_role_idx ON subject USING btree (role);
