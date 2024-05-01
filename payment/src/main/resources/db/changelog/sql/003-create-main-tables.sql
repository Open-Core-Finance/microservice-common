-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:main-table runOnChange:true

CREATE TABLE IF NOT EXISTS internal_fund_transfer
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    from_customer_type character varying(255) NOT NULL,
    from_customer_id bigint NOT NULL,
    from_account_id character varying(255) NOT NULL,
    from_account_type character varying(255) NOT NULL,
    from_currency character varying(255) NOT NULL,
    from_amount double precision NOT NULL,
    to_customer_type character varying(255) NOT NULL,
    to_customer_id bigint NOT NULL,
    to_account_id character varying(255) NOT NULL,
    to_account_type character varying(255) NOT NULL,
    to_currency character varying(255) NOT NULL,
    to_amount double precision NOT NULL,
    CONSTRAINT internal_fund_transfer_from_customer_type_check CHECK (from_customer_type::text = ANY (ARRAY['INDIVIDUAL'::character varying, 'CORPORATE'::character varying]::text[])),
    CONSTRAINT internal_fund_transfer_to_customer_type_check CHECK (to_customer_type::text = ANY (ARRAY['INDIVIDUAL'::character varying, 'CORPORATE'::character varying]::text[])),
    CONSTRAINT internal_fund_transfer_from_account_type_check CHECK (from_account_type::text = ANY (ARRAY['DEPOSIT'::character varying, 'CRYPTO'::character varying, 'LOAN'::character varying, 'GL'::character varying]::text[])),
    CONSTRAINT internal_fund_transfer_to_account_type_check CHECK (to_account_type::text = ANY (ARRAY['DEPOSIT'::character varying, 'CRYPTO'::character varying, 'LOAN'::character varying, 'GL'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.internal_fund_transfer
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    from_customer_type character varying(255) NOT NULL,
    from_customer_id bigint NOT NULL,
    from_account_id character varying(255) NOT NULL,
    from_account_type character varying(255) NOT NULL,
    from_currency character varying(255) NOT NULL,
    from_amount double precision NOT NULL,
    to_customer_type character varying(255) NOT NULL,
    to_customer_id bigint NOT NULL,
    to_account_id character varying(255) NOT NULL,
    to_account_type character varying(255) NOT NULL,
    to_currency character varying(255) NOT NULL,
    to_amount double precision NOT NULL,
    CONSTRAINT internal_fund_transfer_from_customer_type_check CHECK (from_customer_type::text = ANY (ARRAY['INDIVIDUAL'::character varying, 'CORPORATE'::character varying]::text[])),
    CONSTRAINT internal_fund_transfer_to_customer_type_check CHECK (to_customer_type::text = ANY (ARRAY['INDIVIDUAL'::character varying, 'CORPORATE'::character varying]::text[])),
    CONSTRAINT internal_fund_transfer_from_account_type_check CHECK (from_account_type::text = ANY (ARRAY['DEPOSIT'::character varying, 'CRYPTO'::character varying, 'LOAN'::character varying, 'GL'::character varying]::text[])),
    CONSTRAINT internal_fund_transfer_to_account_type_check CHECK (to_account_type::text = ANY (ARRAY['DEPOSIT'::character varying, 'CRYPTO'::character varying, 'LOAN'::character varying, 'GL'::character varying]::text[]))
);