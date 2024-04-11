-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:1 labels:permission,basic-table runOnChange:true

CREATE SCHEMA IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff;

CREATE TABLE IF NOT EXISTS public.resource_action
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    action character varying(255),
    request_method character varying(255),
    resource_type character varying(255),
    url character varying(255),
    CONSTRAINT resource_action_request_method_check CHECK (request_method::text = ANY (ARRAY['GET'::character varying, 'HEAD'::character varying, 'POST'::character varying, 'PUT'::character varying, 'PATCH'::character varying, 'DELETE'::character varying, 'OPTIONS'::character varying, 'TRACE'::character varying]::text[])),
    CONSTRAINT resource_action_action_method_type_url_unique UNIQUE NULLS NOT DISTINCT (action, request_method, resource_type, url)
);

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

CREATE TABLE IF NOT EXISTS rate_source
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    note character varying(255),
    type character varying(255),
    CONSTRAINT rate_source_type_check CHECK (type::text = ANY (ARRAY['INTEREST'::character varying, 'WITHHOLDING_TAX'::character varying, 'VALUE_ADDED_TAX'::character varying]::text[]))
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.rate_source
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    note character varying(255),
    type character varying(255),
    CONSTRAINT rate_source_type_check CHECK (type::text = ANY (ARRAY['INTEREST'::character varying, 'WITHHOLDING_TAX'::character varying, 'VALUE_ADDED_TAX'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS public.currency
(
    id character varying(255) PRIMARY KEY,
    symbol character varying(255) NOT NULL,
    decimal_mark character varying(10) DEFAULT '.' NOT NULL ,
    symbol_at_beginning boolean NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS public.organization
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    city character varying(255),
    country character varying(255),
    currency_id character varying(255),
    decimal_mark character varying(255),
    email character varying(255),
    icon_url character varying(255),
    local_date_format character varying(255),
    local_date_time_format character varying(255),
    logo_url character varying(255),
    name character varying(255),
    non_working_days jsonb,
    phone_number character varying(255),
    state character varying(255),
    street_address_line_1 character varying(255),
    timezone character varying(255),
    zip_postal_code character varying(255),
    CONSTRAINT currency_foreign_key FOREIGN KEY (currency_id) REFERENCES currency (id) MATCH FULL
        ON UPDATE RESTRICT ON DELETE RESTRICT,
    CONSTRAINT organization_name_unique UNIQUE NULLS NOT DISTINCT (name)
);

CREATE TABLE IF NOT EXISTS branch
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    city character varying(255),
    country character varying(255),
    email character varying(255),
    name character varying(255),
    parent_branch_id character varying(255),
    phone_number character varying(255),
    state character varying(255),
    street_address_line_1 character varying(255),
    zip_postal_code character varying(255),
    non_working_days jsonb,
    inherit_non_working_days boolean NOT NULL default true,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.branch
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    city character varying(255),
    country character varying(255),
    email character varying(255),
    name character varying(255),
    parent_branch_id character varying(255),
    phone_number character varying(255),
    state character varying(255),
    street_address_line_1 character varying(255),
    zip_postal_code character varying(255),
    non_working_days jsonb,
    inherit_non_working_days boolean NOT NULL default true,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);

CREATE TABLE IF NOT EXISTS product_category
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    type character varying(255),
    CONSTRAINT product_category_type_check CHECK (type::text = ANY (ARRAY['DEPOSIT'::character varying, 'LOAN'::character varying, 'GL'::character varying, 'CRYPTO'::character varying]::text[]))
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_category
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    type character varying(255),
    CONSTRAINT product_category_type_check CHECK (type::text = ANY (ARRAY['DEPOSIT'::character varying, 'LOAN'::character varying, 'GL'::character varying, 'CRYPTO'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS product_type
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    type character varying(255),
    CONSTRAINT product_type_type_check CHECK (type::text = ANY (ARRAY['DEPOSIT'::character varying, 'LOAN'::character varying, 'GL'::character varying, 'CRYPTO'::character varying]::text[]))
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    type character varying(255),
    CONSTRAINT product_type_type_check CHECK (type::text = ANY (ARRAY['DEPOSIT'::character varying, 'LOAN'::character varying, 'GL'::character varying, 'CRYPTO'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS holiday
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    description character varying(255),
    holiday_date date,
    to_date date,
    repeat_yearly boolean,
    date_range boolean
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.holiday
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    description character varying(255),
    holiday_date date,
    to_date date,
    repeat_yearly boolean,
    date_range boolean
);

CREATE TABLE IF NOT EXISTS crypto_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    product_fees jsonb,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    allow_deposit_after_maturity_date boolean,
    allow_overdrafts boolean,
    days_to_set_to_dormant integer,
    default_term_length integer,
    deposit_limits jsonb,
    early_closure_period integer,
    interest_rate jsonb,
    max_overdraft_limit jsonb,
    max_term_length integer,
    min_term_length integer,
    overdrafts_interest jsonb,
    overdrafts_under_credit_arrangement_managed character varying(255),
    term_unit character varying(255),
    withdrawal_limits jsonb,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    enable_term_deposit boolean NOT NULL default false,
    enable_interest_rate boolean NOT NULL default false,
    CONSTRAINT crypto_product_overdrafts_under_credit_arrangement_manag_check CHECK (overdrafts_under_credit_arrangement_managed::text = ANY (ARRAY['REQUIRED'::character varying, 'NO'::character varying, 'OPTIONAL'::character varying]::text[])),
    CONSTRAINT crypto_product_term_unit_check CHECK (term_unit::text = ANY (ARRAY['DAY'::character varying, 'WEEK'::character varying, 'MONTH'::character varying]::text[]))
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.crypto_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    product_fees jsonb,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    allow_deposit_after_maturity_date boolean,
    allow_overdrafts boolean,
    days_to_set_to_dormant integer,
    default_term_length integer,
    deposit_limits jsonb,
    early_closure_period integer,
    interest_rate jsonb,
    max_overdraft_limit jsonb,
    max_term_length integer,
    min_term_length integer,
    overdrafts_interest jsonb,
    overdrafts_under_credit_arrangement_managed character varying(255),
    term_unit character varying(255),
    withdrawal_limits jsonb,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    enable_term_deposit boolean NOT NULL default false,
    enable_interest_rate boolean NOT NULL default false,
    CONSTRAINT crypto_product_overdrafts_under_credit_arrangement_manag_check CHECK (overdrafts_under_credit_arrangement_managed::text = ANY (ARRAY['REQUIRED'::character varying, 'NO'::character varying, 'OPTIONAL'::character varying]::text[])),
    CONSTRAINT crypto_product_term_unit_check CHECK (term_unit::text = ANY (ARRAY['DAY'::character varying, 'WEEK'::character varying, 'MONTH'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS deposit_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    product_fees jsonb,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    allow_deposit_after_maturity_date boolean,
    allow_overdrafts boolean,
    days_to_set_to_dormant integer,
    default_term_length integer,
    deposit_limits jsonb,
    early_closure_period integer,
    interest_rate jsonb,
    max_overdraft_limit jsonb,
    max_term_length integer,
    min_term_length integer,
    overdrafts_interest jsonb,
    overdrafts_under_credit_arrangement_managed character varying(255),
    term_unit character varying(255),
    withdrawal_limits jsonb,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    enable_term_deposit boolean NOT NULL default false,
    enable_interest_rate boolean NOT NULL default false,
    CONSTRAINT deposit_product_overdrafts_under_credit_arrangement_manag_check CHECK (overdrafts_under_credit_arrangement_managed::text = ANY (ARRAY['REQUIRED'::character varying, 'NO'::character varying, 'OPTIONAL'::character varying]::text[])),
    CONSTRAINT deposit_product_term_unit_check CHECK (term_unit::text = ANY (ARRAY['DAY'::character varying, 'WEEK'::character varying, 'MONTH'::character varying]::text[]))
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.deposit_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    product_fees jsonb,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    allow_deposit_after_maturity_date boolean,
    allow_overdrafts boolean,
    days_to_set_to_dormant integer,
    default_term_length integer,
    deposit_limits jsonb,
    early_closure_period integer,
    interest_rate jsonb,
    max_overdraft_limit jsonb,
    max_term_length integer,
    min_term_length integer,
    overdrafts_interest jsonb,
    overdrafts_under_credit_arrangement_managed character varying(255),
    term_unit character varying(255),
    withdrawal_limits jsonb,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    enable_term_deposit boolean NOT NULL default false,
    enable_interest_rate boolean NOT NULL default false,
    CONSTRAINT deposit_product_overdrafts_under_credit_arrangement_manag_check CHECK (overdrafts_under_credit_arrangement_managed::text = ANY (ARRAY['REQUIRED'::character varying, 'NO'::character varying, 'OPTIONAL'::character varying]::text[])),
    CONSTRAINT deposit_product_term_unit_check CHECK (term_unit::text = ANY (ARRAY['DAY'::character varying, 'WEEK'::character varying, 'MONTH'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS exchange_rate
(
    from_currency character varying(255) NOT NULL,
    to_currency character varying(255) NOT NULL,
    buy_rate double precision,
    name character varying(255),
    sell_rate double precision,
    margin double precision,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    CONSTRAINT exchange_rate_pk PRIMARY KEY (from_currency, to_currency)
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.exchange_rate
(
    from_currency character varying(255) NOT NULL,
    to_currency character varying(255) NOT NULL,
    buy_rate double precision,
    name character varying(255),
    sell_rate double precision,
    margin double precision,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    CONSTRAINT exchange_rate_pk PRIMARY KEY (from_currency, to_currency)
);

CREATE TABLE IF NOT EXISTS gl_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.gl_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);

CREATE TABLE IF NOT EXISTS loan_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    product_fees jsonb,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    arrears_setting jsonb NOT NULL,
    cap_charges boolean NOT NULL,
    close_dormant_accounts boolean NOT NULL,
    interest_rate jsonb NOT NULL,
    loan_values jsonb NOT NULL,
    lock_arrears_accounts boolean NOT NULL,
    penalty_setting jsonb NOT NULL,
    enable_guarantors boolean NOT NULL,
    enable_collateral boolean NOT NULL,
    percent_security_per_loan double precision,
    repayment_collection jsonb NOT NULL,
    repayment_scheduling jsonb NOT NULL,
    under_credit_arrangement_managed character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    CONSTRAINT loan_product_under_credit_arrangement_managed_check CHECK (under_credit_arrangement_managed::text = ANY (ARRAY['REQUIRED'::character varying, 'NO'::character varying, 'OPTIONAL'::character varying]::text[]))
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.loan_product
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    activated boolean NOT NULL,
    allow_arbitrary_fees boolean NOT NULL,
    category character varying(255) NOT NULL,
    currencies character varying[] NOT NULL,
    description character varying(255),
    name character varying(255),
    new_account_setting jsonb NOT NULL,
    product_availabilities jsonb NOT NULL,
    product_fees jsonb,
    show_inactive_fees boolean NOT NULL,
    type character varying(255),
    arrears_setting jsonb NOT NULL,
    cap_charges boolean NOT NULL,
    close_dormant_accounts boolean NOT NULL,
    interest_rate jsonb NOT NULL,
    loan_values jsonb NOT NULL,
    lock_arrears_accounts boolean NOT NULL,
    penalty_setting jsonb NOT NULL,
    enable_guarantors boolean NOT NULL,
    enable_collateral boolean NOT NULL,
    percent_security_per_loan double precision,
    repayment_collection jsonb NOT NULL,
    repayment_scheduling jsonb NOT NULL,
    under_credit_arrangement_managed character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    CONSTRAINT loan_product_under_credit_arrangement_managed_check CHECK (under_credit_arrangement_managed::text = ANY (ARRAY['REQUIRED'::character varying, 'NO'::character varying, 'OPTIONAL'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS rate
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    note character varying(255),
    rate_value double precision,
    type character varying(255),
    valid_from timestamp(6) with time zone,
    rate_source_id character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    CONSTRAINT fkg77aavhjtgcybvjt5pe70hbw2 FOREIGN KEY (rate_source_id)
        REFERENCES rate_source (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT rate_type_check CHECK (type::text = ANY (ARRAY['INTEREST'::character varying, 'WITHHOLDING_TAX'::character varying, 'VALUE_ADDED_TAX'::character varying]::text[]))
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.rate
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    note character varying(255),
    rate_value double precision,
    type character varying(255),
    valid_from timestamp(6) with time zone,
    rate_source_id character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    CONSTRAINT fkg77aavhjtgcybvjt5pe70hbw2 FOREIGN KEY (rate_source_id)
        REFERENCES rate_source (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT rate_type_check CHECK (type::text = ANY (ARRAY['INTEREST'::character varying, 'WITHHOLDING_TAX'::character varying, 'VALUE_ADDED_TAX'::character varying]::text[]))
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
