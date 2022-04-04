CREATE TABLE IF NOT EXISTS users
(
    userid SERIAL PRIMARY KEY,
    username varchar(100) UNIQUE NOT NULL,
    password varchar(100) NOT NULL
);