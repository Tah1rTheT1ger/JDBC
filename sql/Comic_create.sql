CREATE DATABASE ComicStore;
USE ComicStore;

GRANT ALL PRIVILEGES ON ComicStore.* TO 'user1'@'localhost';

CREATE TABLE customer (
    roll_number VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) CHECK (full_name NOT LIKE '%[0-9]%'),
    comic_id VARCHAR(100),
    CONSTRAINT pk_customer PRIMARY KEY (roll_number)
);

CREATE TABLE comic (
    comic_id VARCHAR(100) NOT NULL,
    comic_name VARCHAR(100) NOT NULL,
    comic_author VARCHAR(100) NOT NULL,
    publication_year INTEGER NOT NULL,
    issuer VARCHAR(100),
    CONSTRAINT pk_comic PRIMARY KEY (comic_id)
);

CREATE TABLE comicseller (
    comicseller_id VARCHAR(100) NOT NULL,
    comicseller_name VARCHAR(100) NOT NULL,
    comicseller_password VARCHAR(100) NOT NULL,
    CONSTRAINT pk_comicseller PRIMARY KEY (comicseller_id)
);

CREATE TABLE super_admin (
    super_admin_id VARCHAR(100) NOT NULL,
    super_admin_name VARCHAR(100) NOT NULL,
    super_admin_password VARCHAR(100) NOT NULL,
    CONSTRAINT pk_super_admin PRIMARY KEY (super_admin_id)
);

CREATE TABLE transaction(
    roll_number VARCHAR(100) NOT NULL,
    comic_id VARCHAR(100) ,
    CONSTRAINT pk_id PRIMARY KEY(comic_id)
);