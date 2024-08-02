CREATE SCHEMA IF NOT EXISTS social_media;

-- Create the user table
CREATE TABLE social_media."user" (
    id UUID PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role VARCHAR(50) NOT NULL
);