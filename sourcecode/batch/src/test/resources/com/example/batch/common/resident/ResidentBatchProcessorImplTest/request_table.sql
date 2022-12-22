create table if not exists test_requests (
	id integer primary key,
	status varchar(2) not null,
	requested_at timestamp not null
);

create table if not exists test_string_requests (
	string_id varchar(20) primary key,
	string_status varchar(2) not null,
	string_requested_at timestamp not null
);
