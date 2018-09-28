insert into user(id, username, password, active, email, date)
values (1, 'admin', 'admin', true, 'ivan.veshtard@gmail.com', '2018-09-12 00:00:00');

insert into user_role
values (1, 'USER'), (1, 'ADMIN');