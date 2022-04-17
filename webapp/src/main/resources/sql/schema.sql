CREATE TABLE IF NOT EXISTS users
(
    userid SERIAL PRIMARY KEY,
    email varchar(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS debates
(
    debateid SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS posts
(
    postid SERIAL PRIMARY KEY,
    debateid INTEGER NOT NULL REFERENCES debates,
    userid INTEGER REFERENCES users NOT NULL,
    content TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS likes
(
    userid INTEGER NOT NULL REFERENCES users,
    postid INTEGER NOT NULL REFERENCES posts
);

CREATE OR REPLACE VIEW posts_with_likes AS
    SELECT posts.postid, posts.debateid, posts.userid, posts.content, COUNT(likes.userid) AS likes
    FROM posts
    LEFT JOIN likes ON posts.postid = likes.postid
    GROUP BY posts.postid, posts.debateid, posts.userid, posts.content;