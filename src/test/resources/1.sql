insert into users(id, name, email, password) values (nextval('user_sequence'), 'test', 'test@rest.com', 'pass');
insert into users(id, name, email, password) values (nextval('user_sequence'), 'chan', 'chan@rest.com', 'pass');

insert into posts(id, post_heading, post_body, no_of_likes, posted_on_date, owned_by_user) values(nextval('post_sequence'), 'post heading', 'post body', 0, '2020-02-05', 1);