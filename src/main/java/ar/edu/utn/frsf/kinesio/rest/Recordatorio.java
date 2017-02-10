package ar.edu.utn.frsf.kinesio.rest;

import java.io.Serializable;

public class Recordatorio implements Serializable {

    private static final long serialVersionUID = 1L;

    String nombreContacto;
    String mensaje;

    public Recordatorio() {
    }

    public Recordatorio(String nombreContacto, String mensaje) {
        this.nombreContacto = nombreContacto;
        this.mensaje = mensaje;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
