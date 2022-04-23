ALTER TABLE users ADD COLUMN IF NOT EXISTS username VARCHAR(64) UNIQUE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS password VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS created_date DATE;

ALTER TABLE debates ADD COLUMN IF NOT EXISTS created_date timestamp;

ALTER TABLE posts ADD COLUMN IF NOT EXISTS created_date timestamp;

CREATE TABLE IF NOT EXISTS suscribed
(
    userid INTEGER NOT NULL REFERENCES users,
    debateid INTEGER NOT NULL REFERENCES debates,
    PRIMARY KEY (userid, debateid)
);

CREATE TABLE IF NOT EXISTS likes
(
    userid INTEGER NOT NULL REFERENCES users,
    postid INTEGER NOT NULL REFERENCES posts,
    PRIMARY KEY (userid, postid)
);

CREATE OR REPLACE VIEW public_posts AS
    SELECT posts.postid, posts.debateid, posts.userid, posts.content, COUNT(likes.userid) AS likes, posts.created_date
    FROM posts LEFT JOIN likes ON posts.postid = likes.postid
    GROUP BY posts.postid, posts.debateid, posts.userid, posts.content;