package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.gestores.EstudioFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.Estudio;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;
import ar.edu.utn.frsf.kinesio.gestores.util.Configuracion;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

@Named("adjuntarEstudiosController")
@ViewScoped
public class AdjuntarEstudiosController implements Serializable {

    public AdjuntarEstudiosController() {
    }

    @PostConstruct
    protected void init() {
        tratamiento = (Tratamiento) JsfUtil.getObjectFromRequestParameter(
                "tratamiento",
                FacesContext.getCurrentInstance().getApplication().createConverter(Tratamiento.class),
                null);

        estudiosTratamiento = tratamiento.getEstudioList();

        for (Estudio e : estudiosTratamiento) {
            e.getId();
        }

        if (estudiosTratamiento != null && !estudiosTratamiento.isEmpty()) {
            selected = estudiosTratamiento.get(0);
        }
    }

    private Tratamiento tratamiento;
    private Estudio selected;
    private List<Estudio> estudiosTratamiento;

    @EJB
    private EstudioFacade ejbFacade;

    @EJB
    private TratamientoFacade tratamientosFacade;

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Estudio getSelected() {
        return selected;
    }

    public void setSelected(Estudio estudio) {
        this.selected = estudio;
    }

    public String getSelectedImageRelativePath() {
        String estudiosFolder = Configuracion.getInstance().getEstudiosProperties().getProperty("deltagestion.estudios.folder");
        if (selected != null) {
            return estudiosFolder + "/" + this.selected.getNombrearchivo();
        } else {
            return estudiosFolder + "/img_placeholder.png";
        }
    }

    public List<Estudio> getEstudiosTratamiento() {
        return estudiosTratamiento;
    }

    public void setEstudiosTratamiento(List<Estudio> estudiosTratamiento) {
        this.estudiosTratamiento = estudiosTratamiento;
    }

    public Estudio getEstudio(java.lang.Integer id) {
        return getFacade().find(id);
    }

    private EstudioFacade getFacade() {
        return ejbFacade;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            //TODO: pasar está lógica al facade
            String baseDirectory = Configuracion.getInstance().getEstudiosProperties().getProperty("deltagestion.estudios.path")
                    + Configuracion.getInstance().getEstudiosProperties().getProperty("deltagestion.estudios.folder");

            //create an InputStream from the uploaded file
            InputStream inputStr = event.getFile().getInputstream();

            //create destination File
            File destFile = File.createTempFile(tratamiento.getPaciente().getApellido(), event.getFile().getFileName(), Paths.get(baseDirectory).toFile());

            Files.copy(inputStr, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Estudio e = new Estudio();
            e.setNombrearchivo(destFile.getName());
            e.setTratamiento(tratamiento);
            this.getFacade().create(e);
            
            tratamiento.addEstudio(e);
            tratamientosFacade.edit(tratamiento);

            JsfUtil.addSuccessMessage("Estudio subido con éxito");
            
        } catch (IOException ex) {
            Logger.getLogger(AdjuntarEstudiosController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Ocurrió un error al intentar guardar el estudio.");

        } catch (Exception ex) {
            Logger.getLogger(AdjuntarEstudiosController.class.getName()).log(Level.SEVERE, null, ex);
            JsfUtil.addErrorMessage("Ocurrió un error al intentar guardar el estudio.");
        }
    }
}
