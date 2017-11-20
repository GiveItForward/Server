#!/usr/bin/env bash

su -postgres;

CREATE TABLE Organizations (
  oid INTEGER NOT NULL PRIMARY KEY,
  name text NOT NULL,
  email text NOT NULL,
  website text NOT NULL,
  phone_number text NOT NULL,
);

CREATE TABLE Users (
  uid integer PRIMARY KEY NOT NULL,
  email text NOT NULL,
  password text NOT NULL,
  isAdmin BOOLEAN NOT NULL,
  oid integer REFERENCES Organizations(oid),
  photo text NOT NULL,
  bio text
);

CREATE TABLE Tags (
  tid INTEGER PRIMARY KEY NOT NULL,
  tag text NOT NULL
);

CREATE TABLE User_Tag_Pairs (
  uid INTEGER REFERENCES users(uid) NOT NULL,
  tid INTEGER REFERENCES tags(tid) NOT NULL,
  time_limit TIME,
  verified_by INTEGER REFERENCES users(uid),
  PRIMARY KEY (uid,tid)
);

CREATE TABLE Elevated_User (
  uid INTEGER PRIMARY KEY NOT NULL REFERENCES users(uid),
  first_name text NOT NULL,
  last_name text NOT NULL,
  phone_num text NOT NULL
);

CREATE TABLE Notifications (
  nid INTEGER PRIMARY KEY NOT NULL,
  date TIMESTAMP NOT NULL,
  message text NOT NULL,
  uid INTEGER NOT NULL REFERENCES users(uid)
);

CREATE TABLE Requests (
  rid INTEGER PRIMARY KEY NOT NULL,
  description text NOT NULL,
  amount FLOAT NOT NULL,
  image text
);

CREATE TABLE User_Request_Pairs (
  uid_request INTEGER REFERENCES users(uid) NOT NULL,
  rid INTEGER REFERENCES requests(rid) NOT NULL,
  uid_donate INTEGER REFERENCES users(uid),
  PRIMARY KEY (uid_request,rid)
);

CREATE TABLE Thank_Yous (
  rid INTEGER PRIMARY KEY REFERENCES requests(rid) NOT NULL,
  note text NOT NULL,
  image text
);
