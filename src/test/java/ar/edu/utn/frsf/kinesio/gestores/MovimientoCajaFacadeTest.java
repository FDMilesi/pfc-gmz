package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.MovimientoCaja;
import com.sun.imageio.plugins.common.BogusColorSpace;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MovimientoCajaFacadeTest {

    static MovimientoCajaFacade movimientoCajaFacade;
    static List<MovimientoCaja> listaMovimientos;

    @BeforeClass
    public static void init() {
        movimientoCajaFacade = new MovimientoCajaFacade();

        MovimientoCaja primerMovimiento = movimientoCajaFacade.initMovimientoCaja(MovimientoCaja.TipoMovimiento.INGRESO, null);
        primerMovimiento.setSaldo(new BigDecimal(100));
        MovimientoCaja segundoMovimiento = movimientoCajaFacade.initMovimientoCaja(MovimientoCaja.TipoMovimiento.EGRESO, null);
        segundoMovimiento.setSaldo(new BigDecimal(50));

        //los debo agregar ordenados de forma descendente
        listaMovimientos = new ArrayList<>();
        listaMovimientos.add(segundoMovimiento);
        listaMovimientos.add(primerMovimiento);
        //Hasta aca el ultimo saldo es 50
    }

    @Test
    public void debeCalcularSaldoInicial() {
        List<MovimientoCaja> listaMovimientosVacia = Arrays.asList();

        MovimientoCaja movimiento = movimientoCajaFacade.initMovimientoCaja(MovimientoCaja.TipoMovimiento.INGRESO, null);
        movimiento.setValorUnitario(new BigDecimal(100));

        movimientoCajaFacade.calcularNuevoSaldoOnCreate(movimiento, listaMovimientosVacia);
        //Como no habia movimientos previos e hice un ingreso de 100, el saldo debe se 100
        Assert.assertEquals(new BigDecimal(100), movimiento.getSaldo());
    }

    @Test
    public void debeCalcularSaldoAnteUnIngreso() {
        MovimientoCaja movimiento = movimientoCajaFacade.initMovimientoCaja(MovimientoCaja.TipoMovimiento.INGRESO, null);
        movimiento.setValorUnitario(new BigDecimal(25));

        movimientoCajaFacade.calcularNuevoSaldoOnCreate(movimiento, listaMovimientos);
        //Tenia 50 y le ingrese 25
        Assert.assertEquals(new BigDecimal(75), movimiento.getSaldo());
    }

    @Test
    public void debeCalcularSaldoAnteUnEgreso() {
        MovimientoCaja movimiento = movimientoCajaFacade.initMovimientoCaja(MovimientoCaja.TipoMovimiento.EGRESO, null);
        movimiento.setValorUnitario(new BigDecimal(25));

        movimientoCajaFacade.calcularNuevoSaldoOnCreate(movimiento, listaMovimientos);
        //Tenia 50 y le quite 25
        Assert.assertEquals(new BigDecimal(25), movimiento.getSaldo());
    }
}
