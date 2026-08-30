CREATE TABLE C_PRICE_CONDITION_TYPES (
    condition_type VARCHAR(4) PRIMARY KEY,
    description VARCHAR(256) NOT NULL,
    key VARCHAR(100) NOT NULL
);

INSERT INTO C_PRICE_CONDITION_TYPES (condition_type, description, key) VALUES ('COST', 'Cost Article Price', 'priceCondition.type.cost');
INSERT INTO C_PRICE_CONDITION_TYPES (condition_type, description, key) VALUES ('FARE', 'Base Article Price', 'priceCondition.type.fare');
INSERT INTO C_PRICE_CONDITION_TYPES (condition_type, description, key) VALUES ('DISC', 'Discount', 'priceCondition.type.discount');
INSERT INTO C_PRICE_CONDITION_TYPES (condition_type, description, key) VALUES ('TAXE', 'Tax Condition', 'priceCondition.type.tax');
INSERT INTO C_PRICE_CONDITION_TYPES (condition_type, description, key) VALUES ('PROV', 'Accounting Provision', 'priceCondition.type.prov');
