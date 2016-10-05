--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.1

-- Started on 2016-03-15 19:02:53

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2165 (class 0 OID 16396)
-- Dependencies: 182
-- Data for Name: especialidad; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO especialidad VALUES (1, 'Kinesiología');


--
-- TOC entry 2175 (class 0 OID 0)
-- Dependencies: 181
-- Name: especialidad_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('especialidad_id_seq', 1, false);


--
-- TOC entry 2168 (class 0 OID 16438)
-- Dependencies: 196
-- Data for Name: obrasocial; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO obrasocial VALUES (1,'A.M.O. y E.I.A.G. Mut. Empl. SUTIAGA','');
INSERT INTO obrasocial VALUES (2,'ACA SALUD','');
INSERT INTO obrasocial VALUES (3,'AMEP','');
INSERT INTO obrasocial VALUES (4,'AMUR','');
INSERT INTO obrasocial VALUES (5,'AMUR Pl. Verde Extra Capita','');
INSERT INTO obrasocial VALUES (6,'ARGUS SALUD S.R.L.','');
INSERT INTO obrasocial VALUES (7,'ASOC. ECLESIASTICA SAN. PEDRO','');
INSERT INTO obrasocial VALUES (8,'ASOC. MUTUAL PENC.SOC.LEY 5110','');
INSERT INTO obrasocial VALUES (9,'ASOC. TRABAJ. FARMACIA - SANTA FE','');
INSERT INTO obrasocial VALUES (10,'ASOCIART ART ','');
INSERT INTO obrasocial VALUES (11,'BERKLEY INT.','');
INSERT INTO obrasocial VALUES (12,'CAJA FORENSE','');
INSERT INTO obrasocial VALUES (13,'CAJA PREV.SOC.PROF.ING.STA.FE','');
INSERT INTO obrasocial VALUES (14,'CENTRO ASISTENCIAL','');
INSERT INTO obrasocial VALUES (15,'CERVE SALUD','');
INSERT INTO obrasocial VALUES (16,'CIENCIAS ECONOMICAS','');
INSERT INTO obrasocial VALUES (17,'CONFITEROS Y PASTELEROS','');
INSERT INTO obrasocial VALUES (18,'CRUZ AZUL SALUD','');
INSERT INTO obrasocial VALUES (19,'DAS - GRUPO MEDICO ACE','');
INSERT INTO obrasocial VALUES (20,'DASUTeN','');
INSERT INTO obrasocial VALUES (21,'DIREC. BIENESTAR DE LA ARMADA','');
INSERT INTO obrasocial VALUES (22,'DOCTHOS - Pl. Global - Premium ','');
INSERT INTO obrasocial VALUES (23,'EXPERTA ART','');
INSERT INTO obrasocial VALUES (24,'FE SALUD','');
INSERT INTO obrasocial VALUES (25,'FEDERACION PATRONAL SEGUROS S.A.','');
INSERT INTO obrasocial VALUES (26,'FEDERADA SALUD','');
INSERT INTO obrasocial VALUES (27,'FEDERADA SALUD G1','');
INSERT INTO obrasocial VALUES (28,'FEDERADA SALUD  G 2','');
INSERT INTO obrasocial VALUES (29,'FEDERADA SALUD  G 3','');
INSERT INTO obrasocial VALUES (30,'GALENO','');
INSERT INTO obrasocial VALUES (31,'GALENO ART','');
INSERT INTO obrasocial VALUES (32,'GERDANNA SALUD','');
INSERT INTO obrasocial VALUES (33,'HIELO - ASIS ','');
INSERT INTO obrasocial VALUES (34,'IAPOS - ACCIDENTE DE TRABAJO','');
INSERT INTO obrasocial VALUES (35,'IAPOS','');
INSERT INTO obrasocial VALUES (36,'IAPOS - DPTO. 9 DE JULIO','');
INSERT INTO obrasocial VALUES (37,'IAPOS - DPTO. SAN CRISTOBAL','');
INSERT INTO obrasocial VALUES (38,'IAPOS - DPTO. SAN GERONIMO','');
INSERT INTO obrasocial VALUES (39,'IAPOS INTERIOR','');
INSERT INTO obrasocial VALUES (40,'INST.SEGURO DE ENTRE RIOS','');
INSERT INTO obrasocial VALUES (41,'INTEGRAL SALUD S.A. (ex MAS)','');
INSERT INTO obrasocial VALUES (42,'IOSE','');
INSERT INTO obrasocial VALUES (43,'JERARQUICOS SALUD','');
INSERT INTO obrasocial VALUES (44,'LA HOLANDO SUDAMERICANA ART','');
INSERT INTO obrasocial VALUES (45,'LA MERIDIONAL A.R.T.','');
INSERT INTO obrasocial VALUES (46,'LA SEGUNDA A.R.T.','');
INSERT INTO obrasocial VALUES (47,'LIDERAR ART','');
INSERT INTO obrasocial VALUES (48,'LUIS PASTEUR con AUTORIZ.','');
INSERT INTO obrasocial VALUES (49,'LUZ Y FUERZA','');
INSERT INTO obrasocial VALUES (50,'MARINA MERCANTE - OSEMM','');
INSERT INTO obrasocial VALUES (51,'MEDICAR WORK','');
INSERT INTO obrasocial VALUES (52,'MEDICUS','');
INSERT INTO obrasocial VALUES (53,'MEDIFE S.A. ','');
INSERT INTO obrasocial VALUES (54,'NAM SALUD','');
INSERT INTO obrasocial VALUES (55,'MUTUAL PERS.AGUA Y ENERGIA','');
INSERT INTO obrasocial VALUES (56,'NUEVA MEDICINA','');
INSERT INTO obrasocial VALUES (57,'O.SOC.PATRONES DE CABOTAJE ','');
INSERT INTO obrasocial VALUES (58,'O.SOC.PERSONAL DE FARMACIA','');
INSERT INTO obrasocial VALUES (59,'OBRA SOCIAL DEL PERS.MARITIMO','');
INSERT INTO obrasocial VALUES (60,'OMINT','');
INSERT INTO obrasocial VALUES (61,'OPDEA','');
INSERT INTO obrasocial VALUES (62,'OSAPM','');
INSERT INTO obrasocial VALUES (63,'OSCRAIA - REMISES','');
INSERT INTO obrasocial VALUES (64,'OSDE','');
INSERT INTO obrasocial VALUES (65,'OSDE BINARIO - Pl. 210','');
INSERT INTO obrasocial VALUES (66,'OSDE BINARIO - Pl. 310','');
INSERT INTO obrasocial VALUES (67,'OSDE BINARIO - Pl. 410','');
INSERT INTO obrasocial VALUES (68,'OSDOP - CELSALUD S.R.L.','');
INSERT INTO obrasocial VALUES (69,'OSETy A - GRUPO MEDICO ACE','');
INSERT INTO obrasocial VALUES (70,'OSFFENTOS - SCIS S.A. (Obras Sanitarias)','');
INSERT INTO obrasocial VALUES (71,'OSIAD (Aceiteros)','');
INSERT INTO obrasocial VALUES (72,'OSMATA','');
INSERT INTO obrasocial VALUES (73,'OSPA VIAL','');
INSERT INTO obrasocial VALUES (74,'OSPAC','');
INSERT INTO obrasocial VALUES (75,'OSPACP- PERS.AUX.-ENSALUD','');
INSERT INTO obrasocial VALUES (76,'OSPAT (Pers. del Turf)','');
INSERT INTO obrasocial VALUES (77,'OSPE - GRUPO MEDICO ACE','');
INSERT INTO obrasocial VALUES (78,'OSPEDyC','');
INSERT INTO obrasocial VALUES (79,'OSPeGaP.','');
INSERT INTO obrasocial VALUES (80,'OSPESGA','');
INSERT INTO obrasocial VALUES (81,'OSPIF (Industria Fideera)','');
INSERT INTO obrasocial VALUES (82,'OSPIL','');
INSERT INTO obrasocial VALUES (83,'OSPIM (MADERA)','');
INSERT INTO obrasocial VALUES (84,'OSPIQYP QUIMICOS Y PETROQUIMICOS','');
INSERT INTO obrasocial VALUES (85,'OSPLAD - SANTA FE RED','');
INSERT INTO obrasocial VALUES (86,'OSPM - MOSAISTAS','');
INSERT INTO obrasocial VALUES (87,'OSPSA','');
INSERT INTO obrasocial VALUES (88,'OSPSIP - ASIS ','');
INSERT INTO obrasocial VALUES (89,'OSSALARA (empl. de Casinos)','');
INSERT INTO obrasocial VALUES (90,'OSSEG ','');
INSERT INTO obrasocial VALUES (91,'OSTEP - SCIS S.A. (educ. privada)','');
INSERT INTO obrasocial VALUES (92,'OSTRAC - SCIS S.A.','');
INSERT INTO obrasocial VALUES (93,'PAPEL Y CARTON - GRUPO MEDICO ACE','');
INSERT INTO obrasocial VALUES (94,'PODER JUDICIAL NAC.','');
INSERT INTO obrasocial VALUES (95,'POLICIA FEDERAL','');
INSERT INTO obrasocial VALUES (96,'PREVENCION A.R.T. S.A.','');
INSERT INTO obrasocial VALUES (97,'PREVENCION SALUD','');
INSERT INTO obrasocial VALUES (98,'PROVINCIA A.R.T.','');
INSERT INTO obrasocial VALUES (99,'QBE ARGENTINA ART','');
INSERT INTO obrasocial VALUES (100,'RECOL. Y BARRIDO - ACE','');
INSERT INTO obrasocial VALUES (101,'S.M. SALUD','');
INSERT INTO obrasocial VALUES (102,'S.U.T.I.A.G.A.','');
INSERT INTO obrasocial VALUES (103,'SADAIC','');
INSERT INTO obrasocial VALUES (104,'SAN CRISTOBAL S.M.S.G.','');
INSERT INTO obrasocial VALUES (105,'SANCOR SALUD','');
INSERT INTO obrasocial VALUES (106,'SANCOR 1000 - 500 - C','');
INSERT INTO obrasocial VALUES (107,'SANCOR 4000 - 3000 - 2000','');
INSERT INTO obrasocial VALUES (108,'SANCOR SEGUROS','');
INSERT INTO obrasocial VALUES (109,'S.M.G. ART','');
INSERT INTO obrasocial VALUES (110,'SWISS MEDICAL GROUP S.A.','');
INSERT INTO obrasocial VALUES (111,'TV Salud - SAT','');
INSERT INTO obrasocial VALUES (112,'U.T.A. - O.S.C.T.C.P.','');
INSERT INTO obrasocial VALUES (113,'UNION PERSONAL ','');
INSERT INTO obrasocial VALUES (114,'UNL','');
INSERT INTO obrasocial VALUES (115,'ATSA','');
INSERT INTO obrasocial VALUES (116,'AGUA Y ENERGÍA','');


--
-- TOC entry 2176 (class 0 OID 0)
-- Dependencies: 195
-- Name: obrasocial_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('obrasocial_id_seq', 1, true);


--
-- TOC entry 2170 (class 0 OID 16444)
-- Dependencies: 198
-- Data for Name: tipodetratamiento; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO tipodetratamiento VALUES (1,1,'Fisiokinesioterapia',45,TRUE);
INSERT INTO tipodetratamiento VALUES (2,1,'Kinesioterapia',45,TRUE);
INSERT INTO tipodetratamiento VALUES (3,1,'Fisioterapia',45,TRUE);
INSERT INTO tipodetratamiento VALUES (4,1,'Tratamiento Neurológico',45,TRUE);
INSERT INTO tipodetratamiento VALUES (5,1,'Drenaje Linfático',60,TRUE);
INSERT INTO tipodetratamiento VALUES (6,1,'Kinesioterapia respiratoria',15,TRUE);
INSERT INTO tipodetratamiento VALUES (7,1,'Módulo Discapacidad',45,TRUE);
INSERT INTO tipodetratamiento VALUES (8,1,'Estimulación Temprana',45,TRUE);
INSERT INTO tipodetratamiento VALUES (9,1,'Gimnasia terapéutica',60,FALSE);
INSERT INTO tipodetratamiento VALUES (10,1,'Estética Masajes',90,FALSE);
INSERT INTO tipodetratamiento VALUES (11,1,'Estética Electrodos',60,FALSE);
INSERT INTO tipodetratamiento VALUES (12,1,'FKT + Magnetoterapia',45,TRUE);

--
-- TOC entry 2177 (class 0 OID 0)
-- Dependencies: 197
-- Name: tipodetratamiento_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tipodetratamiento_id_seq', 1, true);


--
-- TOC entry 2166 (class 0 OID 16433)
-- Dependencies: 194
-- Data for Name: tipotratamientoobrasocial; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO tipotratamientoobrasocial VALUES (1,35,'25.01.01-25.01.02',25);
INSERT INTO tipotratamientoobrasocial VALUES (1,113,'25.01.01-25.01.02',25);
INSERT INTO tipotratamientoobrasocial VALUES (1,76,'25.01.01-25.01.02',25);
INSERT INTO tipotratamientoobrasocial VALUES (1,78,'25.01.01-25.01.02-25.50.01',25);
INSERT INTO tipotratamientoobrasocial VALUES (1,10,'25.01.01-25.01.02-25.50.01',25);
INSERT INTO tipotratamientoobrasocial VALUES (1,115,'25.01.01-25.01.02',25);
INSERT INTO tipotratamientoobrasocial VALUES (1,116,'25.01.01-25.01.02-25.50.01',25);


-- Completed on 2016-03-15 19:02:53

--
-- PostgreSQL database dump complete
--

