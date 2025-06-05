-- Insert test user authentication data
-- Password: password123 (BCrypt hashed)
INSERT INTO user_auth (name, email, password_hash) VALUES 
('Test User', 'test@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG');

-- Insert users
INSERT INTO users (name, username, email, phone, website) VALUES
('Leanne Graham', 'bret', 'leanne@example.com', '1-770-736-8031 x56442', 'hildegard.org'),
('Ervin Howell', 'antonette', 'ervin@example.com', '010-692-6593 x09125', 'anastasia.net');

-- Insert addresses for users
INSERT INTO addresses (user_id, street, suite, city, zipcode) VALUES
(1, 'Kulas Light', 'Apt. 556', 'Gwenborough', '92998-3874'),
(2, 'Victor Plains', 'Suite 879', 'Wisokyburgh', '90566-7771');

-- Insert geo coordinates for addresses
INSERT INTO geo (address_id, lat, lng) VALUES
(1, '-37.3159', '81.1496'),
(2, '-43.9509', '-34.4618');

-- Insert companies for users
INSERT INTO companies (user_id, name, catch_phrase, bs) VALUES
(1, 'Romaguera-Crona', 'Multi-layered client-server neural-net', 'harness real-time e-markets'),
(2, 'Deckow-Crist', 'Proactive didactic contingency', 'synergize scalable supply-chains'); 