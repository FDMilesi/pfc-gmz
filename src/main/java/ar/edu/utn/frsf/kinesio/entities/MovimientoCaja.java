package ar.edu.utn.frsf.kinesio.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "movimientocaja")
@NamedQueries({
    @NamedQuery(name = "Movimientocaja.findAll", query = "SELECT m FROM MovimientoCaja m"),
    @NamedQuery(name = "Movimientocaja.findById", query = "SELECT m FROM MovimientoCaja m WHERE m.id = :id"),
    @NamedQuery(name = "Movimientocaja.findByDescripcion", query = "SELECT m FROM MovimientoCaja m WHERE m.descripcion = :descripcion"),
    @NamedQuery(name = "Movimientocaja.findByCantidad", query = "SELECT m FROM MovimientoCaja m WHERE m.cantidad = :cantidad"),
    @NamedQuery(name = "Movimientocaja.findByTipomovimiento", query = "SELECT m FROM MovimientoCaja m WHERE m.tipomovimiento = :tipomovimiento"),
    @NamedQuery(name = "Movimientocaja.findByFechayhora", query = "SELECT m FROM MovimientoCaja m WHERE m.fechayhora = :fechayhora"),
    @NamedQuery(name = "Movimientocaja.findByValorUnitario", query = "SELECT m FROM MovimientoCaja m WHERE m.valorUnitario = :valorUnitario"),
    @NamedQuery(name = "Movimientocaja.findByCaja", query = "SELECT m FROM MovimientoCaja m WHERE m.caja = :caja ORDER BY m.fechayhora DESC"),
    @NamedQuery(name = "Movimientocaja.findBySaldo", query = "SELECT m FROM MovimientoCaja m WHERE m.saldo = :saldo")})

public class MovimientoCaja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(max = 30)
    @Column(name = "descripcion")
    private String descripcion;

    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private short cantidad;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "tipomovimiento")
    private String tipomovimiento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fechayhora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechayhora;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valorunitario")
    private BigDecimal valorUnitario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "saldo")
    private BigDecimal saldo;

    @JoinColumn(name = "cajaid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Caja caja;

    @JoinColumn(name = "conceptoid", referencedColumnName = "id")
    @NotNull(message = "Seleccione un concepto")
    @ManyToOne(optional = false)
    private Concepto concepto;

    public MovimientoCaja() {
    }

    public MovimientoCaja(Integer id) {
        this.id = id;
    }

    public MovimientoCaja(Integer id, short cantidad, String tipomovimiento, Date fechayhora, BigDecimal valorUnitario, BigDecimal saldo) {
        this.id = id;
        this.cantidad = cantidad;
        this.tipomovimiento = tipomovimiento;
        this.fechayhora = fechayhora;
        this.valorUnitario = valorUnitario;
        this.saldo = saldo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getCantidad() {
        return cantidad;
    }

    public void setCantidad(short cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipomovimiento() {
        return tipomovimiento;
    }

    public void setTipomovimiento(String tipomovimiento) {
        this.tipomovimiento = tipomovimiento;
    }

    public Date getFechayhora() {
        return fechayhora;
    }

    public void setFechayhora(Date fechayhora) {
        this.fechayhora = fechayhora;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getMonto() {
        return this.valorUnitario.multiply(new BigDecimal(cantidad));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientoCaja)) {
            return false;
        }
        MovimientoCaja other = (MovimientoCaja) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.edu.utn.frsf.kinesio.entities.Movimientocaja[ id=" + id + " ]";
    }

    public enum TipoMovimiento {
        INGRESO, EGRESO
    }
}
