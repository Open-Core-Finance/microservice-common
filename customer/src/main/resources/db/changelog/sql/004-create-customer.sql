INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.individual_customer (id, created_date, last_modified_date, created_by, last_modified_by, contact_phone, contact_email, contact_company_phone,
                                 mailing_street_address_line_1, mailing_street_address_line_2, mailing_district, mailing_city, mailing_state, mailing_city_id,
                                 mailing_state_id, mailing_zip_postal_code, malling_country, malling_country_id, malling_same_with_address, street_address_line_1,
                                 street_address_line_2, district, city, state, city_id, state_id, zip_postal_code, country, country_id, consent_marketing, consent_non_marketing,
                                 consent_abroad, consent_transfer_to_3rd, contact_home_phone, title, first_name, middle_name, last_name, gender, cis_number, place_of_birth, dob,
                                 nationality, single_nationality, second_nationality, marital_status)
VALUES
    (1, '2024-04-01 22:18:12.651', '2024-04-01 22:18:12.651', '{"email": "", "userId": "", "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '0500 755427', 'cum@aol.org', '', '580-3778 Phasellus Ave', 'XXX', 'XXX', 'Da Nang', '', 130195, 3806, '775883', 'Vietnam', 240, true, '580-3778 Phasellus Ave', 'XXX', 'XXX',
     'Da Nang', 'Đà Nẵng', 130195, 3806, '775883', 'Vietnam', 240, true, true, true, true, '0500 755427', 'Mr', 'Keith', '', 'Irwin', 'UNKNOWN', '', '', '2024-04-01',
     '{"countryCode": "VN", "issuingDate": [2024, 4, 1], "expiringDate": [2044, 3, 31], "identityType": "ID_CARD_12D", "issuingPlace": "Viet Nam", "identityNumber": "0500 755427", "issuingCountry": "Viet Nam"}'::jsonb,
     true, NULL, 'SINGLE') ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.individual_customer (id, created_date, last_modified_date, created_by, last_modified_by, contact_phone, contact_email, contact_company_phone,
                                 mailing_street_address_line_1, mailing_street_address_line_2, mailing_district, mailing_city, mailing_state, mailing_city_id,
                                 mailing_state_id, mailing_zip_postal_code, malling_country, malling_country_id, malling_same_with_address, street_address_line_1,
                                 street_address_line_2, district, city, state, city_id, state_id, zip_postal_code, country, country_id, consent_marketing, consent_non_marketing,
                                 consent_abroad, consent_transfer_to_3rd, contact_home_phone, title, first_name, middle_name, last_name, gender, cis_number, place_of_birth, dob,
                                 nationality, single_nationality, second_nationality, marital_status)
VALUES
    (2, '2024-04-01 22:27:53.395', '2024-04-01 22:27:53.395', '{"email": "", "userId": "", "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '0800 1111', 'fringilla@outlook.ca', '', 'XXXXX', 'XXXXX', 'XXXXX', 'Huyện Khoái Châu', 'Huyện Khoái Châu', 130323, 3768, '202636', 'Vietnam', 240, true, 'XXXXX', 'XXXXX', 'XXXXX',
     'Huyện Khoái Châu', 'Hưng Yên', 130323, 3768, '202636', 'Vietnam', 240, true, true, false, false, '0331 001 4860', 'Ms', 'Hasad', 'Bennett', 'Sloan', 'FEMALE', 'XXXXX', 'Viet Nam', '2015-12-31', '{"countryCode": "VN", "issuingDate": [2016, 4, 5], "expiringDate": [2034, 10, 3], "identityType": "ID_CARD_12D", "issuingPlace": "Viet Nam", "identityNumber": "2026363435", "issuingCountry": "Viet Nam"}'::jsonb,
     true, NULL, 'SINGLE') ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.corporate_customer (id, created_date, last_modified_date, created_by, last_modified_by, contact_name, contact_phone, contact_email, contact_company_phone,
                                mailing_street_address_line_1, mailing_street_address_line_2, mailing_district, mailing_city, mailing_state, mailing_city_id, mailing_state_id,
                                mailing_zip_postal_code, malling_country, malling_country_id, malling_same_with_address, street_address_line_1, street_address_line_2, district,
                                city, state, city_id, state_id, zip_postal_code, country, country_id, consent_marketing, consent_non_marketing, consent_abroad, consent_transfer_to_3rd,
                                "name", tax_number, start_date)
VALUES
    (3, '2024-04-01 22:30:24.347', '2024-04-01 22:30:24.347', '{"email": "", "userId": "", "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     NULL, '02923781939', 'aa@acb.com', '02923781939', 'D5-5, đường số 2', 'khu đô thị mới Long Thịnh, Phường Phú Thứ', 'Cái Răng', 'Cần Thơ', 'Cần Thơ', 148322, 4925, '903878', 'Vietnam',
     240, true, 'D5-5, đường số 2', 'khu đô thị mới Long Thịnh, Phường Phú Thứ', 'Cái Răng', 'Cần Thơ', 'Cần Thơ', 148322, 4925, '903878', 'Vietnam', 240, true, true, true, true,
     'ACB COMPANY LIMITED', '1800685481', '2024-03-20') ON CONFLICT DO NOTHING;

ALTER SEQUENCE tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.customer_id_seq RESTART 4;
