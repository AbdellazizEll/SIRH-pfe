-- Insert roles if the table is empty
INSERT INTO roles (name) SELECT 'ROLE_RH' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_RH');
INSERT INTO roles (name) SELECT 'ROLE_MANAGER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_MANAGER');
INSERT INTO roles (name) SELECT 'ROLE_COLLABORATOR' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_COLLABORATOR');

-- Increase the length of the url column if necessary
ALTER TABLE account_verifications MODIFY url VARCHAR(1024);


-- Insert collaborateurs with solde set to 30
INSERT IGNORE INTO collaborateur (firstname, lastname, email, password, phone, is_enabled, solde)
VALUES
('John', 'Doe', 'john@example.com', 'admin123', '+1234567890', true, 30),
('Jane', 'Smith', 'jane@example.com', 'admin123', '+9876543210', true, 30),
('Alice', 'Johnson', 'alice@example.com', 'admin123', '+2468135790', true, 30),
('Bob', 'Brown', 'bob@example.com', 'admin123', '+1357924680', true, 30),
('David', 'Taylor', 'david@example.com', 'admin123', '+3698521470', true, 30),
('Emily', 'Wilson', 'emily@example.com', 'admin123', '+7539514680', true, 30),
('Grace', 'Martinez', 'grace@example.com', 'admin123', '+9876541230', true, 30),
('Henry', 'Garcia', 'henry@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+3692581470', true, 30),
('Kevin', 'Thomas', 'kevin@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+4569871230', true, 30),
('Michael', 'Johnson', 'michael@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+7894561230', true, 30),
('Sarah', 'Miller', 'sarah@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+2587419300', true, 30),
('William', 'Brown', 'william@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+3698527410', true, 30),
('Jennifer', 'Davis', 'jennifer@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+9741236580', true, 30),
('David', 'Garcia', 'davidg@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+8524173900', true, 30),
('Ashley', 'Robinson', 'ashley@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+6541239870', true, 30),
('Brandon', 'Clark', 'brandon@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+4917832156', true, 30),
('Christina', 'Lewis', 'christina@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+3374196320', true, 30),
('Daniel', 'Walker', 'daniel@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+1237894560', true, 30),
('Elizabeth', 'Hall', 'elizabeth@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+5419873260', true, 30),
('Joseph', 'Baker', 'joseph@example.com', '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', '+6145678901', true, 30);

-- Assign roles to collaborateurs
INSERT IGNORE INTO collaborateur_roles (collaborateur_id, role_id)
SELECT c.id, r.id FROM collaborateur c, roles r
WHERE (c.email, r.name) IN
      (('john@example.com', 'ROLE_COLLABORATOR'),
       ('jane@example.com', 'ROLE_MANAGER'),
       ('alice@example.com', 'ROLE_RH'),
       ('bob@example.com', 'ROLE_COLLABORATOR'),
       ('david@example.com', 'ROLE_MANAGER'),
       ('emily@example.com', 'ROLE_RH'),
       ('grace@example.com', 'ROLE_MANAGER'),
       ('henry@example.com', 'ROLE_RH'),
       ('kevin@example.com', 'ROLE_COLLABORATOR'),
       ('michael@example.com', 'ROLE_MANAGER'),
       ('sarah@example.com', 'ROLE_RH'),
       ('william@example.com', 'ROLE_COLLABORATOR'),
       ('jennifer@example.com', 'ROLE_MANAGER'),
       ('davidg@example.com', 'ROLE_RH'),
       ('ashley@example.com', 'ROLE_COLLABORATOR'),
       ('brandon@example.com', 'ROLE_MANAGER'),
       ('christina@example.com', 'ROLE_RH'),
       ('daniel@example.com', 'ROLE_COLLABORATOR'),
       ('elizabeth@example.com', 'ROLE_MANAGER'),
       ('joseph@example.com', 'ROLE_RH'));

-- Reset the auto-increment sequence if applicable (specific to MySQL)
ALTER TABLE absence AUTO_INCREMENT = 1;

-- Insert predefined absence types
-- Insert predefined absence types
INSERT IGNORE INTO absence (absence, type_abs) VALUES
    (1, 'PAID_LEAVE'),
    (2, 'UNPAID_LEAVE'),
    (3, 'SICK_LEAVE'),
    (4, 'MATERNITY_LEAVE'),
    (5, 'PARENTAL_LEAVE'),
    (6, 'STUDY_LEAVE'),
    (7, 'MARRIAGE_LEAVE'),
    (8, 'EMERGENCY_LEAVE');


