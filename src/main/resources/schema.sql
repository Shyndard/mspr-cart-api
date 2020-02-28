-- POSTGRESQL DATABASE 

CREATE extension "uuid-ossp";

CREATE table product(
	id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
	price NUMERIC(10,2),
	name varchar(50)
);

CREATE table cart (
	user_id UUID PRIMARY KEY,
	created_at TIMESTAMP DEFAULT current_timestamp
);

CREATE table cart_product(
	user_id UUID NOT NULL,
	product_id UUID NOT NULL,
	amount numeric(10),
	PRIMARY KEY(user_id, product_id)
);