-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:1 labels:permission,basic-table runOnChange:true

CREATE TABLE IF NOT EXISTS anonymous_url_access
(
    id VARCHAR(36) default random_uuid() PRIMARY KEY,
    url character varying(255),
    request_method character varying(255),
    CONSTRAINT url_method_unique UNIQUE (url, request_method)
);

CREATE TABLE IF NOT EXISTS internal_service_config
(
    id VARCHAR(36) default random_uuid() PRIMARY KEY,
    activated boolean NOT NULL,
    api_key character varying(255) NOT NULL,
    created_date timestamp(6) with time zone,
    last_modified_date timestamp(6) with time zone,
    service_name character varying(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS permission
(
    id VARCHAR(36) default random_uuid() PRIMARY KEY,
    action character varying(255) NOT NULL,
    control character varying(255) NOT NULL,
    request_method character varying(255),
    resource_type character varying(255) NOT NULL,
    role_id character varying(255) NOT NULL,
    url character varying(255)
);

CREATE TABLE IF NOT EXISTS delete_tracking
(
    id character varying(255) DEFAULT random_uuid() PRIMARY KEY,
    entity_class_name character varying(255) NOT NULL,
    entity_data jsonb NOT NULL,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);