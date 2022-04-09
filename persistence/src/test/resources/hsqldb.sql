SET DATABASE SQL SYNTAX PGS TRUE;
CREATE TABLE IF NOT EXISTS users
(
    userid SERIAL PRIMARY KEY,
    email varchar(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS debates
(
    debateid SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    description varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS posts
(
    postid SERIAL PRIMARY KEY,
    debateid INTEGER NOT NULL REFERENCES debates,
    userid INTEGER REFERENCES users NOT NULL,
    content TEXT NOT NULL
);