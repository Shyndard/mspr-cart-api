CREATE table vat(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	name varchar(50)
);

ALTER TABLE product ADD vat UUID;
ALTER TABLE product DROP COLUMN tva;

INSERT INTO vat (name) VALUES ('normal'), ('intermediary'), ('reduce'), ('super_reduce');