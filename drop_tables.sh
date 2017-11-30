#!/usr/bin/env bash

su -postgres;

drop table Organizations;
drop table Users;
drop table Tags;
drop table User_Tag_Pairs;
drop table Elevated_User;
drop table Notifications;
drop table Requests;
drop table User_Request_Pairs;
drop table Thank_Yous;
