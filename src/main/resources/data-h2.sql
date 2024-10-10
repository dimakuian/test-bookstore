INSERT INTO users (username, password, role) VALUES
('john_doe', '$2a$12$idjZEDYokQmuhTT3tbnpR.d444nUFoUkgkM.KinoQYiIzXxf5sRkm', 'ROLE_USER');
INSERT INTO users (username, password, role) VALUES
('jane_doe', '$2a$12$idjZEDYokQmuhTT3tbnpR.d444nUFoUkgkM.KinoQYiIzXxf5sRkm', 'ROLE_ADMIN');
INSERT INTO users (username, password, role) VALUES
('jim_beam', '$2a$12$idjZEDYokQmuhTT3tbnpR.d444nUFoUkgkM.KinoQYiIzXxf5sRkm', 'ROLE_USER');

INSERT INTO authors (first_name, last_name) VALUES ('John', 'Doe');
INSERT INTO authors (first_name, last_name) VALUES ('Jane', 'Smith');
INSERT INTO authors (first_name, last_name) VALUES ('Emily', 'Johnson');
INSERT INTO authors (first_name, last_name) VALUES ('Michael', 'Brown');

INSERT INTO books (title, author_id, price) VALUES ('The Great Adventure', 1, 19.99);
INSERT INTO books (title, author_id, price) VALUES ('Mystery in the Night', 1, 15.50);
INSERT INTO books (title, author_id, price) VALUES ('The Lost World', 2, 12.75);
INSERT INTO books (title, author_id, price) VALUES ('Science for Beginners', 3, 22.00);
INSERT INTO books (title, author_id, price) VALUES ('Advanced Calculus', 4, 18.40);