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

INSERT INTO obrasocial VALUES (2, 'Sancor Salud', 'www.sancor.com.ar');
INSERT INTO obrasocial VALUES (1, 'IAPOS', 'www.iapos.com');
INSERT INTO obrasocial VALUES (3, 'Amur', 'www.amur.com');
INSERT INTO obrasocial VALUES (4, 'Swiss Medical', 'www.sm.com');
INSERT INTO obrasocial VALUES (5, 'OSDE', 'www.osde.com');
INSERT INTO obrasocial VALUES (6, 'Jerárquicos Salud', 'www.js.com');
INSERT INTO obrasocial VALUES (7, 'Medife', 'www.medife.com');


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

INSERT INTO tipodetratamiento VALUES (1, 1, 'Fisiokinesioterapia', 45, true);
INSERT INTO tipodetratamiento VALUES (3, 1, 'Drenaje Linfático', 60, true);
INSERT INTO tipodetratamiento VALUES (4, 1, 'Rehabilitación Neurológica', 45, true);
INSERT INTO tipodetratamiento VALUES (5, 1, 'Estética Electrodos', 60, false);
INSERT INTO tipodetratamiento VALUES (6, 1, 'Estética Masajes', 90, false);
INSERT INTO tipodetratamiento VALUES (2, 1, 'Kinesioterapia Respiratoria', 15, true);
INSERT INTO tipodetratamiento VALUES (7, 1, 'Gimnasia Terapéutica', 60, false);


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

INSERT INTO tipotratamientoobrasocial VALUES (3, 1, '1234', 25);
INSERT INTO tipotratamientoobrasocial VALUES (3, 2, '1234', 25);
INSERT INTO tipotratamientoobrasocial VALUES (3, 3, '1234', 25);
INSERT INTO tipotratamientoobrasocial VALUES (3, 4, '1234', 25);
INSERT INTO tipotratamientoobrasocial VALUES (3, 5, '1234', 25);
INSERT INTO tipotratamientoobrasocial VALUES (3, 6, '1234', 25);
INSERT INTO tipotratamientoobrasocial VALUES (3, 7, '1234', 25);
INSERT INTO tipotratamientoobrasocial VALUES (1, 1, '250101-250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (1, 2, '250101-250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (1, 3, '250101-250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (1, 4, '250101-250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (4, 1, '4567', 25);
INSERT INTO tipotratamientoobrasocial VALUES (1, 6, '250101-250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (1, 5, '250101-250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (1, 7, '250101-250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (4, 2, '4567', 25);
INSERT INTO tipotratamientoobrasocial VALUES (4, 3, '4567', 25);
INSERT INTO tipotratamientoobrasocial VALUES (2, 1, '250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (4, 4, '4567', 25);
INSERT INTO tipotratamientoobrasocial VALUES (4, 5, '4567', 25);
INSERT INTO tipotratamientoobrasocial VALUES (4, 6, '4567', 25);
INSERT INTO tipotratamientoobrasocial VALUES (4, 7, '4567', 25);
INSERT INTO tipotratamientoobrasocial VALUES (2, 2, '250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (2, 3, '250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (2, 4, '250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (2, 5, '250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (2, 6, '250102', 25);
INSERT INTO tipotratamientoobrasocial VALUES (2, 7, '250102', 25);


-- Completed on 2016-03-15 19:02:53

--
-- PostgreSQL database dump complete
--

