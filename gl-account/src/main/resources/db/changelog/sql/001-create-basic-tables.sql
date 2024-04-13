-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:1 labels:permission,basic-table runOnChange:true
CREATE SCHEMA IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff;

CREATE TABLE IF NOT EXISTS public.internal_service_config
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    api_key character varying(255) NOT NULL,
    created_date timestamp(6) with time zone,
    last_modified_date timestamp(6) with time zone,
    service_name character varying(255) NOT NULL,
    CONSTRAINT internal_service_config_activated_service_name_unique UNIQUE NULLS NOT DISTINCT (activated, service_name)
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
    CONSTRAINT permission_request_method_check CHECK (request_method::text = ANY (ARRAY['GET'::character varying, 'HEAD'::character varying, 'POST'::character varying, 'PUT'::character varying, 'PATCH'::character varying, 'DELETE'::character varying, 'OPTIONS'::character varying, 'TRACE'::character varying]::text[])),
    CONSTRAINT permission_action_control_method_resource_type_role_url_unique UNIQUE NULLS NOT DISTINCT (action, control, request_method, resource_type, role_id, url)
);

CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.permission
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    action character varying(255) NOT NULL,
    control character varying(255) NOT NULL,
    request_method character varying(255),
    resource_type character varying(255) NOT NULL,
    role_id character varying(255) NOT NULL,
    url character varying(255),
    CONSTRAINT permission_control_check CHECK (control::text = ANY (ARRAY['ALLOWED'::character varying, 'DENIED'::character varying, 'ALLOWED_SPECIFIC_RESOURCES'::character varying, 'DENIED_SPECIFIC_RESOURCES'::character varying, 'MANUAL_CHECK'::character varying]::text[])),
    CONSTRAINT permission_request_method_check CHECK (request_method::text = ANY (ARRAY['GET'::character varying, 'HEAD'::character varying, 'POST'::character varying, 'PUT'::character varying, 'PATCH'::character varying, 'DELETE'::character varying, 'OPTIONS'::character varying, 'TRACE'::character varying]::text[])),
    CONSTRAINT permission_action_control_method_resource_type_role_url_unique UNIQUE NULLS NOT DISTINCT (action, control, request_method, resource_type, role_id, url)
);

CREATE TABLE IF NOT EXISTS delete_tracking
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    entity_class_name character varying(255) NOT NULL,
    entity_data jsonb NOT NULL,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
     created_by jsonb,
     last_modified_by jsonb
);

CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.delete_tracking
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    entity_class_name character varying(255) NOT NULL,
    entity_data jsonb NOT NULL,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);
