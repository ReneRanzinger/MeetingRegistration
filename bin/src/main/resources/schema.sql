DROP SCHEMA IF EXISTS registration CASCADE;

CREATE SCHEMA IF NOT EXISTS registration AUTHORIZATION registration;

CREATE SEQUENCE IF NOT EXISTS conference_seq MINVALUE 1 START 1;
CREATE SEQUENCE IF NOT EXISTS fee_seq MINVALUE 1 START 1;
CREATE SEQUENCE IF NOT EXISTS participant_seq MINVALUE 1 START 1;
CREATE SEQUENCE IF NOT EXISTS promotion_code_seq MINVALUE 1 START 1;

CREATE TABLE IF NOT EXISTS registration.conference (
  conference_id bigint NOT NULL PRIMARY KEY,
  conference_code varchar(32) NOT NULL,
  conference_name varchar(256) NOT NULL,
  registration_start timestamp without time zone NOT NULL,
  registration_end timestamp without time zone NOT NULL,
  abstract_start timestamp without time zone NOT NULL,
  abstract_end timestamp without time zone NOT NULL,
  post_registration_code varchar(32) NOT NULL,
  confirmation_email text,
  short_talks boolean
);

CREATE TABLE IF NOT EXISTS registration.fee (
  fee_id bigint DEFAULT nextval('fee_seq') NOT NULL PRIMARY KEY,
  conference_id bigint REFERENCES registration.conference(conference_id), 
  name varchar(64) NOT NULL,
  amount double precision
);

CREATE TABLE IF NOT EXISTS registration.participant (
  participant_id bigint DEFAULT nextval('participant_seq') NOT NULL PRIMARY KEY,
  conference_id bigint NOT NULL REFERENCES registration.conference(conference_id),
  first_name varchar(128) NOT NULL,
  middle_name varchar(128),  
  last_name varchar(128) NOT NULL,
  department varchar(128) NOT NULL,
  institution varchar(256) NOT NULL,
  email varchar(128) NOT NULL,
  address text,
  phone varchar(128) NOT NULL,
  title varchar(128) NOT NULL,
  profession varchar(128) NOT NULL,
  promotion_code varchar(32) NOT NULL,
  fee_id bigint NOT NULL REFERENCES registration.fee(fee_id),
  comment text,
  registration_time timestamp without time zone NOT NULL,
  payed boolean NOT NULL,
  abstract_title varchar(1024),
  abstract bytea,
  diet varchar(124) NOT NULL,
  abstract_filename varchar(10000),
  consider_talk boolean
);  


CREATE TABLE IF NOT EXISTS registration.promotion_code (
  promotion_code_id bigint DEFAULT nextval('promotion_code_seq') NOT NULL PRIMARY KEY,
  conference_id bigint NOT NULL REFERENCES registration.conference(conference_id),
  code varchar(32) NOT NULL,
  description varchar(1024) NOT NULL
);

CREATE TABLE IF NOT EXISTS registration.configuration (
  name varchar(256),
  value varchar(256)
);

CREATE TABLE IF NOT EXISTS registration.conference_promocode (
  conference_id bigint NOT NULL REFERENCES registration.conference(conference_id),
  promotion_code_id bigint NOT NULL REFERENCES registration.promotion_code(promotion_code_id),
  PRIMARY KEY (conference_id,promotion_code_id)
);

