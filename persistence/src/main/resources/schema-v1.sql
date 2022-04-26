ALTER TABLE users ADD COLUMN IF NOT EXISTS username VARCHAR(64) UNIQUE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS password VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS created_date DATE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS role INTEGER;

ALTER TABLE debates ADD COLUMN IF NOT EXISTS created_date timestamp;
ALTER TABLE debates ADD COLUMN IF NOT EXISTS category INTEGER;
ALTER TABLE debates ADD COLUMN IF NOT EXISTS creatorid INTEGER REFERENCES users(userid);
ALTER TABLE debates ADD COLUMN IF NOT EXISTS opponentid INTEGER REFERENCES users(userid);

ALTER TABLE posts ADD COLUMN IF NOT EXISTS created_date timestamp;

CREATE TABLE IF NOT EXISTS images
(
    imageid SERIAL PRIMARY KEY,
    data BYTEA NOT NULL
);

ALTER TABLE posts ADD COLUMN IF NOT EXISTS imageid INTEGER REFERENCES images(imageid) ON DELETE SET NULL;

ALTER TABLE users ADD COLUMN IF NOT EXISTS imageid INTEGER REFERENCES images(imageid) ON DELETE SET NULL;

ALTER TABLE debates ADD COLUMN IF NOT EXISTS imageid INTEGER REFERENCES images(imageid) ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS subscribed
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
    SELECT posts.postid, users.username, posts.debateid, posts.content, COUNT(likes.userid) AS likes, posts.created_date, posts.imageid
    FROM posts LEFT JOIN likes ON posts.postid = likes.postid LEFT JOIN users ON posts.userid = users.userid
    GROUP BY posts.postid, users.username;

CREATE OR REPLACE VIEW public_debates AS
    SELECT debates.debateid, debates.name, debates.description, debates.category, debates.created_date, debates.imageid, u1.username AS creatorusername, u2.username AS opponentusername, COUNT(DISTINCT s.userid) AS subscribedcount
    FROM debates LEFT JOIN users u1 ON debates.creatorid = u1.userid LEFT JOIN users u2 ON debates.opponentid = u2.userid LEFT JOIN subscribed s on debates.debateid = s.debateid
    GROUP BY debates.debateid, u1.userid, u2.userid;