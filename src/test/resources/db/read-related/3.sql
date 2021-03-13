

insert into users(id, name, email, password) values (nextval('user_sequence'), 'test', 'test@rest.com', 'pass');
insert into users(id, name, email, password) values (nextval('user_sequence'), 'chan', 'chan@rest.com', 'pass');
insert into users(id, name, email, password) values (nextval('user_sequence'), 'whataview', 'whataview@rest.com', 'pass');


insert into posts(id, post_heading, post_body, no_of_likes, posted_at_time, owned_by_user)
values(nextval('post_sequence'), 'post heading', 'post body', 0, TIMESTAMP '2020-02-05 10:35:00', 3) ;

insert into posts(id, post_heading, post_body, no_of_likes, posted_at_time, owned_by_user)
values(nextval('post_sequence'), 'post heading 2', 'post body 2', 0, TIMESTAMP '2020-02-05 10:35:00', 3) ;

insert into posts(id, post_heading, post_body, no_of_likes, posted_at_time, owned_by_user)
values(nextval('post_sequence'), 'post heading 3', 'post body 3', 0, TIMESTAMP '2020-02-05 10:35:00', 3) ;

insert into posts(id, post_heading, post_body, no_of_likes, posted_at_time, owned_by_user)
values(nextval('post_sequence'), 'post heading 4', 'post body 4', 0, TIMESTAMP '2020-02-05 10:35:00', 3) ;


insert into comments(id, comment_content, commented_at_time, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is comment existing', TIMESTAMP '2020-02-05 10:35:00', 1, 1, null );
insert into comments(id, comment_content, commented_at_time, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is comment existing 2', TIMESTAMP '2020-02-05 10:35:00', 1, 2, null );
insert into comments(id, comment_content, commented_at_time, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is comment existing 3', TIMESTAMP '2020-02-05 10:35:00', 1, 1, null );
insert into comments(id, comment_content, commented_at_time, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is reply existing on comment 1', TIMESTAMP '2020-02-05 10:35:00', 1, 1, 1 );
insert into comments(id, comment_content, commented_at_time, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is reply existing on comment 1', TIMESTAMP '2020-02-05 10:35:00', 1, 2, 1 );
insert into comments(id, comment_content, commented_at_time, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is reply existing on comment 1', TIMESTAMP '2020-02-05 10:35:00', 1, 1, 1 );

insert into likes_on_posts(id, liked_at_time, liked_post, owned_by_user)
values (nextval('like_post_sequence'), TIMESTAMP '2020-02-05 10:35:00', 1, 1);
insert into likes_on_posts(id, liked_at_time, liked_post, owned_by_user)
values (nextval('like_post_sequence'), TIMESTAMP '2020-02-05 10:35:00', 1, 2);
insert into likes_on_posts(id, liked_at_time, liked_post, owned_by_user)
values (nextval('like_post_sequence'), TIMESTAMP '2020-02-05 10:35:00', 1, 3);


insert into likes_on_comments(id, liked_at_time, liked_comment, owned_by_user)
values (nextval('like_comment_sequence'), TIMESTAMP '2020-02-05 10:35:00', 1, 1);
insert into likes_on_comments(id, liked_at_time, liked_comment, owned_by_user)
values (nextval('like_comment_sequence'), TIMESTAMP '2020-02-05 10:35:00', 2, 2);
insert into likes_on_comments(id, liked_at_time, liked_comment, owned_by_user)
values (nextval('like_comment_sequence'), TIMESTAMP '2020-02-05 10:35:00', 1, 3);
