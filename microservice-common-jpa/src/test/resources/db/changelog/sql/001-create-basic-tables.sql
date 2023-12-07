-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:1 labels:permission,basic-table runOnChange:true

CREATE TABLE IF NOT EXISTS anonymous_url_access
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    url character varying(255),
    request_method character varying(255),
    CONSTRAINT anonymous_url_access_request_method_check CHECK (request_method::text = ANY (ARRAY['GET'::character varying, 'HEAD'::character varying, 'POST'::character varying, 'PUT'::character varying, 'PATCH'::character varying, 'DELETE'::character varying, 'OPTIONS'::character varying, 'TRACE'::character varying]::text[])),
    CONSTRAINT url_method_unique UNIQUE NULLS NOT DISTINCT (url, request_method)
);

CREATE TABLE IF NOT EXISTS resource_action
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    action character varying(255),
    request_method character varying(255),
    resource_type character varying(255),
    url character varying(255),
    CONSTRAINT resource_action_request_method_check CHECK (request_method::text = ANY (ARRAY['GET'::character varying, 'HEAD'::character varying, 'POST'::character varying, 'PUT'::character varying, 'PATCH'::character varying, 'DELETE'::character varying, 'OPTIONS'::character varying, 'TRACE'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS internal_service_config
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    api_key character varying(255) NOT NULL,
    created_date timestamp(6) with time zone,
    last_modified_date timestamp(6) with time zone,
    service_name character varying(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS permission
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    action character varying(255) NOT NULL,
    control character varying(255) NOT NULL,
    request_method character varying(255),
    resource_type character varying(255) NOT NULL,
    role_id character varying(255) NOT NULL,
    url character varying(255),
    CONSTRAINT permission_control_check CHECK (control::text = ANY (ARRAY['ALLOWED'::character varying, 'DENIED'::character varying, 'ALLOWED_SPECIFIC_RESOURCES'::character varying, 'DENIED_SPECIFIC_RESOURCES'::character varying, 'MANUAL_CHECK'::character varying]::text[])),
    CONSTRAINT permission_request_method_check CHECK (request_method::text = ANY (ARRAY['GET'::character varying, 'HEAD'::character varying, 'POST'::character varying, 'PUT'::character varying, 'PATCH'::character varying, 'DELETE'::character varying, 'OPTIONS'::character varying, 'TRACE'::character varying]::text[]))
);