CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(1024),
    email VARCHAR(1024) UNIQUE,
    CONSTRAINT user_id_pk PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    name VARCHAR(512) UNIQUE,
    CONSTRAINT category_id_pk PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(category_id),
    create_date TIMESTAMP WITHOUT TIME ZONE,
    description VARCHAR(4000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT NOT NULL REFERENCES users(user_id),
    lat NUMERIC,
    lon NUMERIC,
    paid BOOLEAN,
    participant_limit INTEGER NOT NULL,
    published_date TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state VARCHAR(255) NOT NULL,
    title VARCHAR(120) NOT NULL,
    views INTEGER,
    CONSTRAINT event_id_pk PRIMARY KEY (event_id)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    event_id BIGINT NOT NULL REFERENCES events(event_id),
    requester_id BIGINT NOT NULL REFERENCES users(user_id),
    created TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(50),
    CONSTRAINT request_id_pk PRIMARY KEY (request_id)
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_event_id_requester_id
ON requests(event_id, requester_id);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    pinned BOOLEAN,
    title VARCHAR(512),
    CONSTRAINT compilation_id_pk PRIMARY KEY (compilation_id)
);

CREATE TABLE IF NOT EXISTS event_compilation (
    event_id BIGINT NOT NULL REFERENCES events(event_id) ON DELETE CASCADE,
    compilation_id BIGINT NOT NULL REFERENCES compilations(compilation_id) ON DELETE CASCADE,
    CONSTRAINT event_compilation_pk PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    text VARCHAR(4000),
    user_id BIGINT NOT NULL REFERENCES users (user_id),
    event_id BIGINT NOT NULL REFERENCES events (event_id),
    create_date TIMESTAMP WITHOUT TIME ZONE
);