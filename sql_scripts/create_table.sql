create table data._user 
(
	user_id text primary key,
	telegram_id bigint not null UNIQUE,
	enable_notification boolean not null,
	password int
);

create table data._message
(
	user_id text references data._user (user_id) on delete cascade,
	message_id serial,
	message_text text,
	notification_type integer,
	start_time timestamp not null,
	end_time timestamp,
	minute_period integer,
	primary key(user_id, message_id)
);

create table data._mail
(
	user_id bigint references data._user (telegram_id) on delete cascade,
	mail_id serial primary key,
	login text,
	password text
);

create table data._mail_ckecker
(
	mail_id integer primary key references data._mail (mail_id) on delete cascade,
	checker_type integer,
	filter_text text,
	notification_type integer	
);

create table data._task
(
	chat_id text,
	task_id bigint,
	person_task text not null,
	name_task text not null,
	start_time timestamp not null,
	end_time timestamp not null,
	minute_period integer,
	completed boolean,
	primary key (chat_id, task_id)
);

create table data.list (
	list_id serial primary key,
	list_description text not null,
	owner_id bigint references data._user(telegram_id) on delete cascade
);

create unique index list_id_index on data.list(list_id); 

create table data.share_list (
	list_id integer references data.list(list_id) on delete cascade,
	user_id bigint references data._user(telegram_id) on delete cascade,
	primary key (list_id, user_id)
);

create table data.list_item (
	item_id serial,
	list_id integer,
	deleted boolean,
	title text,
	details text,
	primary key (item_id, list_id)
)