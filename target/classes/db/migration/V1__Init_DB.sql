create table message (
id bigint not null auto_increment,
filename varchar(255),
tag varchar(255),
text varchar(2048),
date timestamp,
user_id bigint,
branch_id bigint,
answer_message_id bigint,
dialog bit not null,
deleted bit not null,
primary key (id))
engine=MyISAM;

create table user (
id bigint not null auto_increment,
avatar_filename varchar(255),
activation_code varchar(255),
active bit not null,
date timestamp,
last_visit timestamp,
email varchar(255),
password varchar(255),
username varchar(255), primary key (id))
engine=MyISAM;

create table user_role (
user_id bigint not null,
roles varchar(255))
engine=MyISAM;

alter table message
add constraint message_user_fk
foreign key (user_id) references user (id);

alter table user_role
add constraint user_role_user_fk
foreign key (user_id) references user (id)
