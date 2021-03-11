

insert into users(id, name, email, password) values (nextval('user_sequence'), 'test', 'test@rest.com', 'pass');
insert into users(id, name, email, password) values (nextval('user_sequence'), 'chan', 'chan@rest.com', 'pass');
insert into users(id, name, email, password) values (nextval('user_sequence'), 'whataview', 'whataview@rest.com', 'pass');


insert into posts(id, post_heading, post_body, no_of_likes, posted_on_date, owned_by_user)
values(nextval('post_sequence'), 'post heading', 'post body', 0, '2020-02-05', 3) ;

insert into posts(id, post_heading, post_body, no_of_likes, posted_on_date, owned_by_user)
values(nextval('post_sequence'), 'post heading 2', 'post body 2', 0, '2020-02-05', 3) ;

insert into posts(id, post_heading, post_body, no_of_likes, posted_on_date, owned_by_user)
values(nextval('post_sequence'), 'post heading 3', 'post body 3', 0, '2020-02-05', 3) ;

insert into posts(id, post_heading, post_body, no_of_likes, posted_on_date, owned_by_user)
values(nextval('post_sequence'), 'post heading 4', 'post body 4', 0, '2020-02-05', 3) ;


insert into comments(id, comment_content, commented_on_date, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is comment existing', '2020-02-05', 1, 1, null );
insert into comments(id, comment_content, commented_on_date, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is comment existing 2', '2020-02-05', 1, 2, null );
insert into comments(id, comment_content, commented_on_date, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is comment existing 3', '2020-02-05', 1, 1, null );
insert into comments(id, comment_content, commented_on_date, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is reply existing on comment 1', '2020-02-05', 1, 1, 1 );
insert into comments(id, comment_content, commented_on_date, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is reply existing on comment 1', '2020-02-05', 1, 2, 1 );
insert into comments(id, comment_content, commented_on_date, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is reply existing on comment 1', '2020-02-05', 1, 1, 1 );
