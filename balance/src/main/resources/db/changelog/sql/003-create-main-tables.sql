-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:main-table runOnChange:true

CREATE TABLE IF NOT EXISTS account_balance
(
    account_type character varying(255),
    account_id character varying(255),
    currency character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    begin_balance double precision NOT NULL,
    end_balance double precision NOT NULL,
    current_balance double precision NOT NULL,
    CONSTRAINT account_balance_pk PRIMARY KEY(account_type, account_id, currency)
);

CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.account_balance
(
    account_type character varying(255),
    account_id character varying(255),
    currency character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb,
    begin_balance double precision NOT NULL,
    end_balance double precision NOT NULL,
    current_balance double precision NOT NULL,
    CONSTRAINT account_balance_pk PRIMARY KEY(account_type, account_id, currency)
);
