-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:2 labels:permission,basic-table runOnChange:true

CREATE TABLE IF NOT EXISTS user_profile
(
    id character varying(255) DEFAULT random_uuid() NOT NULL PRIMARY KEY,
    activated boolean,
    address character varying(255),
    birthday date,
    display_name character varying(255),
    email character varying(255),
    first_name character varying(255),
    middle_name character varying(255),
    gender character varying(255),
    last_name character varying(255),
    password character varying(255),
    phone_number character varying(255),
    username character varying(255),
    additional_attributes json
);

CREATE TABLE IF NOT EXISTS attempted_login
(
    id character varying(255) DEFAULT random_uuid() NOT NULL PRIMARY KEY,
    account character varying(255),
    app_platform character varying(255),
    app_version jsonb,
    client_app_id character varying(255),
    date date,
    device_token character varying(255),
    enabled boolean NOT NULL,
    ip_address character varying(255),
    user_agent character varying(255),
    additional_info json
);

CREATE TABLE IF NOT EXISTS login_session
(
    id character varying(255) DEFAULT random_uuid() NOT NULL PRIMARY KEY,
    login_time timestamp(6) without time zone,
    login_token character varying(32600),
    refresh_token character varying(32600),
    valid_token boolean,
    user_profile_id character varying(255),
    verify_key character varying(255),
    additional_info json,
    input_account character varying(255),
    input_password character varying(255),
    CONSTRAINT fkb96uupl2xe8shjn1sca7mvijk FOREIGN KEY (user_profile_id) REFERENCES user_profile (id)
);

CREATE TABLE IF NOT EXISTS role
(
    id character varying(255) NOT NULL,
    name character varying(255),
    tenant_id character varying(255),
    additional_attributes jsonb,
    CONSTRAINT role_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_profile_role
(
    id character varying(255) DEFAULT random_uuid() NOT NULL,
    user_profile_id character varying(255) NOT NULL,
    role_id character varying(255) NOT NULL,
    CONSTRAINT user_profile_role_pkey PRIMARY KEY (id),
    CONSTRAINT user_profile_role_user_role_id_fkey FOREIGN KEY (role_id) REFERENCES role (id),
    CONSTRAINT user_profile_role_user_profile_id_fkey FOREIGN KEY (user_profile_id) REFERENCES user_profile (id),
    CONSTRAINT user_profile_role_user_profile_role_unique UNIQUE (role_id, user_profile_id)
);
