-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:2 labels:anonymous_url_data runOnChange:true

CREATE TABLE IF NOT EXISTS anonymous_url_access
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    url character varying(255),
    request_method character varying(255),
    CONSTRAINT anonymous_url_access_request_method_check CHECK (request_method::text = ANY (ARRAY['GET'::character varying, 'HEAD'::character varying, 'POST'::character varying, 'PUT'::character varying, 'PATCH'::character varying, 'DELETE'::character varying, 'OPTIONS'::character varying, 'TRACE'::character varying]::text[])),
    CONSTRAINT url_method_unique UNIQUE NULLS NOT DISTINCT (url, request_method)
);

INSERT INTO anonymous_url_access (url) VALUES ('/') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/parserConfigurationException') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/error') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/swagger-ui.html') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/favicon.ico') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/swagger-ui/*') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/v3/api-docs') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/v3/api-docs/*') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/actuator') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/actuator/*') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/index') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/authentication/login') ON CONFLICT DO NOTHING;
INSERT INTO anonymous_url_access (url) VALUES ('/authentication/refresh-token') ON CONFLICT DO NOTHING;