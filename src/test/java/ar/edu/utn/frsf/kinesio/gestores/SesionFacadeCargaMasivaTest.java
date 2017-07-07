package ar.edu.utn.frsf.kinesio.gestores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class SesionFacadeCargaMasivaTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_FORMAT_HORA = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private SesionFacade sesionFacade;

    private static Date feriadoUno;
    private static Date feriadoDos;
    private static List<Date> listaFeriados;

    @BeforeClass
    public static final void fsetUpClass() throws ParseException {
        feriadoUno = DATE_FORMAT.parse("9/2/2017");
        feriadoDos = DATE_FORMAT.parse("13/2/2017");
        listaFeriados = new ArrayList<>();
        listaFeriados.add(feriadoUno);
        listaFeriados.add(feriadoDos);
    }

    @Before
    public void setUp() {
        sesionFacade = spy(SesionFacade.class);
        doReturn(listaFeriados).when(sesionFacade).listaFeriadosAsDateList();
    }

    @Test
    public void deberianRetornarListaVacia() throws ParseException {

        LocalDate diaInicial = LocalDate.now().withYear(2017).withMonth(2).withDayOfMonth(1);

        Date horaLunes = DATE_FORMAT_HORA.parse("3/8/1991 10:30");

        Map<String, Date> diasYHorarios = new HashMap<>();
        diasYHorarios.put(DayOfWeek.MONDAY.name(), horaLunes);

        //Ejecuto la prueba. Le paso 0(cero) en la cantidad de dias a repetir
        List<Date> listaFechas = sesionFacade.getFechasParaCargaMasiva(diasYHorarios, 0, diaInicial);

        assertTrue(listaFechas.isEmpty());
    }

    @Test
    public void deberianCoincidirFechasCargaMasivaUnDia() throws ParseException {

        LocalDate diaInicial = LocalDate.now().withYear(2017).withMonth(2).withDayOfMonth(1);

        Date horaLunes = DATE_FORMAT_HORA.parse("3/8/1991 10:30");

        Map<String, Date> diasYHorarios = new HashMap<>();
        diasYHorarios.put(DayOfWeek.MONDAY.name(), horaLunes);

        //Ejecuto la prueba
        List<Date> listaFechas = sesionFacade.getFechasParaCargaMasiva(diasYHorarios, 1, diaInicial);

        assertEquals(DATE_FORMAT_HORA.parse("6/2/2017 10:30"), listaFechas.get(0));
    }

    @Test
    public void deberianCoincidirFechasCargaMasivaDosDias() throws ParseException {

        LocalDate diaInicial = LocalDate.now().withYear(2017).withMonth(2).withDayOfMonth(1);

        Date horaLunes = DATE_FORMAT_HORA.parse("3/8/1991 10:30");
        Date horaMiercoles = DATE_FORMAT_HORA.parse("25/5/2010 17:30");

        Map<String, Date> diasYHorarios = new HashMap<>();
        diasYHorarios.put(DayOfWeek.MONDAY.name(), horaLunes);
        diasYHorarios.put(DayOfWeek.WEDNESDAY.name(), horaMiercoles);

        //Ejecuto la prueba
        List<Date> listaFechas = sesionFacade.getFechasParaCargaMasiva(diasYHorarios, 2, diaInicial);

        assertEquals(DATE_FORMAT_HORA.parse("6/2/2017 10:30"), listaFechas.get(0));
        assertEquals(DATE_FORMAT_HORA.parse("8/2/2017 17:30"), listaFechas.get(1));
    }

    @Test
    public void deberianCoincidirFechasCargaMasivaFeriados() throws ParseException {

        LocalDate diaInicial = LocalDate.now().withYear(2017).withMonth(2).withDayOfMonth(1);

        Date horaJueves = DATE_FORMAT_HORA.parse("3/8/1991 10:30");

        Map<String, Date> diasYHorarios = new HashMap<>();
        diasYHorarios.put(DayOfWeek.THURSDAY.name(), horaJueves);

        //Ejecuto la prueba
        List<Date> listaFechas = sesionFacade.getFechasParaCargaMasiva(diasYHorarios, 3, diaInicial);

        assertEquals(DATE_FORMAT_HORA.parse("2/2/2017 10:30"), listaFechas.get(0));
        //Saltea el 9 que esta marcado como feriado
        assertEquals(DATE_FORMAT_HORA.parse("16/2/2017 10:30"), listaFechas.get(1));
        assertEquals(DATE_FORMAT_HORA.parse("23/2/2017 10:30"), listaFechas.get(2));
    }
    
    @Test
    public void deberianCoincidirFechasCargaMasivaCambioAnio() throws ParseException {

        LocalDate diaInicial = LocalDate.now().withYear(2016).withMonth(12).withDayOfMonth(28);

        Date horaViernes = DATE_FORMAT_HORA.parse("3/8/1991 10:30");

        Map<String, Date> diasYHorarios = new HashMap<>();
        diasYHorarios.put(DayOfWeek.FRIDAY.name(), horaViernes);

        //Ejecuto la prueba
        List<Date> listaFechas = sesionFacade.getFechasParaCargaMasiva(diasYHorarios, 2, diaInicial);

        assertEquals(DATE_FORMAT_HORA.parse("30/12/2016 10:30"), listaFechas.get(0));
        assertEquals(DATE_FORMAT_HORA.parse("6/1/2017 10:30"), listaFechas.get(1));
    }
}
