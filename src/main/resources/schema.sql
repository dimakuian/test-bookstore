CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ROLE_WRITER', 'ROLE_VIEWER') NOT NULL
);

CREATE TABLE IF NOT EXISTS authors (
  author_id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  UNIQUE (first_name, last_name)
);

CREATE TABLE IF NOT EXISTS books (
  book_id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  author_id INT NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  UNIQUE (title, author_id),
  FOREIGN KEY (author_id) REFERENCES authors(`author_id`)
);