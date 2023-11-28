ALTER TABLE "user" ADD COLUMN updated timestamp without time zone;
ALTER TABLE subject ADD COLUMN updated timestamp without time zone;
ALTER TABLE subject_category ADD COLUMN updated timestamp without time zone;
ALTER TABLE region ADD COLUMN updated timestamp without time zone;
ALTER TABLE money_movement_category ADD COLUMN updated timestamp without time zone;