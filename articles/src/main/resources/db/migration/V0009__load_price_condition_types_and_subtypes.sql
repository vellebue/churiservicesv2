INSERT INTO C_PRICE_CONDITION_TYPES (condition_type, description, condition_key) VALUES
    ('COST', 'Cost Article Price',   'priceCondition.type.cost'),
    ('FARE', 'Base Article Price',   'priceCondition.type.fare'),
    ('DISC', 'Discount',              'priceCondition.type.discount'),
    ('TAXE', 'Tax Condition',         'priceCondition.type.tax'),
    ('PROV', 'Accounting Provision',  'priceCondition.type.prov');

INSERT INTO C_PRICE_CONDITION_SUBTYPES (condition_type, condition_subtype, description, subtype_key) VALUES
    ('COST', 'STDC', 'Standard cost',                   'priceCondition.subtype.cost.stdc'),
    ('FARE', 'FARA', 'Base Article Price for articles', 'priceCondition.subtype.fare.fara'),
    ('FARE', 'FARS', 'Base Article Price for services', 'priceCondition.subtype.fare.fars'),
    ('DISC', 'DISS', 'Standard discount',               'priceCondition.subtype.disc.diss'),
    ('DISC', 'DICO', 'Commercial discount',             'priceCondition.subtype.disc.dico'),
    ('TAXE', 'VAT',  'VAT Tax',                         'priceCondition.subtype.taxe.vat'),
    ('TAXE', 'PERS', 'Personal Tax',                    'priceCondition.subtype.taxe.pers'),
    ('TAXE', 'SPEC', 'Special Tax',                     'priceCondition.subtype.taxe.spec'),
    ('PROV', 'STDP', 'Standard provision',              'priceCondition.subtype.prov.stdp'),
    ('PROV', 'RAPP', 'Rappel provision',                'priceCondition.subtype.prov.rapp');
