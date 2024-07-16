ALTER TABLE customer
    DROP FOREIGN KEY fk_comic_id;

ALTER TABLE comic
    DROP FOREIGN KEY fk_issuer;

DROP TABLE comic;
DROP TABLE comicseller;
DROP TABLE customer;
DROP TABLE super_admin;

DROP DATABASE ComicStore;
