DROP INDEX subject_role_idx;
ALTER TABLE subject
    DROP COLUMN role;
ALTER TABLE subject
    ADD COLUMN address_city character varying(50) NOT NULL;
ALTER TABLE subject
    ADD COLUMN address_country character varying(3) NOT NULL;
ALTER TABLE subject
    ADD COLUMN address_house_number character varying(10) NOT NULL;
ALTER TABLE subject
    ADD COLUMN address_street_name character varying(50) NOT NULL;
ALTER TABLE subject
    ADD COLUMN address_zip_code character varying(10) NOT NULL;
ALTER TABLE subject
    ADD COLUMN address_region_id bigint;
ALTER TABLE subject
    ADD CONSTRAINT subject_address_region_fk FOREIGN KEY (address_region_id)
        REFERENCES region (id);