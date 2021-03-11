delete from users;
delete from posts;
delete from comments;

ALTER SEQUENCE user_sequence RESTART;
ALTER SEQUENCE post_sequence RESTART;
ALTER SEQUENCE comment_sequence RESTART;