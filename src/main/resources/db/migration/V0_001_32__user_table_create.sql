CREATE TABLE IF NOT EXISTS "user"
(
    id             bigserial                   NOT NULL,
    active         boolean                     NOT NULL,
    email          character varying(50)       NOT NULL,
    firstname      character varying(100)      NOT NULL,
    password       character varying(250)      NOT NULL,
    surname        character varying(100)      NOT NULL,
    created        timestamp without time zone NOT NULL,
    last_logged_in timestamp without time zone,
    CONSTRAINT user_pkey PRIMARY KEY (id),
    CONSTRAINT user_email_uc UNIQUE (email) DEFERRABLE INITIALLY DEFERRED
);

CREATE INDEX IF NOT EXISTS user_email_idx ON "user" USING btree (email);
CREATE INDEX IF NOT EXISTS user_firstname_idx ON "user" USING btree (firstname);

CREATE TABLE IF NOT EXISTS "user_permission"
(
    user_id bigint                NOT NULL,
    permission    character varying(30) NOT NULL,
    CONSTRAINT user_permission_user_fk FOREIGN KEY (user_id) REFERENCES "user" (id)
);
