CREATE DATABASE chatop;
USE chatop;

CREATE TABLE `USERS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255),
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `RENTALS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` numeric,
  `price` numeric,
  `picture` varchar(255),
  `description` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);
ALTER TABLE RENTALS ADD owner_id INTEGER;

CREATE TABLE `MESSAGES` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `rental_id` integer,
  `user_id` integer,
  `message` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

ALTER TABLE RENTALS ADD CONSTRAINT FK_User_Rentals FOREIGN KEY (owner_id) REFERENCES USERS (id);

ALTER TABLE MESSAGES ADD CONSTRAINT FK_User_Messages FOREIGN KEY (user_id) REFERENCES USERS (id);

ALTER TABLE MESSAGES ADD CONSTRAINT FK_Rental_Messages FOREIGN KEY (rental_id) REFERENCES RENTALS (id);


