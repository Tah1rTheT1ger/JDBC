INSERT INTO customer(roll_number, full_name, comic_id)
VALUES
    ('IMT2022100', 'Tahir', NULL),
    ('IMT2022034', 'Tarun', NULL),
    ('IMT2022536', 'Lohit', NULL);

INSERT INTO comic(comic_id, comic_name, comic_author, publication_year, issuer)
VALUES
    ('b1', 'The Amazing Spider-Man', 'Stan Lee', '1963', NULL),
    ('b2', 'Batman: Year One', 'Frank Miller', '1987', NULL),
    ('b3', 'Watchmen', 'Alan Moore', '1986', NULL);

INSERT INTO comicseller(comicseller_id, comicseller_name, comicseller_password)
VALUES
    ('l1', 'seller1', '1234'),
    ('l2', 'seller2', '1456');

INSERT INTO super_admin(super_admin_id, super_admin_name, super_admin_password)
VALUES
    ('a1', 'admin1', '1234'),
    ('a2', 'admin2', '1456');
