delete from user_role;
delete from user;

insert into user(id, active, password, username) values
(1, true, '{fSkaQXjHR8fgFpB8dQTZcr7dEgK1NDWIVTd58p70D6o=}9f862d0b253a07032760a090beb5124c', 'petya'),
(2, true, '{uU3ie+OQq39wREmeDuKqk6FR6lHwmYn3n5Yc9e4nLP0=}80219d23367f178a2ec136efe1bef6cb', 'ivan');

insert into user_role (user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');