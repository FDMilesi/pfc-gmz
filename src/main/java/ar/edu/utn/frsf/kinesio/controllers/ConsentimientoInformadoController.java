package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 */
@Named("consentimientoInformadoController")
@ViewScoped
public class ConsentimientoInformadoController implements Serializable {

    public ConsentimientoInformadoController() {

    }

    @PostConstruct
    protected void init(){
        getListaOrdenesReporte();
    }
    
    //Metodos de negocio
    public void getListaOrdenesReporte() {
        tratamiento = (Tratamiento) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("tratamientoParaConsentimiento");
        
        nombreApellido = tratamiento.getPaciente().getApellido()+", "+tratamiento.getPaciente().getNombre();
        domicilio = tratamiento.getPaciente().getDomicilio();
        fechaNacimiento = tratamiento.getPaciente().getFechaDeNacimiento();
        numeroDocumento = tratamiento.getPaciente().getDni();
        profesionalDerivante = tratamiento.getMedicoDerivante();
        obraSocial = tratamiento.getPaciente().getObraSocial().getNombre();
        diagnosticoIngreso = tratamiento.getDiagnostico();
        cantidadSesionesPrevistas = tratamiento.getCantidadDeSesiones();
        
    }
    
    private Tratamiento tratamiento;
    private String nombreApellido;
    private String domicilio;
    private String localidad;
    private Date fechaNacimiento;
    private String estadoCivil;
    private String tipoDocumento;
    private String numeroDocumento;
    private String profesionalDerivante;
    private String numeroMatriculaProfesional;
    private String fechaOrdenMedica;
    private String obraSocial;
    private String fechaAutorizacion;
    private String cobertura;
    private String diagnosticoIngreso;
    private String contraindicaciones;
    private String antecedentesPersonales;
    private String agentesAplicar;  //Art 14 Ley 10392
    private int cantidadSesionesPrevistas;
    private String evaluacionAlEgreso;
    private boolean altaProvisoria;
    private Date fechaAltaProvisoria;
    private boolean altaDefinitiva;
    private Date fechaAltaDefinitiva;
    
    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getProfesionalDerivante() {
        return profesionalDerivante;
    }

    public void setProfesionalDerivante(String profesionalDerivante) {
        this.profesionalDerivante = profesionalDerivante;
    }

    public String getNumeroMatriculaProfesional() {
        return numeroMatriculaProfesional;
    }

    public void setNumeroMatriculaProfesional(String numeroMatriculaProfesional) {
        this.numeroMatriculaProfesional = numeroMatriculaProfesional;
    }

    public String getFechaOrdenMedica() {
        return fechaOrdenMedica;
    }

    public void setFechaOrdenMedica(String fechaOrdenMedica) {
        this.fechaOrdenMedica = fechaOrdenMedica;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public String getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(String fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getDiagnosticoIngreso() {
        return diagnosticoIngreso;
    }

    public void setDiagnosticoIngreso(String diagnosticoIngreso) {
        this.diagnosticoIngreso = diagnosticoIngreso;
    }

    public String getContraindicaciones() {
        return contraindicaciones;
    }

    public void setContraindicaciones(String contraindicaciones) {
        this.contraindicaciones = contraindicaciones;
    }

    public String getAntecedentesPersonales() {
        return antecedentesPersonales;
    }

    public void setAntecedentesPersonales(String antecedentesPersonales) {
        this.antecedentesPersonales = antecedentesPersonales;
    }

    public String getAgentesAplicar() {
        return agentesAplicar;
    }

    public void setAgentesAplicar(String agentesAplicar) {
        this.agentesAplicar = agentesAplicar;
    }

    public int getCantidadSesionesPrevistas() {
        return cantidadSesionesPrevistas;
    }

    public void setCantidadSesionesPrevistas(int cantidadSesionesPrevistas) {
        this.cantidadSesionesPrevistas = cantidadSesionesPrevistas;
    }

    public String getEvaluacionAlEgreso() {
        return evaluacionAlEgreso;
    }

    public void setEvaluacionAlEgreso(String evaluacionAlEgreso) {
        this.evaluacionAlEgreso = evaluacionAlEgreso;
    }

    public boolean isAltaProvisoria() {
        return altaProvisoria;
    }

    public void setAltaProvisoria(boolean altaProvisoria) {
        this.altaProvisoria = altaProvisoria;
    }

    public Date getFechaAltaProvisoria() {
        return fechaAltaProvisoria;
    }

    public void setFechaAltaProvisoria(Date fechaAltaProvisoria) {
        this.fechaAltaProvisoria = fechaAltaProvisoria;
    }

    public boolean isAltaDefinitiva() {
        return altaDefinitiva;
    }

    public void setAltaDefinitiva(boolean altaDefinitiva) {
        this.altaDefinitiva = altaDefinitiva;
    }

    public Date getFechaAltaDefinitiva() {
        return fechaAltaDefinitiva;
    }

    public void setFechaAltaDefinitiva(Date fechaAltaDefinitiva) {
        this.fechaAltaDefinitiva = fechaAltaDefinitiva;
    }

    public String getCobertura() {
        return cobertura;
    }

    public void setCobertura(String cobertura) {
        this.cobertura = cobertura;
    }

    
    

}
