create table message_notifications(
user_id bigint not null references user,
message_id bigint not null references message,
primary key (user_id, message_id)
)