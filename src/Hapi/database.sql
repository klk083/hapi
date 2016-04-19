-- noinspection SqlNoDataSourceInspectionForFile
DROP TABLE IF EXISTS subscription_menu;
DROP TABLE IF EXISTS subscription_order;
DROP TABLE IF EXISTS menu_order;
DROP TABLE IF EXISTS menu_ingredient;
DROP TABLE IF EXISTS ingredient;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS statistics;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS subscription;

CREATE TABLE customer(
  customer_id INTEGER AUTO_INCREMENT,
  customer_name VARCHAR(30) NOT NULL,
  customer_address VARCHAR(30),
  customer_tlf VARCHAR(8) UNIQUE NOT NULL,
  customer_discount INTEGER NOT NULL,
  is_company BOOLEAN NOT NULL,
  CONSTRAINT customer_pk PRIMARY KEY(customer_id));

CREATE TABLE menu(
  menu_id INTEGER AUTO_INCREMENT,
  menu_name VARCHAR(30) NOT NULL,
  menu_price INTEGER NOT NULL,
  menu_description VARCHAR(100),
  CONSTRAINT menu_pk PRIMARY KEY(menu_id));

CREATE TABLE orders(
  order_id INTEGER AUTO_INCREMENT,
  customer_id INTEGER NOT NULL,
  delivery_time DATETIME,
  delivered BOOLEAN,
  CONSTRAINT order_pk PRIMARY KEY(order_id));

CREATE TABLE menu_order(
  order_id INTEGER NOT NULL,
  menu_id INTEGER NOT NULL,
  quantity INTEGER NOT NULL,
  description VARCHAR(100),
  inprogress BOOLEAN NOT NULL,
  CONSTRAINT menu_order_pk PRIMARY KEY (order_id, menu_id),
  CONSTRAINT menu_order_fk1 FOREIGN KEY (order_id) REFERENCES orders(order_id),
  CONSTRAINT menu_order_fk2 FOREIGN KEY (menu_id) REFERENCES menu(menu_id));

CREATE TABLE role(
  role_id INTEGER AUTO_INCREMENT,
  role VARCHAR(30) NOT NULL,
  CONSTRAINT role_pk PRIMARY KEY (role_id));

CREATE TABLE employee(
  employee_id INTEGER AUTO_INCREMENT,
  role_id INTEGER NOT NULL,
  name VARCHAR(30) NOT NULL,
  username VARCHAR(12) UNIQUE NOT NULL,
  password_hash VARCHAR(106) NOT NULL,
  password_salt VARCHAR(16) NOT NULL,
  CONSTRAINT employee_pk PRIMARY KEY(employee_id),
  CONSTRAINT employee_fk FOREIGN KEY (role_id) REFERENCES role(role_id));

CREATE TABLE subscription(
  subscription_id INTEGER AUTO_INCREMENT,
  description VARCHAR(100),
  CONSTRAINT subscription_pk PRIMARY KEY(subscription_id));

CREATE TABLE subscription_order(
  subscription_id INTEGER NOT NULL,
  order_id INTEGER NOT NULL,
  from_date DATE,
  to_date DATE,
  CONSTRAINT subscription_order_pk PRIMARY KEY(subscription_id, order_id),
  CONSTRAINT subscription_order_fk1 FOREIGN KEY (subscription_id) REFERENCES subscription(subscription_id),
  CONSTRAINT subscription_order_fk2 FOREIGN KEY(order_id) REFERENCES orders(order_id));

CREATE TABLE subscription_menu(
  subscription_id INTEGER NOT NULL,
  menu_id INTEGER NOT NULL,
  CONSTRAINT subscription_menu_pk PRIMARY KEY(subscription_id, menu_id),
  CONSTRAINT subscription_menu_fk1 FOREIGN KEY (subscription_id) REFERENCES subscription(subscription_id),
  CONSTRAINT subscription_menu_fk2 FOREIGN KEY(menu_id) REFERENCES menu(menu_id));

CREATE TABLE ingredient(
  ingredient_id INTEGER AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
  unit VARCHAR(5) NOT NULL,
  price INTEGER NOT NULL,
  CONSTRAINT ingredient_pk PRIMARY KEY(ingredient_id));

CREATE TABLE menu_ingredient(
  ingredient_id INTEGER NOT NULL,
  menu_id INTEGER NOT NULL,
  quantity INTEGER NOT NULL,
  CONSTRAINT menu_ingredient_pk PRIMARY KEY (ingredient_id, menu_id),
  CONSTRAINT menu_ingredient_fk1 FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id),
  CONSTRAINT menu_ingredient_fk2 FOREIGN KEY (menu_id) REFERENCES menu(menu_id));
