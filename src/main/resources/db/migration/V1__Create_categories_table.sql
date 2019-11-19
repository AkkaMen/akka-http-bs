CREATE TABLE "categories" (
"id" BIGSERIAL PRIMARY KEY,
"title" VARCHAR NOT NULL
);
ALTER TABLE categories ADD CONSTRAINT categories_unique_title UNIQUE (title);


--INSERT INTO categories VALUES (6, 'docker');
--INSERT INTO categories VALUES (2, 'postgres');
--INSERT INTO categories VALUES (3, 'scala');
--INSERT INTO categories VALUES (4, 'spark');
--INSERT INTO categories VALUES (5, 'maven');