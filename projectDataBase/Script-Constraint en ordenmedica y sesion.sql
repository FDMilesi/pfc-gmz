ALTER TABLE ONLY ordenmedica
    ADD CONSTRAINT fk_orden_medica_tratamiento FOREIGN KEY (tratamientoid) REFERENCES tratamiento(id);
	
ALTER TABLE ONLY sesion
    ADD CONSTRAINT sesion_tratamiento_fk FOREIGN KEY (tratamientoid) REFERENCES tratamiento(id) ON DELETE CASCADE;