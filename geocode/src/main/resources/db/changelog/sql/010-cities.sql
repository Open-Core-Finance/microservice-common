-- Liquibase formatted SQL
-- ChangeSet Trung.Doan:10 labels:cities runOnChange:true

CREATE INDEX idx_16386_cities_ibfk_1 ON public.city USING btree (state_id);

CREATE INDEX idx_16386_cities_ibfk_2 ON public.city USING btree (country_id);

ALTER TABLE ONLY public.city
    ADD CONSTRAINT cities_ibfk_1 FOREIGN KEY (state_id) REFERENCES public.state(id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE ONLY public.city
    ADD CONSTRAINT cities_ibfk_2 FOREIGN KEY (country_id) REFERENCES public.country(id) ON UPDATE RESTRICT ON DELETE RESTRICT;
