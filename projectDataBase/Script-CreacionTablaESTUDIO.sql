CREATE TABLE estudio(
id serial,
tratamientoid integer NOT NULL,
nombrearchivo varchar(125) NOT NULL,
CONSTRAINT pk_estudio PRIMARY KEY (id),
CONSTRAINT fk_estudio_tratamiento FOREIGN KEY (tratamientoid) REFERENCES tratamiento (id),
CONSTRAINT uk_nombrearchivo UNIQUE (tratamientoid,nombrearchivo)
);