CREATE TABLE COUNTRIES (
    country_id VARCHAR(2) PRIMARY KEY,
    description VARCHAR(256) NOT NULL,
    country_key VARCHAR(20) NOT NULL
);

CREATE TABLE COUNTRY_REGIONS (
    country_id VARCHAR(2) NOT NULL REFERENCES COUNTRIES(country_id),
    region_id VARCHAR(20) NOT NULL,
    description VARCHAR(256) NOT NULL,
    region_key VARCHAR(50) NOT NULL,
    PRIMARY KEY (country_id, region_id)
);
