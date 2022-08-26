DROP TABLE IF EXISTS `post`;
DROP TABLE IF EXISTS `pet_images`;
DROP TABLE IF EXISTS `pet`;
DROP TABLE IF EXISTS `breed`;
DROP TABLE IF EXISTS `pet_type`;
DROP TABLE IF EXISTS `location`;
DROP TABLE IF EXISTS `validation_token`;
DROP TABLE IF EXISTS `password_reset_token`;
DROP TABLE IF EXISTS `contact`;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKt4v0rrweyk393bdgt107vdx0x` (`role_id`),
  KEY `FK2o0jvgh89lemvvo17cbqvdxaa` (`user_id`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKt4v0rrweyk393bdgt107vdx0x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `contact` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `contact_value` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbxl6anxo14q097g8cd2e51v55` (`user_id`),
  CONSTRAINT `FKbxl6anxo14q097g8cd2e51v55` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `password_reset_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiration_date` bigint NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK83nsrttkwkb6ym0anu051mtxn` (`user_id`),
  CONSTRAINT `FK83nsrttkwkb6ym0anu051mtxn` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `validation_token` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiration_date` bigint NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrh9uoclxjl8m4mn844gdtgnt4` (`user_id`),
  CONSTRAINT `FKrh9uoclxjl8m4mn844gdtgnt4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `location` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `sub_region` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pet_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `breed` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `pet_type_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnytk9d1juxhhmj9du2001a4l8` (`pet_type_id`),
  CONSTRAINT `FKnytk9d1juxhhmj9du2001a4l8` FOREIGN KEY (`pet_type_id`) REFERENCES `pet_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `age` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `breed_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiqbjbaml7gtwulqptktmsi5dc` (`breed_id`),
  CONSTRAINT `FKiqbjbaml7gtwulqptktmsi5dc` FOREIGN KEY (`breed_id`) REFERENCES `breed` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pet_images` (
  `pet_id` bigint NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  KEY `FKjv5ge6puunoipe1pkod8dlmwq` (`pet_id`),
  CONSTRAINT `FKjv5ge6puunoipe1pkod8dlmwq` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `post` (
  `type` varchar(31) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `relocation_urgency` varchar(255) DEFAULT NULL,
  `location_id` bigint DEFAULT NULL,
  `pet_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ai7w8jit2djyn5q2wbb6yndo2` (`pet_id`),
  KEY `FKi1psqwjnonr2lmbvxow5walgf` (`location_id`),
  KEY `FK7ky67sgi7k0ayf22652f7763r` (`user_id`),
  CONSTRAINT `FK7ky67sgi7k0ayf22652f7763r` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKdiyv23j4rejh5e6uq6v4846uj` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`),
  CONSTRAINT `FKi1psqwjnonr2lmbvxow5walgf` FOREIGN KEY (`location_id`) REFERENCES `location` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

## ROLES
INSERT INTO role (name) VALUES('ROLE_USER'), ('ROLE_ADMIN');

## USERS
INSERT INTO users (email, enabled, first_name, password, username) 
VALUES 
('exampleadmin1@gmail.com', 1, 'Example Admin 1', '$2a$10$imYiGRQ/v9dKg9PweIhAl.uho3eF.lYwW3pFkgba00KYa1v4Hzkaq', 'exampleadmin1'),
('example1@gmail.com', 1, 'Example1', '$2a$10$fhC48J2zQv7U0LhI0hC.9OzY973NvYNmhYn3JiWhEV2WlGMNbFvau', 'example1'),
('example2@gmail.com', 1, 'Example2', '$2a$10$pos2dlRRttlk5jDYiNboMu1pbBAvyGNrgaiKJK1WWOsTTa47N6L5G', 'example2'),
('example3@gmail.com', 1, 'Example3', '$2a$10$GQzSmxpjiSvcT24/4VWNhes2pcF3PLX9t13cefb8vWcCupFcPIfIO', 'example3'),
('example4@gmail.com', 1, 'Example4', '$2a$10$pdRGdf9Hx/R/0mINwccWVe0xJVNGZnvZCmqePhLGzq58aNjNjaBu6', 'example4');

#USERS ROLES
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, role r
WHERE r.name = 'ROLE_USER';
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, role r
WHERE r.name = 'ROLE_ADMIN' AND u.username = 'exampleadmin1';

#LOCATIONS
INSERT INTO location (country, region, sub_region) 
VALUES 
('ARGENTINA', 'CABA', 'BELGRANO'), ('ARGENTINA', 'CABA', 'PALERMO'), ('ARGENTINA', 'CABA', 'LUGANO'), 
('ARGENTINA', 'CABA', 'CABALLITO'), ('ARGENTINA', 'CABA', 'SAN NICOLAS'), ('ARGENTINA', 'GBA', 'AVELLANEDA'),
 ('ARGENTINA', 'GBA', 'QUILMES'), ('ARGENTINA', 'GBA', 'MORENO'), ('ARGENTINA', 'GBA', 'PILAR'), ('ARGENTINA', 'GBA', 'TIGRE');

#Pet type
INSERT INTO pet_type (name) VALUES('DOG'),('CAT'),('TURTLE'),('OTHER');

#Breeds
INSERT INTO breed (name, pet_type_id)
SELECT n.column_0, p.id
FROM pet_type p, (VALUES ROW('POODLE'), ROW('DALMATIAN'), ROW('CANE CORSO'), ROW('BULL DOG'),
	ROW('OTHER')) n
WHERE p.name = 'DOG';

INSERT INTO breed (name, pet_type_id)
SELECT n.column_0, p.id
FROM pet_type p, (VALUES ROW('PERSA'), ROW('BENGAL'), ROW('SIAMESE'), ROW('RAGDOLL'),
	ROW('OTHER')) n
WHERE p.name = 'CAT';

INSERT INTO breed (name, pet_type_id)
SELECT 'OTHER', p.id
FROM pet_type p
WHERE p.name = 'TURTLE' OR p.name = 'OTHER';

# Pet / pet images / post
INSERT INTO pet (age, description, name, breed_id)
SELECT 3, 'Dog-friendly', 'Toto', b.id
FROM breed b WHERE b.name = 'POODLE';
INSERT INTO pet_images (pet_id, image)
SELECT p.id, 'poodle.jpg'
FROM pet p WHERE p.name = 'Toto';
INSERT INTO post (type, date, description, relocation_urgency, location_id, pet_id, user_id)
SELECT 'found', DATE("2017-06-15"),'Found in the street between FakeStreet and NotAStreet',
	'NONE', l.id, p.id, u.id
FROM location l, pet p, users u
WHERE l.country = 'ARGENTINA' AND l.region = 'CABA' AND l.sub_region = 'BELGRANO'
AND p.name = 'Toto' AND u.username = 'example1';

INSERT INTO pet (age, description, name, breed_id)
SELECT 1, 'Female, not friendly with dogs', 'Dora', b.id
FROM breed b WHERE b.name = 'CANE CORSO';
INSERT INTO pet_images (pet_id, image)
SELECT p.id, 'canecorso.jpg'
FROM pet p WHERE p.name = 'Dora';
INSERT INTO post (type, date, description, relocation_urgency, location_id, pet_id, user_id)
SELECT 'search', DATE("2020-01-15"),'She ran in San Martin square and I lost sight of her',
	null, l.id, p.id, u.id
FROM location l, pet p, users u
WHERE l.country = 'ARGENTINA' AND l.region = 'CABA' AND l.sub_region = 'SAN NICOLAS'
AND p.name = 'Dora' AND u.username = 'example1';

INSERT INTO pet (age, description, name, breed_id)
SELECT 7, 'Has a scar on the cheek', 'Merli', b.id
FROM breed b JOIN pet_type p ON p.id = b.pet_type_id 
WHERE b.name = 'OTHER' AND p.name = 'DOG';
INSERT INTO pet_images (pet_id, image)
SELECT p.id, 'merli.jpg'
FROM pet p WHERE p.name = 'Merli';
INSERT INTO post (type, date, description, relocation_urgency, location_id, pet_id, user_id)
SELECT 'found', DATE("2020-03-11"),'Found in Rodriguez Pena square',
	'LOW', l.id, p.id, u.id
FROM location l, pet p, users u
WHERE l.country = 'ARGENTINA' AND l.region = 'CABA' AND l.sub_region = 'SAN NICOLAS'
AND p.name = 'Merli' AND u.username = 'example2';

INSERT INTO pet (age, description, name, breed_id)
SELECT 3, 'Red collar', 'Lizy', b.id
FROM breed b JOIN pet_type p ON p.id = b.pet_type_id 
WHERE b.name = 'OTHER' AND p.name = 'DOG';
INSERT INTO pet_images (pet_id, image)
SELECT p.id, 'lizy.jpg'
FROM pet p WHERE p.name = 'Lizy';
INSERT INTO post (type, date, description, relocation_urgency, location_id, pet_id, user_id)
SELECT 'found', DATE("2020-07-11"),'Found in Serrano square. Need realocation, I have a cat.',
	'HIGH', l.id, p.id, u.id
FROM location l, pet p, users u
WHERE l.country = 'ARGENTINA' AND l.region = 'CABA' AND l.sub_region = 'PALERMO'
AND p.name = 'Lizy' AND u.username = 'example2';

INSERT INTO pet (age, description, name, breed_id)
SELECT 0, 'Is a kitten, no more than three months', 'Kitty', b.id
FROM breed b JOIN pet_type p ON p.id = b.pet_type_id 
WHERE b.name = 'OTHER' AND p.name = 'CAT';
INSERT INTO pet_images (pet_id, image)
SELECT p.id, 'kitty.jpg'
FROM pet p WHERE p.name = 'Kitty';
INSERT INTO post (type, date, description, relocation_urgency, location_id, pet_id, user_id)
SELECT 'found', DATE("2021-03-11"),'',
	'HIGH', l.id, p.id, u.id
FROM location l, pet p, users u
WHERE l.country = 'ARGENTINA' AND l.region = 'GBA' AND l.sub_region = 'AVELLANEDA'
AND p.name = 'Kitty' AND u.username = 'example3';

INSERT INTO pet (age, description, name, breed_id)
SELECT 3, 'has a gray and black tail, is a little overweight', 'Aleu', b.id
FROM breed b JOIN pet_type p ON p.id = b.pet_type_id 
WHERE b.name = 'OTHER' AND p.name = 'CAT';
INSERT INTO pet_images (pet_id, image)
SELECT p.id, 'aleu.jpg'
FROM pet p WHERE p.name = 'Aleu';
INSERT INTO post (type, date, description, relocation_urgency, location_id, pet_id, user_id)
SELECT 'search', DATE("2021-01-11"),'She went out through my apartment balcony',
	null, l.id, p.id, u.id
FROM location l, pet p, users u
WHERE l.country = 'ARGENTINA' AND l.region = 'CABA' AND l.sub_region = 'PALERMO'
AND p.name = 'Aleu' AND u.username = 'example3';

INSERT INTO pet (age, description, name, breed_id)
SELECT 8, 'Broken shell on left side', 'Manolo', b.id
FROM breed b JOIN pet_type p ON p.id = b.pet_type_id 
WHERE b.name = 'OTHER' AND p.name = 'TURTLE';
INSERT INTO pet_images (pet_id, image)
SELECT p.id, 'manolo.jpg'
FROM pet p WHERE p.name = 'Manolo';
INSERT INTO post (type, date, description, relocation_urgency, location_id, pet_id, user_id)
SELECT 'search', DATE("2021-02-11"),'He ran away from my house near China Town Tigre',
	null, l.id, p.id, u.id
FROM location l, pet p, users u
WHERE l.country = 'ARGENTINA' AND l.region = 'GBA' AND l.sub_region = 'TIGRE'
AND p.name = 'Manolo' AND u.username = 'example4';