insert into users(id, name, email, password) values (nextval('user_sequence'), 'test', 'test@rest.com', 'pass');
insert into users(id, name, email, password) values (nextval('user_sequence'), 'chan', 'chan@rest.com', 'pass');
insert into users(id, name, email, password) values (nextval('user_sequence'), 'whataview', 'whataview@rest.com', 'pass');

insert into posts(id, post_heading, post_body, no_of_likes, posted_at_time, owned_by_user)
values(nextval('post_sequence'), 'post heading', 'post body', 0, TIMESTAMP '2020-02-05 10:35:00', 1) ;

insert into secure_resource(id, type, post_id, comment_id, like_post_id, like_comment_id, owner_id)
values(nextval('secure_resource_seq'), 'P', 1, null, null, null, 1) ;

insert into comments(id, comment_content, commented_at_time, commented_on_post, owned_by_user, parent_comment)
values (nextval('comment_sequence'), 'This is an existing comment', TIMESTAMP '2020-02-05 10:35:00', 1, 1, null );

insert into secure_resource(id, type, post_id, comment_id, like_post_id, like_comment_id, owner_id)
values(nextval('secure_resource_seq'), 'C', null, 1, null, null, 1) ;

insert into likes_on_posts(id, liked_at_time, liked_post, owned_by_user)
values (nextval('like_post_sequence'), TIMESTAMP '2020-02-05 10:35:00', 1, 1);

insert into secure_resource(id, type, post_id, comment_id, like_post_id, like_comment_id, owner_id)
values(nextval('secure_resource_seq'), 'P', null, null, 1, null, 1) ;

insert into likes_on_comments(id, liked_at_time, liked_comment, owned_by_user)
values (nextval('like_comment_sequence'), TIMESTAMP '2020-02-05 10:35:00', 1, 1);

insert into secure_resource(id, type, post_id, comment_id, like_post_id, like_comment_id, owner_id)
values(nextval('secure_resource_seq'), 'P', null, null, null, 1, 1) ;