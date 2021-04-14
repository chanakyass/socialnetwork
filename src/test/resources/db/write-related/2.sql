--delete from user_role;
--delete from role;
delete from users;
delete from posts;
delete from comments;
delete from likes_on_posts;
delete from likes_on_comments;

ALTER SEQUENCE user_sequence RESTART WITH 1;
ALTER SEQUENCE post_sequence RESTART WITH 1;
ALTER SEQUENCE comment_sequence RESTART WITH 1;
ALTER SEQUENCE like_post_sequence RESTART WITH 1;
ALTER SEQUENCE like_comment_sequence RESTART WITH 1;