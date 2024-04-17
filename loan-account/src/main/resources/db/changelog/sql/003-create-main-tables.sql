-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:main-table runOnChange:true

CREATE TABLE IF NOT EXISTS loan_account
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
    customer_type character varying(255) NOT NULL,
    customer_id bigint NOT NULL,
    product_fees jsonb,
    loan_applied_values jsonb,
    under_credit_arrangement_managed character varying(255),
    account_interest_rate jsonb,
    account_repayment_scheduling jsonb,
    repayment_collection jsonb,
    account_arrears_setting jsonb,
    account_penalty_setting jsonb,
    close_dormant_accounts boolean NOT NULL,
    lock_arrears_accounts boolean NOT NULL,
    cap_charges boolean NOT NULL,
    enable_guarantors boolean NOT NULL,
    enable_collateral boolean NOT NULL,
    percent_security_per_loan double precision
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.loan_account
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
    customer_type character varying(255) NOT NULL,
    customer_id bigint NOT NULL,
    product_fees jsonb,
    loan_applied_values jsonb,
    under_credit_arrangement_managed character varying(255),
    account_interest_rate jsonb,
    account_repayment_scheduling jsonb,
    repayment_collection jsonb,
    account_arrears_setting jsonb,
    account_penalty_setting jsonb,
    close_dormant_accounts boolean NOT NULL,
    lock_arrears_accounts boolean NOT NULL,
    cap_charges boolean NOT NULL,
    enable_guarantors boolean NOT NULL,
    enable_collateral boolean NOT NULL,
    percent_security_per_loan double precision
);

CREATE TABLE IF NOT EXISTS account_balance
(
    account_id character varying(255) NOT NULL,
    currency character varying(255) NOT NULL,
    account_type character varying(255) NOT NULL,
    amount double precision NOT NULL,
    CONSTRAINT account_balance_pk PRIMARY KEY(account_id, currency, account_type)
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.account_balance
(
    account_id character varying(255) NOT NULL,
    currency character varying(255) NOT NULL,
    account_type character varying(255) NOT NULL,
    amount double precision NOT NULL,
    CONSTRAINT account_balance_pk PRIMARY KEY(account_id, currency, account_type)
);

CREATE TABLE IF NOT EXISTS loan_transaction
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
    loan_account_id character varying(255) NOT NULL,
    loan_customer_type character varying(255) NOT NULL,
    loan_customer_id bigint NOT NULL
);
CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.loan_transaction
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
    loan_account_id character varying(255) NOT NULL,
    loan_customer_type character varying(255) NOT NULL,
    loan_customer_id bigint NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS loan_account_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
CREATE SEQUENCE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.loan_account_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
