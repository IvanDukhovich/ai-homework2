-- Insert test user authentication data
-- Password: password123 (BCrypt hashed)
INSERT INTO user_auth (name, email, password_hash) VALUES 
('Test User', 'test@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG');

-- Insert users
INSERT INTO users (id, name, username, email, phone, website) VALUES
(1, 'Test User', 'testuser', 'test@example.com', '123-456-7890', 'test.com'),
(2, 'John Doe', 'johndoe', 'john@example.com', '098-765-4321', 'john.com');

-- Insert addresses for users
INSERT INTO addresses (id, user_id, street, suite, city, zipcode) VALUES
(1, 1, 'Test Street', 'Apt 123', 'Test City', '12345'),
(2, 2, 'Main Street', 'Suite 456', 'Another City', '67890');

-- Insert geo coordinates for addresses
INSERT INTO geo (id, address_id, lat, lng) VALUES
(1, 1, '40.7128', '-74.0060'),
(2, 2, '34.0522', '-118.2437');

-- Insert companies for users
INSERT INTO companies (id, user_id, name, catch_phrase, bs) VALUES
(1, 1, 'Test Company', 'Test Catch Phrase', 'Test BS'),
(2, 2, 'Another Company', 'Another Catch Phrase', 'Another BS'); 