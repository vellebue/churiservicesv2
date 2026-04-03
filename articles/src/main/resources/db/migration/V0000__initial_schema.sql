-- Initial empty schema for Articles
CREATE TABLE IF NOT EXISTS flyway_init_marker (
    id SERIAL PRIMARY KEY,
    initialized_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
