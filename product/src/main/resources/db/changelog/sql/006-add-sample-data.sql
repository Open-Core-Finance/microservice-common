-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:6 labels:add_currencies runOnChange:true

INSERT INTO public.currency(id, symbol) VALUES('EUR', '€') ON CONFLICT DO NOTHING;
INSERT INTO public.currency(id, symbol) VALUES('JPY', '¥') ON CONFLICT DO NOTHING;
INSERT INTO public.currency(id, symbol) VALUES('GBP', '£') ON CONFLICT DO NOTHING;
INSERT INTO public.currency(id, symbol) VALUES('AUD', 'A$') ON CONFLICT DO NOTHING;
INSERT INTO public.currency(id, symbol) VALUES('CNY', '¥') ON CONFLICT DO NOTHING;
INSERT INTO public.currency(id, symbol) VALUES('BTC', '₿') ON CONFLICT DO NOTHING;
INSERT INTO public.currency (id, symbol) VALUES('ETH', 'Ξ') ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_category (id, "name", "type") VALUES('6d19baa9-603a-48a5-bf29-2353d73d7d3d', 'Generic Deposit', 'DEPOSIT') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_category (id, "name", "type") VALUES('7c0793e4-ca0a-4d91-9ab9-9442e679fbe2', 'Generic Loan', 'LOAN') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_category (id, "name", "type") VALUES('43b9a203-143f-4d9d-9f2b-3cfd1056445d', 'Generic GL', 'GL') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_category (id, "name", "type") VALUES('556dc67d-d9ae-44bd-b64a-c689d89fd807', 'Generic Crypto', 'CRYPTO') ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('36215f2f-4773-4ab8-ae7f-c031428d4086', 'Current Account', 'DEPOSIT') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('9f00ba79-4a1c-408b-842e-775272eb8c20', 'Savings Account', 'DEPOSIT') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('60fd25ba-f12b-4356-95a1-2ea48c4fa10b', 'Fixed Deposit', 'DEPOSIT') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('21b0cd4b-077c-4cce-9083-5a5c615c93dd', 'Fixed Term Loan', 'LOAN') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('5fbdeac8-b4af-4bac-90e0-2f3edab1a1b1', 'Dynamic Term Loan', 'LOAN') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('64238694-d7fe-4e28-9540-5559303a720f', 'Interest-Free Loan', 'LOAN') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('d3db8acd-e9b4-4f30-b497-4b88f05a17d0', 'Free GL', 'GL') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('2f24311e-4331-4be9-8066-4475eff5b80b', 'Deposit GL', 'GL') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('b2b79cf9-ad62-4027-8a6f-e2dc54c3132b', 'Loan GL', 'GL') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, "name", "type") VALUES('2d272a78-ecd1-444f-a470-2265d69460a1', 'Holding Crypto', 'CRYPTO') ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.product_type (id, name, "type") VALUES ('dcca66d6-090d-4384-922a-aa98e0254ddf','Crypto GL','GL') ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.crypto_product (id, activated, allow_arbitrary_fees, category, currencies, description, "name", new_account_setting, product_availabilities, product_fees,
show_inactive_fees, "type", allow_deposit_after_maturity_date, allow_overdrafts, days_to_set_to_dormant, default_term_length, deposit_limits, early_closure_period, interest_rate, max_overdraft_limits,
max_term_length, min_term_length, overdrafts_interest, overdrafts_under_credit_arrangement_managed, term_unit, withdrawal_limits, created_date, last_modified_date, created_by,
last_modified_by, enable_term_deposit, enable_interest_rate)
VALUES('8188950b-9cd7-4dff-ab02-d1dbe6fe025c', true, false, '556dc67d-d9ae-44bd-b64a-c689d89fd807', '{BTC,ETH}',
'To hold crypto currencies', 'Crypto Hold', '{"type": "RANDOM_PATTERN", "initialState": "NEW", "randomPatternTemplate": "CRYPT######", "increasementStartingFrom": 0, "fixLengthId": true, "fixAccountLength": 6}'::jsonb,
'[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb, '[]'::jsonb, false,
'2d272a78-ecd1-444f-a470-2265d69460a1', false, false, NULL, 0, '[]'::jsonb, NULL, NULL, NULL, 0, 0,
'{"interestItems": [], "percentPerDay": 0, "interestDayInYear": "FIXED_365_DAYS", "interestRateTerms": "FIXED", "calculationDateType": "FIRST_DAY_EVERY_MOTNH", "applyWithholdingTaxes": false, "calculationDateOption": null, "calculationDateFixedDay": 14, "interestRateConstraints": [], "interestRateIndexSource": "", "allowNegativeInterestRate": false, "calculationDateFixedMonth": 3, "interestCalculationMethod": "PERCENTAGE_PER_MONTH", "balanceInterestCalculation": "END_OF_dAY"}'::jsonb,
'NO', 'DAY', '[]'::jsonb, '2024-03-14 15:31:56.457', '2024-03-14 15:31:56.457',
'{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
'{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
false, false) ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.gl_product (id, activated, allow_arbitrary_fees, category, currencies, description, "name", new_account_setting, product_availabilities,
show_inactive_fees, "type", created_date, last_modified_date, created_by, last_modified_by)
VALUES('a711c6f3-cad2-4848-b320-245f3c25be76', true, false, '43b9a203-143f-4d9d-9f2b-3cfd1056445d','{EUR,JPY,GBP,AUD,CNY}',
    'GL for all deposit transactions', 'Deposit GL',
    '{"type": "INCREASEMENT", "initialState": "NEW", "randomPatternTemplate": "@@@$#####", "increasementStartingFrom": 0, "fixLengthId": true, "fixAccountLength": 9}'::jsonb,
    '[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb, false, '2f24311e-4331-4be9-8066-4475eff5b80b',
    '2024-03-14 15:47:26.914', '2024-03-14 15:47:26.914',
    '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
    '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb)
    ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.gl_product (id,activated,allow_arbitrary_fees,category,currencies,description,name,new_account_setting,product_availabilities,
                        show_inactive_fees,"type",created_date,last_modified_date,created_by,last_modified_by)
VALUES
    ('f8db0f92-5f79-41f9-8536-1722e250f19a',true,false,'43b9a203-143f-4d9d-9f2b-3cfd1056445d','{USD,JPY,EUR}','GL for fees','Fee GL',
     '{"type": "UUID", "idPrefix": "", "idSuffix": "", "fixLengthId": true, "initialState": "NEW", "fixAccountLength": 6, "randomPatternTemplate": "@@@$#####", "increasementStartingFrom": 0}'::jsonb,
     '[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb,
     false,'d3db8acd-e9b4-4f30-b497-4b88f05a17d0','2024-04-06 22:26:35.149','2024-04-06 22:26:35.149',
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb),
    ('ccc7b7c7-90b9-48de-9f88-cc644eff8861',true,false,'43b9a203-143f-4d9d-9f2b-3cfd1056445d',
     '{USD,AUD,GBP,CNY,JPY}','GL for loan','Loan GL',
     '{"type": "RANDOM_PATTERN", "idPrefix": "", "idSuffix": "", "fixLengthId": true, "initialState": "NEW", "fixAccountLength": 6, "randomPatternTemplate": "@@@$#####", "increasementStartingFrom": 0}'::jsonb,
     '[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb,
     false,'b2b79cf9-ad62-4027-8a6f-e2dc54c3132b','2024-04-06 22:31:18.765','2024-04-06 22:31:18.765',
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb)
    ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.gl_product (id,activated,allow_arbitrary_fees,category,currencies,description,name,new_account_setting,product_availabilities,
                        show_inactive_fees,"type",created_date,last_modified_date,created_by,last_modified_by)
VALUES
    ('e99c7669-7e79-4c96-ba84-343fc18bf010',true,false,'43b9a203-143f-4d9d-9f2b-3cfd1056445d','{ETH,BTC}','GL for crypto curencies','Crypto GL',
     '{"type": "INCREASEMENT", "idPrefix": "CRYPTO", "idSuffix": "", "fixLengthId": true, "initialState": "NEW", "fixAccountLength": 6, "randomPatternTemplate": "@@@$#####", "increasementStartingFrom": 10}'::jsonb,
     '[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb,
     false,'dcca66d6-090d-4384-922a-aa98e0254ddf','2024-04-06 22:55:48.828','2024-04-06 22:55:48.828',
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
     '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb)
    ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.loan_product (id, activated, allow_arbitrary_fees, category, currencies, description, "name", new_account_setting, product_availabilities, product_fees,
show_inactive_fees, "type", arrears_setting, cap_charges, close_dormant_accounts, interest_rate, loan_values, lock_arrears_accounts, penalty_setting, enable_guarantors,
enable_collateral, percent_security_per_loan, repayment_collection, repayment_scheduling, under_credit_arrangement_managed, created_date, last_modified_date, created_by, last_modified_by)
VALUES('db05cce1-24f7-4c04-8e61-e0e52817e6a4', true, false, '7c0793e4-ca0a-4d91-9ab9-9442e679fbe2',
'{EUR,JPY,GBP,AUD,CNY}',
'Fixed term loan 01', 'Fixed term loan', '{"type": "RANDOM_PATTERN", "initialState": "NEW", "randomPatternTemplate": "LOAN######", "increasementStartingFrom": 0, "fixLengthId": true, "fixAccountLength": 9}'::jsonb,
'[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb, '[]'::jsonb, false, '21b0cd4b-077c-4cce-9083-5a5c615c93dd',
'{"floors": [{"value": 0.0, "currencyId": "USD"}, {"value": 0.0, "currencyId": "VND"}, {"value": 0.0, "currencyId": "USD"}, {"value": 0.0, "currencyId": "GBP"}, {"value": 0.0, "currencyId": "JPY"}, {"value": 0.0, "currencyId": "CNY"}], "toleranceAmounts": [{"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "VND", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "GBP", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "JPY", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "CNY", "defaultVal": 0.0}], "tolerancePeriods": [{"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "VND", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "GBP", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "JPY", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "CNY", "defaultVal": 0.0}], "daysCalculatedFrom": "OLDEST_LATE_REPAYMENT", "includeNonWorkingDay": false}'::jsonb,
false, false, '{"interestType": "CAPITALIZED", "percentPerDay": 0, "interestDayInYear": "FIXED_365_DAYS", "interestRateConstraints": [{"maxVal": 9.0, "minVal": 1.0, "currencyId": "USD", "defaultVal": 1.5}, {"maxVal": 9.0, "minVal": 1.0, "currencyId": "VND", "defaultVal": 1.5}, {"maxVal": 9.0, "minVal": 1.0, "currencyId": "EUR", "defaultVal": 1.5}, {"maxVal": 9.0, "minVal": 1.0, "currencyId": "GBP", "defaultVal": 1.5}, {"maxVal": 9.0, "minVal": 1.0, "currencyId": "JPY", "defaultVal": 1.5}, {"maxVal": 9.0, "minVal": 1.0, "currencyId": "CNY", "defaultVal": 1.5}], "interestRateIndexSource": "", "interestCalculationPoint": "PERCENTAGE_PER_MONTH", "interestCalculationMethod": "FLAT", "repaymentsInterestCalculation": "ACTUAL_NUMBER_OF_DAYS", "accruedInterestPostingFrequency": "REPAYMENT"}'::jsonb,
'[{"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "VND", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "EUR", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "GBP", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "JPY", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "CNY", "defaultVal": 0.0}]'::jsonb,
false, '{"calculationMethod": "NONE", "penaltyRateConstraints": [{"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "VND", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "EUR", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "GBP", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "JPY", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "CNY", "defaultVal": 0.0}], "penaltyTolerancePeriod": 1}'::jsonb,
false, false, 0.0, '{"acceptPrePayments": true, "repaymentHorizontal": true, "repaymentTypesOrder": ["FEE", "PENALTY", "INTEREST", "PRINCIPAL"], "prePaymentRecalculation": "RECALCULATE_SCHEDULE_KEEP_SAME_NUMBER_OF_TERMS", "autoApplyInterestPrePayments": true, "acceptPrepaymentFutureInterest": false}'::jsonb,
'{"intervalValue": 1, "repaymentDays": [], "intervalOption": "MONTH", "gracePeriodType": "NO", "repaymentMethod": "INTERVAL", "currencyRounding": "NO_ROUNDING", "scheduleRounding": "NO_ROUNDING", "shortMonthHandling": "LAST_DAY_OF_MONTH", "gracePeriodConstraints": null, "installmentsConstraints": [{"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "VND", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "GBP", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "JPY", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "CNY", "defaultVal": 0.0}], "repaymentsScheduleEditing": {"adjustPaymentDates": false, "adjustFeePaymentSchedule": false, "adjustNumberInstallments": false, "configurePaymentHolidays": false, "adjustPenaltyPaymentSchedule": false, "adjustInterestPaymentSchedule": false, "adjustPrincipalPaymentSchedule": false}, "nonWorkingDaysRescheduling": "NO_RESCHEDULING", "firstDueDateOffsetConstraints": [{"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "VND", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "USD", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "GBP", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "JPY", "defaultVal": 0.0}, {"maxVal": 0.0, "minVal": 0.0, "currencyId": "CNY", "defaultVal": 0.0}], "collectPrincipalEveryRepayments": 1}'::jsonb,
'OPTIONAL', '2024-03-14 15:43:03.391', '2024-03-14 15:43:03.391', '{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
'{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb) ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.deposit_product (id, activated, allow_arbitrary_fees, category, currencies, description, "name", new_account_setting, product_availabilities, product_fees, show_inactive_fees,
"type", allow_deposit_after_maturity_date, allow_overdrafts, days_to_set_to_dormant, default_term_length, deposit_limits, early_closure_period, interest_rate, max_overdraft_limits,
max_term_length, min_term_length, overdrafts_interest, overdrafts_under_credit_arrangement_managed, term_unit, withdrawal_limits, created_date, last_modified_date, created_by,
last_modified_by, enable_term_deposit, enable_interest_rate)
VALUES('4187dc8e-d844-4ecc-a7e3-b5f5e8b9d73e', true, false, '6d19baa9-603a-48a5-bf29-2353d73d7d3d',
'{EUR,JPY,GBP,AUD,CNY}',
'CASA account with no interest rate', 'No interest CASA',
'{"type": "RANDOM_PATTERN", "initialState": "NEW", "randomPatternTemplate": "CASA######", "increasementStartingFrom": 0, "fixLengthId": true, "fixAccountLength": 6}'::jsonb,
'[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb, '[]'::jsonb,
false, '36215f2f-4773-4ab8-ae7f-c031428d4086', false, false, NULL, 0, '[]'::jsonb, NULL, NULL, NULL, 0, 0,
'{"interestItems": [], "percentPerDay": 0, "interestDayInYear": "FIXED_365_DAYS", "interestRateTerms": "FIXED", "calculationDateType": "FIRST_DAY_EVERY_MOTNH", "applyWithholdingTaxes": false, "calculationDateOption": null, "calculationDateFixedDay": 14, "interestRateConstraints": [], "interestRateIndexSource": "", "allowNegativeInterestRate": false, "calculationDateFixedMonth": 3, "interestCalculationMethod": "PERCENTAGE_PER_MONTH", "balanceInterestCalculation": "END_OF_dAY"}'::jsonb,
'NO', 'DAY', '[]'::jsonb, '2024-03-14 15:31:56.457', '2024-03-14 15:31:56.457',
'{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
'{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
false, false) ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.deposit_product (id, activated, allow_arbitrary_fees, category, currencies, description, "name", new_account_setting, product_availabilities, product_fees, show_inactive_fees,
"type", allow_deposit_after_maturity_date, allow_overdrafts, days_to_set_to_dormant, default_term_length, deposit_limits, early_closure_period, interest_rate, max_overdraft_limits,
max_term_length, min_term_length, overdrafts_interest, overdrafts_under_credit_arrangement_managed, term_unit, withdrawal_limits, created_date, last_modified_date, created_by,
last_modified_by, enable_term_deposit, enable_interest_rate)
VALUES('121a89a3-5583-49c7-b9aa-1e25857e09c7', true, false, '6d19baa9-603a-48a5-bf29-2353d73d7d3d',
'{EUR,JPY,GBP,AUD,CNY}',
'Fix interest term deposit', 'Fixed deposit', '{"type": "RANDOM_PATTERN", "initialState": "NEW", "randomPatternTemplate": "DEP######", "increasementStartingFrom": 0, "fixLengthId": true, "fixAccountLength": 6}'::jsonb,
'[{"modeInfo": [], "availabilityMode": "ALL_GROUPS"}, {"modeInfo": [], "availabilityMode": "ALL_BRANCHES"}]'::jsonb, '[]'::jsonb,
false, '60fd25ba-f12b-4356-95a1-2ea48c4fa10b', false, false, NULL, 2, '[]'::jsonb, NULL,
'{"interestItems": [{"toValue": 0.0, "fromValue": 0.0, "currencyId": "VND", "interestPercentage": 0.0}], "percentPerDay": 0, "interestDayInYear": "FIXED_365_DAYS", "interestRateTerms": "FIXED", "calculationDateType": "FIRST_DAY_EVERY_MOTNH", "applyWithholdingTaxes": false, "calculationDateOption": null, "calculationDateFixedDay": 14, "interestRateConstraints": [{"maxVal": 2.0, "minVal": 0.01, "currencyId": "VND", "defaultVal": 0.5}, {"maxVal": 2.0, "minVal": 0.01, "currencyId": "USD", "defaultVal": 0.5}, {"maxVal": 2.0, "minVal": 0.01, "currencyId": "USD", "defaultVal": 0.5}, {"maxVal": 2.0, "minVal": 0.01, "currencyId": "JPY", "defaultVal": 0.5}, {"maxVal": 2.0, "minVal": 0.01, "currencyId": "CNY", "defaultVal": 0.5}], "interestRateIndexSource": "", "allowNegativeInterestRate": false, "calculationDateFixedMonth": 3, "interestCalculationMethod": "PERCENTAGE_PER_MONTH", "balanceInterestCalculation": "END_OF_dAY"}'::jsonb,
NULL, 0, 0, '{"interestItems": [], "percentPerDay": 0, "interestDayInYear": "FIXED_365_DAYS", "interestRateTerms": "FIXED", "calculationDateType": "FIRST_DAY_EVERY_MOTNH", "applyWithholdingTaxes": false, "calculationDateOption": null, "calculationDateFixedDay": 14, "interestRateConstraints": [], "interestRateIndexSource": "", "allowNegativeInterestRate": false, "calculationDateFixedMonth": 3, "interestCalculationMethod": "PERCENTAGE_PER_MONTH", "balanceInterestCalculation": "END_OF_dAY"}'::jsonb,
'NO', 'DAY', '[]'::jsonb, '2024-03-14 15:35:57.025', '2024-03-14 15:35:57.025', '{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
'{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb,
false, true) ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.holiday (id, description, holiday_date, to_date, repeat_yearly, date_range)
VALUES('04f04797-ee95-4919-bfaf-6d837f1d62e2', 'New year', '2024-12-31', '2024-01-01', true, true) ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.holiday (id, description, holiday_date, to_date, repeat_yearly, date_range)
VALUES('913a195a-c93b-4850-960f-52f97842a060', 'Christmas', '2024-12-25', '2024-12-25', true, false) ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.holiday (id, description, holiday_date, to_date, repeat_yearly, date_range)
VALUES('67abb435-14a6-410c-aff4-05ec0cfeb29d', 'Cá tháng 4 - 2024', '2024-04-01', '2024-04-01', false, false) ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.holiday (id, description, holiday_date, to_date, repeat_yearly, date_range)
VALUES('7845553f-3a0f-4d1e-ae8d-1f969c4639b4', 'Giải phóng 2024', '2024-04-30', '2024-05-01', false, true) ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.branch (id, city, country, email, "name", parent_branch_id, phone_number, state, street_address_line_1, zip_postal_code, non_working_days, inherit_non_working_days, created_date, last_modified_date, created_by, last_modified_by)
VALUES('18a78172-ecb9-4f9f-a732-c5ee2431add1', 'HCM City', 'Vietnam', 'xxxx@email.com', 'Branch 01', '', '0xxxxxx', 'HCM City', 'N/A', '73000', NULL, true, '2024-03-14 23:09:39.187', '2024-03-14 23:09:39.187',
       '{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb, '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb) ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.branch (id, city, country, email, "name", parent_branch_id, phone_number, state, street_address_line_1, zip_postal_code, non_working_days, inherit_non_working_days, created_date, last_modified_date, created_by, last_modified_by)
VALUES('03141876-d54a-4eae-a235-1a01781df3ae', 'Ha Noi City', 'Vietnam', 'xxx@email.com', 'Branch 02', '', '0xxxxxxx', 'Ha Noi City', 'N/A', '00000', '["MONDAY", "SUNDAY"]'::jsonb, false, '2024-03-14 23:10:56.106', '2024-03-14 23:10:56.106',
       '{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb, '{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb) ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.exchange_rate (from_currency, to_currency, buy_rate, "name", sell_rate, margin, created_date, last_modified_date,
created_by, last_modified_by) VALUES('VND', 'USD', 24835.0, NULL, 25335.0, 100.0, '2024-03-15 13:15:06.904', '2024-03-15 13:15:06.904',
'{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
'{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb)
ON CONFLICT DO NOTHING;

INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.exchange_rate (from_currency, to_currency, buy_rate, "name", sell_rate, margin, created_date, last_modified_date,
created_by, last_modified_by) VALUES('CNY', 'VND', 3337.33, NULL, 3537.33, 100.0, '2024-03-15 13:45:21.963', '2024-03-15 13:45:21.963',
'{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
'{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb)
ON CONFLICT DO NOTHING;
INSERT INTO tenant_0f522100_7d8c_4b67_9a7f_779e1b179eff.exchange_rate (from_currency, to_currency, buy_rate, "name", sell_rate, margin, created_date, last_modified_date,
created_by, last_modified_by) VALUES('JPY', 'VND', 156.95, NULL, 176.95, 10.0, '2024-03-15 14:08:06.308', '2024-03-15 14:08:06.308',
'{"email": "", "userId": null, "lastName": null, "username": "", "firstName": "", "middleName": null, "displayName": ""}'::jsonb,
'{"email": "admin@admin.com", "userId": "01", "lastName": null, "username": "admin", "firstName": null, "middleName": null, "displayName": "System Admin"}'::jsonb)
ON CONFLICT DO NOTHING;
