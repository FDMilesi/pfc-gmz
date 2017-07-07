package ar.edu.utn.frsf.kinesio.gestores;

import ar.edu.utn.frsf.kinesio.entities.Caja;
import ar.edu.utn.frsf.kinesio.entities.Concepto;
import ar.edu.utn.frsf.kinesio.entities.MovimientoCaja;
import ar.edu.utn.frsf.kinesio.entities.MovimientoCaja.TipoMovimiento;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class MovimientoCajaFacade extends AbstractFacade<MovimientoCaja> {

    @PersistenceContext(unitName = "ar.edu.utn.frsf_kinesio_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MovimientoCajaFacade() {
        super(MovimientoCaja.class);
    }

    public MovimientoCaja initMovimientoCaja(TipoMovimiento tipoMovimiento, Caja caja) {
        MovimientoCaja movimientoCaja = new MovimientoCaja();
        movimientoCaja.setCaja(caja);
        movimientoCaja.setFechayhora(new Date());
        movimientoCaja.setSaldo(BigDecimal.ZERO);
        movimientoCaja.setTipomovimiento(tipoMovimiento.name());
        movimientoCaja.setCantidad((short) 1);
        movimientoCaja.setValorUnitario(BigDecimal.ZERO);
        return movimientoCaja;
    }

    @Override
    public void create(MovimientoCaja movimiento) {
        //Tengo que buscar el ultimo saldo
        List<MovimientoCaja> movimientos = getMovimientosByCaja(movimiento.getCaja());

        this.calcularNuevoSaldoOnCreate(movimiento, movimientos);

        super.create(movimiento);
    }

    /**
     * *
     * Setea al movimiento pasado como parámetro el valor que le corresponde en
     * el saldo. La lista de movimientos debe estar ordenada según la fecha de
     * los movimientos en forma descendente
     *
     * @param movimiento Movimiento al cual se le va a calcular el saldo
     * @param totalMovimientos Lista ordenada descendentemente del total de
     * movimientos de la caja
     */
    protected void calcularNuevoSaldoOnCreate(MovimientoCaja movimiento, List<MovimientoCaja> totalMovimientos) {
        BigDecimal saldoAnterior;

        if (totalMovimientos.isEmpty()) { //Si no hay movimientos el saldo inicial es cero
            saldoAnterior = BigDecimal.ZERO;
        } else {
            saldoAnterior = totalMovimientos.get(0).getSaldo();
        }

        if (movimiento.getTipomovimiento().equals(MovimientoCaja.TipoMovimiento.EGRESO.name())) {
            movimiento.setSaldo(saldoAnterior.subtract(movimiento.getMonto()));
        } else {
            movimiento.setSaldo(saldoAnterior.add(movimiento.getMonto()));
        }
    }

    //Los retorna ordenados
    public List<MovimientoCaja> getMovimientosByCaja(Caja caja) {
        return getEntityManager()
                .createNamedQuery("Movimientocaja.findByCaja")
                .setParameter("caja", caja).getResultList();
    }

    public Caja getCaja() {
        return this.getEntityManager().find(Caja.class, (short) 1);
    }

    public List<Concepto> findAllConceptos() {
        return this.getEntityManager().createNamedQuery("Concepto.findAll", Concepto.class).getResultList();
    }
}
