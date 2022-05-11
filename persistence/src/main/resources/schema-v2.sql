CREATE TABLE IF NOT EXISTS votes (
    userid INTEGER REFERENCES users NOT NULL,
    debateid INTEGER REFERENCES debates NOT NULL,
    PRIMARY KEY (userid, debateid),
    vote INTEGER NOT NULL
);

ALTER TABLE posts ADD COLUMN IF NOT EXISTS status INTEGER;

CREATE OR REPLACE VIEW public_posts AS
SELECT posts.postid, users.username, posts.debateid, posts.content, COUNT(likes.userid) AS likes, posts.created_date, posts.imageid, posts.status
FROM posts LEFT JOIN likes ON posts.postid = likes.postid LEFT JOIN users ON posts.userid = users.userid
GROUP BY posts.postid, users.username;

CREATE OR REPLACE VIEW public_debates AS
    SELECT debates.debateid, debates.name, debates.description, debates.category, debates.created_date, debates.imageid,
       u1.username AS creatorusername, u2.username AS opponentusername,
       COUNT(DISTINCT s.userid) AS subscribedcount, debates.status,
       COUNT(DISTINCT v.userid) FILTER ( WHERE vote = 0 ) AS forcount, COUNT(DISTINCT v.userid) FILTER ( WHERE vote = 1 ) AS againstcount
    FROM debates LEFT JOIN users u1 ON debates.creatorid = u1.userid
        LEFT JOIN users u2 ON debates.opponentid = u2.userid LEFT JOIN subscribed s on debates.debateid = s.debateid
        LEFT JOIN votes v ON debates.debateid = v.debateid
    GROUP BY debates.debateid, u1.userid, u2.userid;