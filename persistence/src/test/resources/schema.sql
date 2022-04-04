CREATE TABLE users
(
    userid INTEGER IDENTITY PRIMARY KEY,
    username varchar(100) UNIQUE NOT NULL,
    password varchar(100) NOT NULL
);