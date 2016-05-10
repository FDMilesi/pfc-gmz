CREATE TABLE Especialidad ( 
	id smallserial NOT NULL,
	nombre varchar(30)
)
;

CREATE TABLE Agenda ( 
	id smallserial NOT NULL,
	especialidadId smallint,
	profesional varchar(50)
)
;

CREATE TABLE Concepto ( 
	id smallserial NOT NULL,
	nombre varchar(30) NOT NULL
)
;

CREATE TABLE Movimiento ( 
	id serial NOT NULL,
	conceptoId smallint NOT NULL,
	cajaId smallint NOT NULL,
	descripcion varchar(30),
	cantidad smallint NOT NULL,
	total real NOT NULL,
	tipoMovimiento varchar(7) NOT NULL,
	fechayHora timestamp without time zone NOT NULL
)
;

CREATE TABLE Caja ( 
	id smallserial NOT NULL,
	saldoActual real NOT NULL
)
;

CREATE TABLE Sesion ( 
	id serial NOT NULL,
	tratamientoId integer,
	agendaId smallserial,
	numeroDeSesion smallint,
	fechaHoraInicio timestamp without time zone,
	duracion smallint,
	transcurrida boolean,
	cuenta boolean
)
;

CREATE TABLE TipoTratamientoObraSocial ( 
	tipoTratamientoId smallint NOT NULL,
	obraSocialId smallint NOT NULL,
	codigoDePrestacion varchar(20),
	topeSesionesAño smallint
)
;

CREATE TABLE ObraSocial ( 
	id smallserial NOT NULL,
	nombre varchar(20) NOT NULL,
	linkAutorizacion varchar(256)
)
;

CREATE TABLE TipoDeTratamiento ( 
	id smallserial NOT NULL,
	especialidadId smallint,
	nombre varchar(50) NOT NULL,
	duracion smallint NOT NULL,
	cubiertoPorObraSocial boolean NOT NULL
)
;

CREATE TABLE OrdenMedica ( 
	id serial NOT NULL,
	tratamientoId integer NOT NULL,
	obraSocialId smallint,
	cantidadDeSesiones smallint NOT NULL,
	codigoDeAutorizacion varchar(50),
	fechaCreacion date NOT NULL,
	presentadaAlCirculo boolean,
	numeroAfiliadoPaciente varchar(10)
)
;

CREATE TABLE Paciente ( 
	id serial NOT NULL,
	obraSocialId smallint,
	apellido varchar(50) NOT NULL,
	nombre varchar(50) NOT NULL,
	dni varchar(10) NOT NULL,
	domicilio varchar(50),
	telefono varchar(20),
	celular varchar(20),
	fechaDeNacimiento date,
	nroAfiliadoOS varchar(10),
	fechaAlta date NOT NULL
)
;

CREATE TABLE Tratamiento ( 
	id serial NOT NULL,
	tipoDeTratamientoId smallint NOT NULL,
	pacienteId integer NOT NULL,
	tratamientoAsociadoId integer,
	cantidadDeSesiones smallint NOT NULL,
	diagnostico varchar(100),
	particular boolean NOT NULL,
	finalizado boolean,
	observaciones text,
	fechaCreacion date,
	sesionesAFavor smallint,
	sesionesAFavorUsadas boolean,
	fechaUltimaAutorizacion date,
	medicoderivante character varying(100)
)
;


ALTER TABLE Especialidad ADD CONSTRAINT PK_Especialidad 
	PRIMARY KEY (id)
;


ALTER TABLE Agenda ADD CONSTRAINT PK_Agenda 
	PRIMARY KEY (id)
;


ALTER TABLE Concepto ADD CONSTRAINT PK_Concepto 
	PRIMARY KEY (id)
;


ALTER TABLE Movimiento ADD CONSTRAINT PK_Movimiento 
	PRIMARY KEY (id)
;


ALTER TABLE Caja ADD CONSTRAINT PK_Caja 
	PRIMARY KEY (id)
;


ALTER TABLE Sesion ADD CONSTRAINT PK_Sesion 
	PRIMARY KEY (id)
;


ALTER TABLE TipoTratamientoObraSocial ADD CONSTRAINT PK_TipoTratamientoObraSocial 
	PRIMARY KEY (tipoTratamientoId, obraSocialId)
;


ALTER TABLE ObraSocial ADD CONSTRAINT PK_Obra_Social 
	PRIMARY KEY (id)
;


ALTER TABLE TipoDeTratamiento ADD CONSTRAINT PK_Tipo_de_Tratamiento 
	PRIMARY KEY (id)
;


ALTER TABLE OrdenMedica ADD CONSTRAINT PK_Orden 
	PRIMARY KEY (id)
;


ALTER TABLE Paciente ADD CONSTRAINT PK_Paciente 
	PRIMARY KEY (id)
;


ALTER TABLE Tratamiento ADD CONSTRAINT PK_Tratamiento 
	PRIMARY KEY (id)
;




ALTER TABLE Agenda ADD CONSTRAINT FK_Agenda_Especialidad 
	FOREIGN KEY (especialidadId) REFERENCES Especialidad (id)
;

ALTER TABLE Movimiento ADD CONSTRAINT FK_Movimiento_Caja 
	FOREIGN KEY (cajaId) REFERENCES Caja (id)
;

ALTER TABLE Movimiento ADD CONSTRAINT FK_Movimiento_Concepto 
	FOREIGN KEY (conceptoId) REFERENCES Concepto (id)
;

ALTER TABLE Sesion ADD CONSTRAINT FK_Sesion_Agenda 
	FOREIGN KEY (agendaId) REFERENCES Agenda (id)
;

ALTER TABLE Sesion ADD CONSTRAINT FK_Sesion_Tratamiento 
	FOREIGN KEY (tratamientoId) REFERENCES Tratamiento (id)
;

ALTER TABLE TipoTratamientoObraSocial ADD CONSTRAINT FK_TipoTratamientoObraSocial_TipoDeTratamiento 
	FOREIGN KEY (tipoTratamientoId) REFERENCES TipoDeTratamiento (id)
;

ALTER TABLE TipoTratamientoObraSocial ADD CONSTRAINT FK_TipoTratamientoObraSocial_ObraSocial 
	FOREIGN KEY (obraSocialId) REFERENCES ObraSocial (id)
;

ALTER TABLE TipoDeTratamiento ADD CONSTRAINT FK_TipoDeTratamiento_Especialidad 
	FOREIGN KEY (especialidadId) REFERENCES Especialidad (id)
;

ALTER TABLE OrdenMedica ADD CONSTRAINT FK_Orden_Medica_Tratamiento 
	FOREIGN KEY (tratamientoId) REFERENCES Tratamiento (id)
;

ALTER TABLE OrdenMedica ADD CONSTRAINT FK_Orden_Medica_Obra_Social 
	FOREIGN KEY (obraSocialId) REFERENCES ObraSocial (id)
;

ALTER TABLE Paciente ADD CONSTRAINT FK_Pacientes_Obras_Sociales 
	FOREIGN KEY (obraSocialId) REFERENCES ObraSocial (id)
;

ALTER TABLE Tratamiento ADD CONSTRAINT FK_Tratamientos_Tipos_de_Tratamiento 
	FOREIGN KEY (tipoDeTratamientoId) REFERENCES TipoDeTratamiento (id)
;

ALTER TABLE Tratamiento ADD CONSTRAINT FK_Tratamiento_Paciente 
	FOREIGN KEY (pacienteId) REFERENCES Paciente (id)
;

ALTER TABLE Tratamiento ADD CONSTRAINT FK_Tratamiento_Tratamiento 
	FOREIGN KEY (tratamientoAsociadoId) REFERENCES Tratamiento (id)
;
