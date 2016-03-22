/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.edu.utn.frsf.kinesio.controllers.util;

import java.util.Date;

/**
 *
 * @author Fran
 */
public class CreacionSesionEvento {
    
    private Date date;

    public CreacionSesionEvento() {
    }
    
    public CreacionSesionEvento(Date date){
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}