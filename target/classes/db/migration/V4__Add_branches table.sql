create table branch (
id bigint not null auto_increment,
dialog bit not null,
name varchar(100) unique,
description varchar(255),
primary key (id))
engine=MyISAM;