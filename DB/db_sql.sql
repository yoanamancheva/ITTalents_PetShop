CREATE SCHEMA IF NOT EXISTS  petshop;
use petshop;
  CREATE TABLE users (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  administrator BOOLEAN NOT NULL  
  );  


-- pets

CREATE TABLE pets (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  gender ENUM('F', 'M') NOT NULL,
  breed VARCHAR(100) NOT NULL,
  sub_breed VARCHAR(100) NOT NULL,
  age INTEGER NOT NULL,
  posted TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  pet_desc VARCHAR(300) NOT NULL,
  in_sale BOOLEAN NOT NULL,
  price DOUBLE NOT NULL,
  quantity INTEGER NOT NULL
  );

CREATE TABLE pets_photos (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  photo_path VARCHAR(100) NOT NULL,
  pet_id INTEGER NOT NULL,
  FOREIGN KEY (pet_id) REFERENCES pets(id)
  );
  
  CREATE TABLE pets_in_sale (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  pet_id INTEGER NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  discount_price DOUBLE NOT NULL,
  FOREIGN KEY (pet_id) REFERENCES pets(id)  
  ); 
  
  CREATE TABLE all_orders_pets (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  address VARCHAR(200) NOT NULL,
  user_id INTEGER NOT NULL,
  final_price DOUBLE NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
  );
  
CREATE TABLE pets_in_order (
  order_id INTEGER NOT NULL,
  pet_id INTEGER NOT NULL,
  quantity INTEGER NOT NULL,
  PRIMARY KEY(order_id, pet_id),
  FOREIGN KEY (order_id) REFERENCES all_orders_pets(id),
  FOREIGN KEY (pet_id) REFERENCES pets(id)
  );
  
  
  -- products
  
  CREATE TABLE products (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  product_name VARCHAR(100) NOT NULL,
  category VARCHAR(100) NOT NULL,
  price DOUBLE NOT NULL,
  quantity INTEGER NOT NULL,
  manifacturer VARCHAR(100) NOT NULL,
  `description` VARCHAR(300) NOT NULL,
  photo_path VARCHAR(200) NOT NULL
  );  
    
  CREATE TABLE products_in_sale (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  product_id INTEGER NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  discount_price DOUBLE NOT NULL,
  FOREIGN KEY (product_id) REFERENCES products(id)
  ); 
  
  CREATE TABLE all_orders_products (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  address VARCHAR(200) NOT NULL,
  user_id INTEGER NOT NULL,
  final_price DOUBLE NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
  );
  
  CREATE TABLE products_in_order (
  order_id INTEGER NOT NULL,
  product_id INTEGER NOT NULL,
  quantity INTEGER NOT NULL,
  PRIMARY KEY (order_id, product_id),
  FOREIGN KEY (order_id) REFERENCES all_orders_products(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
  );

  CREATE TABLE reviews (
  id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
  product_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  review VARCHAR(500) NOT NULL,
  FOREIGN KEY (product_id) REFERENCES products(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
  );
  
