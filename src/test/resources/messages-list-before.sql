delete from message;
insert into message (id, text, tag, date, user_id) values
(1, 'first', 'my-tag', '2018-08-29 15:06:56', 1),
(2, 'second', 'more', '2018-08-29 15:16:56', 1),
(3, 'third', 'my-tag', '2018-08-29 15:26:56', 1),
(4, 'fourth', 'another', '2018-08-29 15:36:56', 2);

alter table message AUTO_INCREMENT=10;
