-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:anonymous_url_data runOnChange:true

CREATE TABLE IF NOT EXISTS anonymous_url_access
(
    id character varying(255) DEFAULT random_uuid() PRIMARY KEY,
    url character varying(255),
    request_method character varying(255),
    CONSTRAINT url_method_unique UNIQUE (url, request_method)
);

MERGE INTO anonymous_url_access (id, url) VALUES ('1', '/');
MERGE INTO anonymous_url_access (id, url) VALUES ('2', '/parserConfigurationException');
MERGE INTO anonymous_url_access (id, url) VALUES ('3', '/error');
MERGE INTO anonymous_url_access (id, url) VALUES ('4', '/swagger-ui.html');
MERGE INTO anonymous_url_access (id, url) VALUES ('5', '/favicon.ico');
MERGE INTO anonymous_url_access (id, url) VALUES ('6', '/swagger-ui/*');
MERGE INTO anonymous_url_access (id, url) VALUES ('7', '/v3/api-docs');
MERGE INTO anonymous_url_access (id, url) VALUES ('8', '/v3/api-docs/*');
MERGE INTO anonymous_url_access (id, url) VALUES ('9', '/actuator');
MERGE INTO anonymous_url_access (id, url) VALUES ('10', '/actuator/*');
MERGE INTO anonymous_url_access (id, url) VALUES ('11', '/index');
MERGE INTO anonymous_url_access (id, url) VALUES ('12', '/authentication/login');
MERGE INTO anonymous_url_access (id, url) VALUES ('13', '/authentication/refresh-token');