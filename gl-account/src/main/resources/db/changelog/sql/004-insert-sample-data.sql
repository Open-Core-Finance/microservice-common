-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:4 labels:sample-data runOnChange:true

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.gl_account (id,"name",category_id,category_name,type_id,type_name,description,status,created_date,last_modified_date,created_by,last_modified_by,supported_currencies,product_id)
VALUES
    ('000000001','Deposit GL 01','43b9a203-143f-4d9d-9f2b-3cfd1056445d','Generic GL','2f24311e-4331-4be9-8066-4475eff5b80b','Deposit GL','GL for deposit',
     'NEW','2024-04-06 23:49:52.722','2024-04-06 23:49:52.722',
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{EUR,AUD,CNY}','a711c6f3-cad2-4848-b320-245f3c25be76'),
    ('ba8940df-70ea-4118-8bbb-da3d97e40702','Fee GL 01','43b9a203-143f-4d9d-9f2b-3cfd1056445d','Generic GL','d3db8acd-e9b4-4f30-b497-4b88f05a17d0','Free GL','GL for fees',
     'NEW','2024-04-06 23:50:08.356','2024-04-06 23:50:08.356',
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{USD,JPY,EUR}','f8db0f92-5f79-41f9-8536-1722e250f19a'),
    ('SYY750383','Loan GL 01','43b9a203-143f-4d9d-9f2b-3cfd1056445d','Generic GL','b2b79cf9-ad62-4027-8a6f-e2dc54c3132b','Loan GL','GL for loan',
     'NEW','2024-04-06 23:50:25.687','2024-04-06 23:50:25.687',
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{USD,GBP,JPY}','ccc7b7c7-90b9-48de-9f88-cc644eff8861') ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.gl_account (id, "name", category_id, category_name, type_id, type_name, description, status, created_date, last_modified_date, created_by, last_modified_by,
                                                                    supported_currencies, product_id)
VALUES
    ('CRYPTO000010', 'Crypto GL 01', '43b9a203-143f-4d9d-9f2b-3cfd1056445d', 'Generic GL', 'dcca66d6-090d-4384-922a-aa98e0254ddf', 'Crypto GL', 'GL for crypto', 'NEW', '2024-04-07 00:01:48.855', '2024-04-07 00:01:48.855',
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{BTC,ETH}', 'e99c7669-7e79-4c96-ba84-343fc18bf010') ON CONFLICT DO NOTHING;

ALTER SEQUENCE tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.gl_account_id_seq RESTART 10;
