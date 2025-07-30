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

