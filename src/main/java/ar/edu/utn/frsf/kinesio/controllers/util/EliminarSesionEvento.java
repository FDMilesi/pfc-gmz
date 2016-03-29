/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers.util;

/**
 *
 * @author Fran
 */
public class EliminarSesionEvento {
    private String idSesionEliminada;
    
    public EliminarSesionEvento() {

    }
    
    public EliminarSesionEvento(String idSesionEliminada){
        this.idSesionEliminada = idSesionEliminada;
    }

    public String getIdSesionEliminada() {
        return idSesionEliminada;
    }

    public void setIdSesionEliminada(String idSesionEliminada) {
        this.idSesionEliminada = idSesionEliminada;
    }
}