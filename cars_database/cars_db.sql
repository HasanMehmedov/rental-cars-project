DROP DATABASE IF EXISTS cars_db;

CREATE DATABASE cars_db;
USE cars_db;

CREATE TABLE branches(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL UNIQUE,
    town VARCHAR(100) NOT NULL
);

CREATE TABLE employees(
	id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    egn CHAR(10) NOT NULL UNIQUE,
    phone_number CHAR(10) UNIQUE,
    email VARCHAR(255) UNIQUE,
    salary DOUBLE NOT NULL,
    manager_id INT,
    branch_id INT NOT NULL,
    
    CONSTRAINT fk_employees_managers FOREIGN KEY(manager_id) REFERENCES employees(id)
    ON UPDATE CASCADE ON DELETE SET NULL,
    
    CONSTRAINT fk_employees_branches FOREIGN KEY(branch_id) REFERENCES branches(id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE customers(
	id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    egn CHAR(10) NOT NULL UNIQUE,
    phone_number CHAR(10) UNIQUE,
    email VARCHAR(255) UNIQUE
);

CREATE TABLE bank_accounts(
	id INT PRIMARY KEY AUTO_INCREMENT,
    credit_card_id VARCHAR(100) NOT NULL UNIQUE,
    amount DOUBLE NOT NULL,
    customer_id INT NOT NULL,
    
    CONSTRAINT fk_customers_bank_accounts FOREIGN KEY(customer_id) REFERENCES customers(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE coupons(
	id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    
    CONSTRAINT fk_customers_coupons FOREIGN KEY(customer_id) REFERENCES customers(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE addresses(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    town VARCHAR(100) NOT NULL
);

CREATE TABLE cars(
	id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    reg_number VARCHAR(50) NOT NULL UNIQUE,
    year INT NOT NULL,
    mileage INT DEFAULT 0 NOT NULL,
    battery_level INT NOT NULL,
    is_free BOOL DEFAULT TRUE,
    town VARCHAR(100) NOT NULL,
    address_id INT NOT NULL,
    
    CONSTRAINT fk_cars_addresses FOREIGN KEY(address_id) REFERENCES addresses(id)
);

CREATE TABLE problems(
	id INT PRIMARY KEY AUTO_INCREMENT,
	car_id INT NOT NULL,
    type ENUM('repair of damages', 'recharge', 'clean up', 'replacement of tires', 'warranty service visit') NOT NULL,
    
    CONSTRAINT fk_problems_cars FOREIGN KEY(car_id) REFERENCES cars(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    
    CONSTRAINT uq_carId_type UNIQUE(car_id, type)
);

CREATE TABLE services(
	id INT PRIMARY KEY AUTO_INCREMENT,
    type ENUM('repair of damage', 'recharge', 'clean up', 'replacement of tires', 'warranty service visit') NOT NULL,
    car_id INT NOT NULL,
    
    CONSTRAINT fk_services_cars FOREIGN KEY(car_id) REFERENCES cars(id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE routes(
	id INT PRIMARY KEY AUTO_INCREMENT,
    user ENUM('customer', 'employee') NOT NULL,
    car_id INT NOT NULL,
    town VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    initial_address_id INT NOT NULL,
    end_address_id INT NOT NULL,
    start_time TIME NOT NULL,
    duration TIME NOT NULL,
    
    CONSTRAINT fk_routes_cars FOREIGN KEY(car_id) REFERENCES cars(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    
    CONSTRAINT fk_route_initial_address FOREIGN KEY(initial_address_id) REFERENCES addresses(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    
    CONSTRAINT fk_route_end_address FOREIGN KEY(end_address_id) REFERENCES addresses(id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE employees_routes(
	id INT PRIMARY KEY AUTO_INCREMENT,
    route_id INT NOT NULL UNIQUE,
    employee_id INT NOT NULL,
    service_id INT NOT NULL UNIQUE,
    
    CONSTRAINT fk_employees_routes_routes FOREIGN KEY(route_id) REFERENCES routes(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    
    CONSTRAINT fk_employees_routes_employees FOREIGN KEY(employee_id) REFERENCES employees(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    
    CONSTRAINT fk_employees_routes_services FOREIGN KEY(service_id) REFERENCES services(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE customers_routes(
	id INT PRIMARY KEY AUTO_INCREMENT,
    route_id INT NOT NULL,
    customer_id INT NOT NULL,
    price DOUBLE NOT NULL,
    coupon_used BOOL NOT NULL,
    
    CONSTRAINT fk_customers_routes_customers FOREIGN KEY(customer_id) REFERENCES customers(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    
    CONSTRAINT fk_customers_routes_routes FOREIGN KEY(route_id) REFERENCES routes(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO branches(name, address, town)
VALUES('Intellect', 'ul. "5019-ta" 42, 1592 zh.k. Drujba 1', 'Sofia'),
('Creative', '"Elin Pelin" № 41 – "Proslav"', 'Plovdiv'),
('Brightness', '"Vinitsa",ul."Cherno more" 1', 'Varna'),
('Style', '"D.Ezero" ul."Z.Zograf" No77', 'Burgas');

INSERT INTO addresses(name, town)
VALUES('ul. "prof. Georgi Bradistilov"', 'Sofia'),
('ul. "Vasil Kalchev", 1172 zh.k. Dianabad', 'Sofia'),
('ul. "Kosta Lulchev", 1113 Geo Milev', 'Sofia'),
('ul. Al. Bogoridi 26', 'Burgas'),
('bul. Hristo Botev 55', 'Burgas'),
('ul. 3ti Mart', 'Burgas'),
('ul. Vasil Levski 3', 'Plovdiv'),
('zh.k. Trakia, ul. Shipka 1', 'Plovdiv'),
('ul. Gerlovo 1', 'Plovdiv'),
('ul. Knyaz Boris I', 'Varna'),
('ul. prof. Marin Drinov', 'Varna'),
('bul. Chatalja', 'Varna');