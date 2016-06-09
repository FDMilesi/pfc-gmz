package ar.edu.utn.frsf.kinesio.controllers.adaptadores;

import ar.edu.utn.frsf.kinesio.controllers.SesionController;
import ar.edu.utn.frsf.kinesio.gestores.SesionFacade;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 */
@TestQualifier
@RequestScoped
public class SesionControllerHijo extends SesionController{

}
