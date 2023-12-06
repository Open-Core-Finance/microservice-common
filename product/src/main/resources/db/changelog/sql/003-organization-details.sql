-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:organization runOnChange:true

INSERT INTO public.currency (id, name, symbol, decimal_mark, symbol_at_beginning)
    VALUES ('a2f067fa-07e9-452d-89dd-5a3bbd5793fc', 'VND', 'đ', ',', false) ON CONFLICT DO NOTHING;
INSERT INTO public.currency (id, name, symbol, decimal_mark, symbol_at_beginning)
    VALUES ('014f25e9-eced-4210-a1eb-3b108aaad33c', 'USD', '$', '.', true) ON CONFLICT DO NOTHING;

INSERT INTO organization(
	city, country, currency_id, decimal_mark, email, icon_url, local_date_format, local_date_time_format, logo_url, name, non_working_days, phone_number, state, street_address_line_1, timezone, zip_postal_code)
	VALUES ('Hồ Chí Minh', 'Việt Nam', 'a2f067fa-07e9-452d-89dd-5a3bbd5793fc', ',', 'admin@bank1.com', null, 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm:ss', null, 'Bank 01', null, '0123456789', 'Hồ Chí Minh', '100 - Street 01', 'GMT+7', '73000')
	ON CONFLICT (name) DO UPDATE set name = EXCLUDED.name, phone_number = EXCLUDED.phone_number, email = EXCLUDED.email;

INSERT INTO organization(
	city, country, currency_id, decimal_mark, email, icon_url, local_date_format, local_date_time_format, logo_url, name, non_working_days, phone_number, state, street_address_line_1, timezone, zip_postal_code)
	VALUES ('Hồ Chí Minh', 'Việt Nam', '014f25e9-eced-4210-a1eb-3b108aaad33c', '.', 'admin@bank2.com', null, 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm:ss', null, 'Bank 02', null, '0123456789', 'Hồ Chí Minh', '100 - Street 02', 'GMT+7', '73001')
	ON CONFLICT (name) DO UPDATE set name = EXCLUDED.name, phone_number = EXCLUDED.phone_number, email = EXCLUDED.email;