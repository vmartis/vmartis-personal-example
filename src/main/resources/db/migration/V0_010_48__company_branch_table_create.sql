CREATE TABLE IF NOT EXISTS company_branch
(
    id                   bigserial                   NOT NULL,
    name                 character varying(100)      NOT NULL,
    address_city         character varying(50)       NOT NULL,
    address_country      character varying(3)        NOT NULL,
    address_house_number character varying(10)       NOT NULL,
    address_street_name  character varying(50)       NOT NULL,
    address_zip_code     character varying(10)       NOT NULL,
    address_region_id    bigint                      NOT NULL,
    company_id           bigint                      NOT NULL,
    created              timestamp without time zone NOT NULL,
    updated              timestamp without time zone,
    CONSTRAINT company_branch_pkey PRIMARY KEY (id),
    CONSTRAINT company_branch_company_fk FOREIGN KEY (company_id) REFERENCES company (id),
    CONSTRAINT company_branch_address_region_fk FOREIGN KEY (address_region_id) REFERENCES region (id)
);

CREATE INDEX IF NOT EXISTS company_branch_name_idx ON company_branch USING btree (name);

