-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:3 labels:regions runOnChange:true

CREATE SEQUENCE IF NOT EXISTS public.region_id_seq
    AS integer START WITH 6 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

CREATE TABLE IF NOT EXISTS public.region (
    id integer NOT NULL DEFAULT nextval('public.region_id_seq'::regclass) PRIMARY KEY,
    name character varying(100) NOT NULL,
    translations jsonb,
    created_date timestamp with time zone,
    last_modified_date timestamp with time zone,
    enabled boolean DEFAULT true NOT NULL,
    wiki_data_id character varying(255)
);

COMMENT ON COLUMN public.region.wiki_data_id IS 'Rapid API GeoDB Cities';

INSERT INTO public.region(id, name, translations, created_date, last_modified_date, enabled, wiki_data_id) VALUES
	(1, 'Africa', '{"kr":"아프리카","pt-BR":"África","pt":"África","nl":"Afrika","hr":"Afrika","fa":"آفریقا","de":"Afrika","es":"África","fr":"Afrique","ja":"アフリカ","it":"Africa","cn":"非洲","tr":"Afrika"}', '2023-08-14 07:11:03+00', '2023-08-14 07:11:03+00', true, 'Q15'),
	(2, 'Americas', '{"kr":"아메리카","pt-BR":"América","pt":"América","nl":"Amerika","hr":"Amerika","fa":"قاره آمریکا","de":"Amerika","es":"América","fr":"Amérique","ja":"アメリカ州","it":"America","cn":"美洲","tr":"Amerika"}', '2023-08-14 07:11:03+00', '2023-08-14 07:11:03+00', true, 'Q828'),
	(3, 'Asia', '{"kr":"아시아","pt-BR":"Ásia","pt":"Ásia","nl":"Azië","hr":"Ázsia","fa":"آسیا","de":"Asien","es":"Asia","fr":"Asie","ja":"アジア","it":"Asia","cn":"亚洲","tr":"Asya"}', '2023-08-14 07:11:03+00', '2023-08-14 07:11:03+00', true, 'Q48'),
	(4, 'Europe', '{"kr":"유럽","pt-BR":"Europa","pt":"Europa","nl":"Europa","hr":"Európa","fa":"اروپا","de":"Europa","es":"Europa","fr":"Europe","ja":"ヨーロッパ","it":"Europa","cn":"欧洲","tr":"Avrupa"}', '2023-08-14 07:11:03+00', '2023-08-14 07:11:03+00', true, 'Q46'),
	(5, 'Oceania', '{"kr":"오세아니아","pt-BR":"Oceania","pt":"Oceania","nl":"Oceanië en Australië","hr":"Óceánia és Ausztrália","fa":"اقیانوسیه","de":"Ozeanien und Australien","es":"Oceanía","fr":"Océanie","ja":"オセアニア","it":"Oceania","cn":"大洋洲","tr":"Okyanusya"}', '2023-08-14 07:11:03+00', '2023-08-14 07:11:03+00', true, 'Q55643'),
	(6, 'Polar', '{"kr":"남극","pt-BR":"Antártida","pt":"Antártida","nl":"Antarctica","hr":"Antarktika","fa":"جنوبگان","de":"Antarktika","es":"Antártida","fr":"Antarctique","ja":"南極大陸","it":"Antartide","cn":"南極洲","tr":"Antarktika"}', '2023-08-14 07:11:03+00', '2023-08-14 07:11:03+00', true, 'Q51')
    ON CONFLICT DO NOTHING;
