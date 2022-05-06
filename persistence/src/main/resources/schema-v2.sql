CREATE TABLE IF NOT EXISTS votes (
    userid INTEGER REFERENCES users NOT NULL,
    debateid INTEGER REFERENCES debates NOT NULL,
    PRIMARY KEY (userid, debateid),
    vote INTEGER NOT NULL
);

CREATE OR REPLACE VIEW public_debates AS
SELECT debates.debateid, debates.name, debates.description, debates.category, debates.created_date, debates.imageid,
       u1.username AS creatorusername, u2.username AS opponentusername,
       COUNT(DISTINCT s.userid) AS subscribedcount, debates.status,
       COUNT(DISTINCT v.userid) FILTER ( WHERE vote = 0 ) AS forcount, COUNT(DISTINCT v.userid) FILTER ( WHERE vote = 1 ) AS againstcount
FROM debates LEFT JOIN users u1 ON debates.creatorid = u1.userid
    LEFT JOIN users u2 ON debates.opponentid = u2.userid LEFT JOIN subscribed s on debates.debateid = s.debateid
    LEFT JOIN votes v ON debates.debateid = v.debateid
GROUP BY debates.debateid, u1.userid, u2.userid;