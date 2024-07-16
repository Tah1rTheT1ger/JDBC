ALTER TABLE customer
    ADD CONSTRAINT fk_comic_id FOREIGN KEY (comic_id) REFERENCES comic(comic_id);

ALTER TABLE comic
    ADD CONSTRAINT fk_issuer FOREIGN KEY (issuer) REFERENCES customer(roll_number);


