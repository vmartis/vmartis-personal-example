CREATE TABLE IF NOT EXISTS company
(
    id              bigserial              NOT NULL,
    name            character varying(100) NOT NULL,
    company_id      character varying(8)   NOT NULL,
    role            character varying(10)  NOT NULL,
    vat_id          character varying(12),
    vat_id_local    character varying(12),
    defaul_currency character varying(3),
    web             character varying(100),
    active          boolean DEFAULT true   NOT NULL,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS company_role_idx ON company USING btree (role);
CREATE INDEX IF NOT EXISTS company_name_idx ON company USING btree (name);
-- UC for ensuring only one instance of owned company
CREATE UNIQUE INDEX company_role_owned_uc ON company (role) WHERE (role = 'OWNED');
