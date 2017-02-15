
CREATE TABLE datodecontacto
(
  id serial,
  idgooglecontacts character varying(30),
  nombregooglecontacts character varying(30),
  pacienteid integer,
  desearecibirwhatsapp boolean,
  sincronizado boolean NOT NULL DEFAULT false,
  CONSTRAINT "datosdecontacto_PK" PRIMARY KEY (id),
  CONSTRAINT fk_datosdecontacto_paciente FOREIGN KEY (pacienteid)
      REFERENCES paciente (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL
)