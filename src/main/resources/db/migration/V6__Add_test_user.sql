insert into user(id, username, password, active, email, date)
values (2, 'ivan', 'ivan', true, 'ivan.veshtard@gmail.com', '2018-01-01 00:00:00'),
(3, 'petr', 'petr', true, 'ivan.veshtard@gmail.com', '2018-01-01 00:00:00');

insert into user_role
values (2, 'USER'), (3, 'USER');

update user set password =  MD5(password);