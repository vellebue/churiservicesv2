CREATE SEQUENCE addresses_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE customers_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE customer_delegations_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE ADDRESS_TYPES (
    address_type VARCHAR(10) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    address_key VARCHAR(50) NOT NULL
);

CREATE TABLE ADDRESSES (
    address_id BIGINT NOT NULL DEFAULT nextval('addresses_seq') PRIMARY KEY,
    address_type VARCHAR(10) NOT NULL REFERENCES ADDRESS_TYPES(address_type),
    name VARCHAR(1024) NOT NULL,
    address VARCHAR(1024) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(2) NOT NULL,
    region VARCHAR(10) NOT NULL
);

CREATE TABLE CUSTOMERS (
    customer_id BIGINT NOT NULL DEFAULT nextval('customers_seq') PRIMARY KEY,
    commercial_name VARCHAR(512) NOT NULL,
    social_name VARCHAR(1024) NOT NULL,
    vat_number VARCHAR(20) NOT NULL,
    customer_address_id BIGINT NOT NULL REFERENCES ADDRESSES(address_id),
    social_address_id BIGINT NOT NULL REFERENCES ADDRESSES(address_id),
    billing_address_id BIGINT REFERENCES ADDRESSES(address_id)
);

CREATE TABLE CUSTOMER_DELEGATIONS (
    delegation_id BIGINT NOT NULL DEFAULT nextval('customer_delegations_seq') PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES CUSTOMERS(customer_id) ON DELETE CASCADE,
    order_id INTEGER NOT NULL,
    name VARCHAR(512) NOT NULL,
    address_id BIGINT NOT NULL REFERENCES ADDRESSES(address_id),
    billing_address_id BIGINT REFERENCES ADDRESSES(address_id),
    CONSTRAINT uk_customer_delegation_order UNIQUE (customer_id, order_id)
);
