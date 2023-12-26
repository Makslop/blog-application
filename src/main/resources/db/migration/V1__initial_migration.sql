CREATE TABLE IF NOT EXISTS post
(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    body VARCHAR(5000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    account_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS authority
(
    name VARCHAR(16) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS account
(
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS account_authority
(
    account_id BIGINT,
    authority_name VARCHAR(16),
    PRIMARY KEY(account_id, authority_name),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (authority_name) REFERENCES authority(name)
);
CREATE TABLE IF NOT EXISTS comment
(
    id SERIAL PRIMARY KEY,
    text VARCHAR(5000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    post_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (account_id) REFERENCES account(id)
);

