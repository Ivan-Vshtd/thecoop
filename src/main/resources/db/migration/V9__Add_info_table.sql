create table users_info (
id bigint not null auto_increment,
user_id bigint unique,
location varchar(255),
birthday timestamp,
primary key (id))
engine=MyISAM;

insert into users_info(id, user_id, location, birthday)
values (1, 1, 'here', '1971-01-01 00:00:00'),
(2, 2, 'here', '1971-01-01 00:00:00'),
(3, 3, 'here', '1971-01-01 00:00:00');