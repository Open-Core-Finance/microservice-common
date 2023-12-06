-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:system_admin runOnChange:true

-- Role
INSERT INTO role (id, name, tenant_id) VALUES ('SystemAdmin', 'System Admin', NULL) ON CONFLICT DO NOTHING;

-- UserProfile
INSERT INTO user_profile (id, activated, address, birthday, display_name, email, first_name, gender, last_name, password,
  phone_number, username, role_id) VALUES
  ('01', true, NULL, NULL, 'System Admin', 'admin@admin.com', 'Admin', 'FEMALE', NULL, '$2a$10$VXilztBj3jmpzMHGd4lIj.Q5R.5aqo7t6H3MWSMO8HUTle3VpIkNa', NULL,
  'admin', NULL) ON CONFLICT DO NOTHING;

-- User Role
INSERT INTO user_profile_role(user_profile_id, role_id) VALUES ('01', 'SystemAdmin') ON CONFLICT DO NOTHING;

-- Authen
INSERT INTO permission (id, action, control, request_method, resource_type, role_id, url)
  VALUES ('26ff0fb6-233b-4b03-8cc6-39528de92325', 'ANY', 'ALLOWED', NULL, 'attemptedlogin', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (id, action, control, request_method, resource_type, role_id, url)
  VALUES ('750a527a-dcd2-4fc5-9ef7-c6cb663b99a7', 'ANY', 'ALLOWED', NULL, 'authen', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (id, action, control, request_method, resource_type, role_id, url)
  VALUES ('1039b387-80e3-4c44-8c19-377ccd0a06bd', 'ANY', 'ALLOWED', NULL, 'loginsession', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (id, action, control, request_method, resource_type, role_id, url)
  VALUES ('4804ab03-5d0e-4915-a1c1-e5c45f56efe4', 'ANY', 'ALLOWED', NULL, 'role', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (id, action, control, request_method, resource_type, role_id, url)
  VALUES ('51d86e59-bba3-4acd-b898-06fd175c0361', 'ANY', 'ALLOWED', NULL, 'userprofile', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;

-- rollback delete from permission where role_id='SystemAdmin';
-- rollback delete from anonymous_url_access where id in ('/authentication/login', '/authentication/refresh-token');
-- rollback delete from user_profile_role where user_profile_id='01' and role_id='SystemAdmin';
-- rollback delete from user_profile where id='01';
-- rollback delete from role where id='SystemAdmin';