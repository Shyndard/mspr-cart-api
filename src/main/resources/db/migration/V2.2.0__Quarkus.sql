CREATE table category(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	name varchar(50)
);

INSERT INTO category (name) VALUES ('none'), ('health'), ('food');

ALTER TABLE product ADD category UUID;
ALTER TABLE product ADD description varchar(500);
ALTER TABLE product ADD logo_url varchar(200);

UPDATE product SET category = (SELECT id FROM category WHERE name ILIKE 'none')