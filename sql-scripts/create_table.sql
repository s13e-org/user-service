use user_service;

CREATE TABLE users (
	user_id BINARY(16) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    avatar_url VARCHAR(255),
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NULL
);

CREATE TABLE roles (
    role_id BINARY(16) PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE permissions (
    permission_id  BINARY(16) PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE permission_endpoints (
    id BINARY(16) PRIMARY KEY,
    permission_id BINARY(16) NOT NULL,
    http_method VARCHAR(10), 
    service VARCHAR(10),
    endpoint_path VARCHAR(255) NOT NULL, -- /api/users, /api/products/:id
    check (http_method in ('GET', 'POST', 'PUT', 'DELETE')),
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE,
    UNIQUE(permission_id, http_method, endpoint_path)
);


CREATE TABLE user_roles (
    user_id BINARY(16),
    role_id BINARY(16),
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE role_permissions (
    role_id BINARY(16),
    permission_id BINARY(16),
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id)
);

CREATE TABLE refresh_token (
    id BINARY(16) PRIMARY KEY,
    token VARCHAR(526),
    expiry_date DATETIME(6),
    user_id BINARY(16),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


insert into roles (role_id, role_name, description) values (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')),"USER", "");

insert into roles (role_id, role_name, description) values (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655449999', '-', '')),"ADMIN", "");

insert into roles (role_id, role_name, description) values (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440099', '-', '')),"GATEWAY", "");

insert into permissions values (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440098', '-', '')),"GET_PERMISSION", "");

insert into role_permissions values (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440099', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440098', '-', '')));

insert into permission_endpoints values (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440898', '-', '')), UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440098', '-', '')), "GET", "user-service", "/permissions/endpoint-user");

insert into users(user_id, username, password_hash) values (UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440998', '-', '')), "gateway", '$2a$10$3oUMfJyPcTEBDOiFY/8.aO5LAwtxiCbspd4RrvLmc4aNET3VhPyrG');

INSERT INTO user_roles (user_id, role_id)
VALUES (
    UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440998', '-', '')), -- user gateway
    UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440099', '-', ''))  -- role GATEWAY
);

SELECT p.permission_name
FROM user_roles ur
JOIN roles r ON ur.role_id = r.role_id
JOIN role_permissions rp on rp.role_id = r.role_id
JOIN permissions p on p.permission_id = rp.permission_id
JOIN users u on u.user_id = ur.user_id
WHERE u.username = "GATEWAY"

