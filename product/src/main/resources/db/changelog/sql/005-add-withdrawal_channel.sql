-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:5 labels:withdrawal_channel runOnChange:true

CREATE TABLE IF NOT EXISTS withdrawal_channel
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);

CREATE TABLE IF NOT EXISTS tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.withdrawal_channel
(
    id character varying(255) DEFAULT gen_random_uuid()::character varying(255) PRIMARY KEY,
    name character varying(255),
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    created_by jsonb,
    last_modified_by jsonb
);

INSERT INTO withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('01', 'ATM', now(), now()) ON CONFLICT DO NOTHING;
INSERT INTO withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('02', 'POS', now(), now()) ON CONFLICT DO NOTHING;
INSERT INTO withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('03', 'ONLINE', now(), now()) ON CONFLICT DO NOTHING;
INSERT INTO withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('04', 'BRANCH', now(), now()) ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('01', 'ATM', now(), now()) ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('02', 'POS', now(), now()) ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('03', 'ONLINE', now(), now()) ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.withdrawal_channel (id, name, created_date, last_modified_date)
VALUES ('04', 'BRANCH', now(), now()) ON CONFLICT DO NOTHING;
