-- DROP SCHEMA IF EXISTS registration CASCADE;

CREATE SCHEMA IF NOT EXISTS registration AUTHORIZATION registration;

CREATE SEQUENCE IF NOT EXISTS conference_seq MINVALUE 1 START 1;
CREATE SEQUENCE IF NOT EXISTS fee_seq MINVALUE 1 START 1;
CREATE SEQUENCE IF NOT EXISTS participant_seq MINVALUE 550011 START 550011;
CREATE SEQUENCE IF NOT EXISTS promotion_code_seq MINVALUE 1 START 1;

CREATE TABLE IF NOT EXISTS registration.conference (
  conference_id bigint DEFAULT nextval('conference_seq') NOT NULL PRIMARY KEY,
  conference_code varchar(32) NOT NULL,
  conference_name varchar(256) NOT NULL,
  registration_start timestamp without time zone NOT NULL,
  registration_end timestamp without time zone NOT NULL,
  abstract_start timestamp without time zone NOT NULL,
  abstract_end timestamp without time zone NOT NULL,
  post_registration_code varchar(32),
  email_list	text,
  confirmation_email text NOT NULL,
  short_talks boolean
);

CREATE TABLE IF NOT EXISTS registration.fee (
  fee_id bigint DEFAULT nextval('fee_seq') NOT NULL PRIMARY KEY,
  conference_id bigint REFERENCES registration.conference(conference_id), 
  name varchar(64) NOT NULL,
  amount double precision NOT NULL,
  UNIQUE (name,conference_id)
);

CREATE TABLE IF NOT EXISTS registration.participant (
  participant_id bigint DEFAULT nextval('participant_seq') NOT NULL PRIMARY KEY,
  conference_id bigint NOT NULL REFERENCES registration.conference(conference_id),
  title varchar(128) NOT NULL,
  first_name varchar(128) NOT NULL,
  middle_name varchar(128),  
  last_name varchar(128) NOT NULL,
  department varchar(128) NOT NULL,
  institution varchar(256) NOT NULL,
  email varchar(128) NOT NULL,
  address text,
  phone varchar(128) NOT NULL,
  profession varchar(128) NOT NULL,
  promotion_code varchar(32),
  fee_id bigint NOT NULL REFERENCES registration.fee(fee_id),
  diet varchar(124),
  comment text,
  registration_time timestamp without time zone NOT NULL,
  payed boolean NOT NULL,
  abstract_title varchar(1024),
  abstract bytea,
  abstract_filename varchar(10000),
  consider_talk boolean,
  UNIQUE (email,conference_id)
);  


CREATE TABLE IF NOT EXISTS registration.promotion_code (
  promotion_code_id bigint DEFAULT nextval('promotion_code_seq') NOT NULL PRIMARY KEY,
  conference_id bigint NOT NULL REFERENCES registration.conference(conference_id),
  code varchar(32) NOT NULL,
  description varchar(1024) NOT NULL,
  UNIQUE (promotion_code_id,conference_id)
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

