SET DATABASE SQL SYNTAX PGS TRUE;

CREATE TABLE IF NOT EXISTS images
(
    imageid SERIAL PRIMARY KEY,
    data varbinary(100000) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    userid SERIAL PRIMARY KEY,
    email varchar(100) UNIQUE NOT NULL,
    username VARCHAR(64) UNIQUE,
    password VARCHAR(100),
    created_date DATE,
    role INTEGER,
    imageid INTEGER REFERENCES images(imageid) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS debates
(
    debateid SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    description TEXT NOT NULL,
    created_date timestamp,
    category INTEGER,
    status INTEGER,
    creatorid INTEGER REFERENCES users(userid),
    opponentid INTEGER REFERENCES users(userid),
    imageid INTEGER REFERENCES images(imageid) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS posts
(
    postid SERIAL PRIMARY KEY,
    debateid INTEGER NOT NULL REFERENCES debates,
    userid INTEGER REFERENCES users NOT NULL,
    content TEXT NOT NULL,
    created_date timestamp,
    imageid INTEGER REFERENCES images(imageid) ON DELETE SET NULL
);

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

CREATE VIEW public_posts AS
SELECT posts.postid, users.username, posts.debateid, posts.content, COUNT(likes.userid) AS likes, posts.created_date, posts.imageid
FROM posts LEFT JOIN likes ON posts.postid = likes.postid LEFT JOIN users ON posts.userid = users.userid
GROUP BY posts.postid, users.username;

CREATE TABLE IF NOT EXISTS votes (
                                     userid INTEGER REFERENCES users NOT NULL,
                                     debateid INTEGER REFERENCES debates NOT NULL,
                                     PRIMARY KEY (userid, debateid),
                                     vote INTEGER NOT NULL
);

CREATE VIEW public_debates AS
    SELECT debates.debateid, debates.name, debates.description, debates.category, debates.created_date, debates.imageid,
       u1.username AS creatorusername, u2.username AS opponentusername,
       COUNT(DISTINCT s.userid) AS subscribedcount, debates.status,
       COUNT(DISTINCT v.userid) FILTER ( WHERE vote = 0 ) AS forcount, COUNT(DISTINCT v.userid) FILTER ( WHERE vote = 1 ) AS againstcount
    FROM debates LEFT JOIN users u1 ON debates.creatorid = u1.userid
             LEFT JOIN users u2 ON debates.opponentid = u2.userid LEFT JOIN subscribed s on debates.debateid = s.debateid
             LEFT JOIN votes v ON debates.debateid = v.debateid
    GROUP BY debates.debateid, u1.userid, u2.userid;