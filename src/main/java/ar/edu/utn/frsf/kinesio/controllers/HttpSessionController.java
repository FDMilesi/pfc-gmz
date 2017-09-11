package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.controllers.util.UserRoles;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.Principal;
import javax.faces.context.FacesContext;

/**
 * Bean encargado de manipular la sesión del usuario logueado en el sistema.
 * Permite invalidar la sesión y obtener información acerca del usuario. Cada
 * instancia de este controlador sólo tiene acceso a la sesión que lo creó.
 */
@Named(value = "httpSessionController")
@SessionScoped
public class HttpSessionController implements Serializable {

    private String usuarioLogueado;
    private String rolUsuario;

    /**
     * Creates a new instance of SessionController
     */
    public HttpSessionController() {
    }

    /**
     * Invalida la sesión que el navegador establece con el sistema, requiriendo
     * nuevamente las credenciales (usuario+contraseña) para acceder a recursos
     * protegidos
     *
     * @return Outcome que permite identificar el logout de modo que pueda
     * establecerse una ruta explícita en el archivo faces-config.xml, donde se
     * especifica donde redirigir luego del logout.
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "logout";
    }

    /**
     * Retorna el nombre del usuario (username) que se introdujo en el
     * formulario de login a la hora de iniciar la sesión corriente.
     *
     * @return Nombre del usuario logeado. En caso de ser null retorna un texto
     * alternativo.
     */
    public String getUsuarioLogueado() {
        if (usuarioLogueado == null) {
            Principal usuario = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
            if (usuario != null) {
                usuarioLogueado = usuario.getName();
            } else {
                usuarioLogueado = "Sesión no iniciada";
            }
        }

        return usuarioLogueado;
    }

    public String getRolUsuario() {
        if (rolUsuario == null){
            for (UserRoles rol : UserRoles.values()) {
                if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole(rol.name()))
                    rolUsuario = rol.name();
            }
        }
        return rolUsuario;
    }
        
}
