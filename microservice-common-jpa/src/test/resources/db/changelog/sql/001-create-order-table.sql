-- db/changelog/sql/001-create-order-table.sql

-- Liquibase formatted SQL

-- ChangeSet
-- ChangeSet author: Trung Doan
-- ChangeSet id: 001
CREATE TABLE IF NOT EXISTS public.coffee_order
(
    id bigint NOT NULL,
    items jsonb,
    order_status character varying(255),
    sub_total double precision,
    total double precision NOT NULL,
    vat double precision NOT NULL,
    store_id bigint NOT NULL,
    queue_id bigint NULL,
    CONSTRAINT coffee_order_pkey PRIMARY KEY (id),
    CONSTRAINT coffee_order_order_status_check CHECK (order_status::text = ANY (ARRAY['UNPAID'::character varying, 'ORDERED'::character varying, 'PROCESSING'::character varying, 'COMPLETED'::character varying, 'CANCELED'::character varying, 'REFUND'::character varying]::text[]))
)
-- /ChangeSet