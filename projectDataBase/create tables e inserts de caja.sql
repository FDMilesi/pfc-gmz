CREATE TABLE movimientocaja
(
 id serial not null,
 conceptoid smallint NOT NULL,
 cajaid smallint NOT NULL,
 descripcion character varying(30),
 cantidad smallint NOT NULL,
 tipomovimiento character varying(7) NOT NULL,
 fechayhora timestamp without time zone NOT NULL,
 valorunitario numeric(12,6) NOT NULL,
 saldo numeric(12,6) NOT NULL,
 CONSTRAINT pk_movimiento PRIMARY KEY (id),
 CONSTRAINT fk_movimiento_caja FOREIGN KEY (cajaid)
     REFERENCES public.caja (id) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT fk_movimiento_concepto FOREIGN KEY (conceptoid)
     REFERENCES public.concepto (id) MATCH SIMPLE
     ON UPDATE NO ACTION ON DELETE NO ACTION
)

CREATE TABLE caja
(
 id smallserial not null,
 CONSTRAINT pk_caja PRIMARY KEY (id)
)

INSERT INTO concepto VALUES (1,'Estampilla');
INSERT INTO concepto VALUES (2,'Tapping');
INSERT INTO concepto VALUES (3,'Coseguro');
INSERT INTO concepto VALUES (4,'Sesión particular');
INSERT INTO concepto VALUES (5,'Otro');
