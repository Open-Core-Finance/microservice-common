-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:main-table runOnChange:true

CREATE TABLE IF NOT EXISTS deposit_account
(
    id character varying(255) PRIMARY KEY,
    name character varying(255) NOT NULL,
    category_id character varying(255) NOT NULL,
    category_name character varying(255) NOT NULL,
    type_id character varying(255) NOT NULL,
    type_name character varying(255) NOT NULL,
    description character varying(255),
    status character varying(255),
    account_fees jsonb,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    supported_currencies character varying[] NOT NULL,
    product_id character varying(255) NOT NULL,
    allow_deposit_after_maturity_date boolean NOT NULL default false,
    allow_overdrafts boolean NOT NULL default false,
    days_to_set_to_dormant integer,
    term_length integer,
    deposit_limits jsonb,
    early_closure_period integer,
    interest_rate jsonb,
    max_overdraft_limits jsonb,
    overdrafts_interest jsonb,
    overdrafts_under_credit_arrangement_managed character varying(255),
    term_unit character varying(255),
    withdrawal_limits jsonb,
    enable_term_deposit boolean NOT NULL default false,
    customer_type character varying(255) NOT NULL,
    customer_id bigint NOT NULL
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.deposit_account
(
    id character varying(255) PRIMARY KEY,
    name character varying(255) NOT NULL,
    category_id character varying(255) NOT NULL,
    category_name character varying(255) NOT NULL,
    type_id character varying(255) NOT NULL,
    type_name character varying(255) NOT NULL,
    description character varying(255),
    status character varying(255),
    account_fees jsonb,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    supported_currencies character varying[] NOT NULL,
    product_id character varying(255) NOT NULL,
    allow_deposit_after_maturity_date boolean NOT NULL default false,
    allow_overdrafts boolean NOT NULL default false,
    days_to_set_to_dormant integer,
    term_length integer,
    deposit_limits jsonb,
    early_closure_period integer,
    interest_rate jsonb,
    max_overdraft_limits jsonb,
    overdrafts_interest jsonb,
    overdrafts_under_credit_arrangement_managed character varying(255),
    term_unit character varying(255),
    withdrawal_limits jsonb,
    enable_term_deposit boolean NOT NULL default false,
    customer_type character varying(255) NOT NULL,
    customer_id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS deposit_account_balance
(
    account_id character varying(255) NOT NULL,
    currency character varying(255) NOT NULL,
    account_type character varying(255) NOT NULL,
    amount double precision NOT NULL,
    CONSTRAINT deposit_account_balance_pk PRIMARY KEY(account_id, currency, account_type)
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.deposit_account_balance
(
    account_id character varying(255) NOT NULL,
    currency character varying(255) NOT NULL,
    account_type character varying(255) NOT NULL,
    amount double precision NOT NULL,
    CONSTRAINT deposit_account_balance_pk PRIMARY KEY(account_id, currency, account_type)
);

CREATE TABLE IF NOT EXISTS deposit_transaction
(
    id character varying(255) PRIMARY KEY,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    trans_date timestamp with time zone,
    amount double precision NOT NULL,
    vat double precision NOT NULL,
    total_amount double precision NOT NULL,
    currency character varying(255) NOT NULL,
    target_currency character varying(255) NOT NULL,
    transaction_fees jsonb,
    total_fee_amount double precision NOT NULL,
    total_fee_vat_amount double precision NOT NULL,
    total_fee_vat_and_fee_amount double precision NOT NULL,
    transaction_side character varying(255),
    before_tran_amount double precision NOT NULL,
    after_tran_amount double precision NOT NULL,
    applied_exchange_rate double precision NOT NULL,
    memo character varying(255),
    counter_account_id character varying(255) NOT NULL,
    counter_account_type character varying(255) NOT NULL,
    counter_customer_type character varying(255) NOT NULL,
    counter_customer_id bigint NOT NULL,
    transaction_type character varying(255) NOT NULL,
    transaction_code character varying(255) NOT NULL,
    terminal_id character varying(255),
    request_app_id character varying(255),
    request_channel_id character varying(255),
    gl_account_id character varying(255) NOT NULL,
    deposit_account_id character varying(255) NOT NULL,
    deposit_customer_type character varying(255) NOT NULL,
    deposit_customer_id bigint NOT NULL
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.deposit_transaction
(
    id character varying(255) PRIMARY KEY,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    trans_date timestamp with time zone,
    amount double precision NOT NULL,
    vat double precision NOT NULL,
    total_amount double precision NOT NULL,
    currency character varying(255) NOT NULL,
    target_currency character varying(255) NOT NULL,
    transaction_fees jsonb,
    total_fee_amount double precision NOT NULL,
    total_fee_vat_amount double precision NOT NULL,
    total_fee_vat_and_fee_amount double precision NOT NULL,
    transaction_side character varying(255),
    before_tran_amount double precision NOT NULL,
    after_tran_amount double precision NOT NULL,
    applied_exchange_rate double precision NOT NULL,
    memo character varying(255),
    counter_account_id character varying(255) NOT NULL,
    counter_account_type character varying(255) NOT NULL,
    counter_customer_type character varying(255) NOT NULL,
    counter_customer_id bigint NOT NULL,
    transaction_type character varying(255) NOT NULL,
    transaction_code character varying(255) NOT NULL,
    terminal_id character varying(255),
    request_app_id character varying(255),
    request_channel_id character varying(255),
    gl_account_id character varying(255) NOT NULL,
    deposit_account_id character varying(255) NOT NULL,
    deposit_customer_type character varying(255) NOT NULL,
    deposit_customer_id bigint NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS deposit_account_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
CREATE SEQUENCE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.deposit_account_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
