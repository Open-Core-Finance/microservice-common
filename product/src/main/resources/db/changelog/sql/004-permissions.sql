-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:4 labels:permissions runOnChange:true

-- System Admin
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'branch', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'cryptoproduct', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'currency', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'depositproduct', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'exchangerate', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'glproduct', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'holiday', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'loanproduct', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'organization', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'productcategory', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'rate', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'ratesource', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'permission', 'SystemAdmin', NULL) ON CONFLICT DO NOTHING;

-- Bank 1 Admin
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'branch', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'cryptoproduct', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'currency', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'depositproduct', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'exchangerate', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'glproduct', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'holiday', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'loanproduct', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'productcategory', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'rate', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'ratesource', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'permission', 'Bank1-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'GET', 'organization', 'Bank1-Admin', '/organizations/*') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'PUT', 'organization', 'Bank1-Admin', '/organizations/*') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'POST', 'organization', 'Bank1-Admin', null) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'DELETE', 'organization', 'Bank1-Admin', null) ON CONFLICT DO NOTHING;

-- Bank 2 Admin
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'branch', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'cryptoproduct', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'currency', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'depositproduct', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'exchangerate', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'glproduct', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'holiday', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'loanproduct', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'productcategory', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'rate', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'ratesource', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO public.permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', NULL, 'permission', 'Bank2-Admin', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'GET', 'organization', 'Bank2-Admin', '/organizations/*') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'PUT', 'organization', 'Bank2-Admin', '/organizations/*') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'POST', 'organization', 'Bank2-Admin', null) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'DELETE', 'organization', 'Bank2-Admin', null) ON CONFLICT DO NOTHING;

-- Bank 1 User
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'POST', 'branch', 'Bank1-User', 'branches/') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'POST', 'branch', 'Bank1-User', 'branches/*') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'GET', 'branch', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'PUT', 'branch', 'Bank1-User', null) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'DELETE', 'branch', 'Bank1-User', null) ON CONFLICT DO NOTHING;

INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'cryptoproduct', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'currency', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'depositproduct', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'exchangerate', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'glproduct', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'holiday', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'loanproduct', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'productcategory', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'rate', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'ratesource', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;

INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'GET', 'permission', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'POST', 'permission', 'Bank1-User', '/permission/*') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'ALLOWED', 'POST', 'permission', 'Bank1-User', '/permission/') ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'PUT', 'permission', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;
INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', 'DELETE', 'permission', 'Bank1-User', NULL) ON CONFLICT DO NOTHING;

INSERT INTO permission (action, control, request_method, resource_type, role_id, url)
  VALUES ('ANY', 'DENIED', NULL, 'organization', 'Bank1-User', null) ON CONFLICT DO NOTHING;