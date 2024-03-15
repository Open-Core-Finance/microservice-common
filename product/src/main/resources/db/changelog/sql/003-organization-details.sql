-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:organization runOnChange:true

INSERT INTO public.currency (id, symbol, decimal_mark, symbol_at_beginning) VALUES ('VND', 'đ', ',', false) ON CONFLICT DO NOTHING;
INSERT INTO public.currency (id, symbol) VALUES ('USD', '$') ON CONFLICT DO NOTHING;

INSERT INTO public.organization(id,
	city, country, currency_id, decimal_mark, email, icon_url, local_date_format, local_date_time_format, logo_url, name, non_working_days, phone_number, state, street_address_line_1, timezone, zip_postal_code)
	VALUES ('57488b17-0af8-4eaf-841e-f3cce2c6c062', 'Hồ Chí Minh', 'Việt Nam', 'VND', ',', 'admin@bank1.com', null, 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm:ss', null, 'Bank 01', null, '0123456789', 'Hồ Chí Minh', '100 - Street 01', 'GMT+7', '73000')
	ON CONFLICT (name) DO UPDATE set name = EXCLUDED.name, phone_number = EXCLUDED.phone_number, email = EXCLUDED.email;

INSERT INTO public.organization(id,
	city, country, currency_id, decimal_mark, email, icon_url, local_date_format, local_date_time_format, logo_url, name, non_working_days, phone_number, state, street_address_line_1, timezone, zip_postal_code)
	VALUES ('0f522100-7d8c-4b67-9a7f-779e1b179eff', 'Hồ Chí Minh', 'Việt Nam', 'USD', '.', 'admin@bank2.com', null, 'yyyy-MM-dd', 'yyyy-MM-dd hh:mm:ss', null, 'Bank 02', null, '0123456789', 'Hồ Chí Minh', '100 - Street 02', 'GMT+7', '73001')
	ON CONFLICT (name) DO UPDATE set name = EXCLUDED.name, phone_number = EXCLUDED.phone_number, email = EXCLUDED.email;
