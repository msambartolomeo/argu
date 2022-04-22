CREATE TABLE IF NOT EXISTS users
(
    userid SERIAL PRIMARY KEY,
    username VARCHAR(64) UNIQUE default null,
    password VARCHAR(100) default null,
    email varchar(100) UNIQUE NOT NULL,
    created_date TIMESTAMP default now()
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

CREATE TABLE IF NOT EXISTS suscribed
(
    userid INTEGER NOT NULL REFERENCES users,
    debateid INTEGER NOT NULL REFERENCES debates,
    PRIMARY KEY (userid, debateid)
);