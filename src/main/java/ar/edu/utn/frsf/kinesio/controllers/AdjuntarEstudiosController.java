package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.gestores.EstudioFacade;
import ar.edu.utn.frsf.kinesio.controllers.util.JsfUtil;
import ar.edu.utn.frsf.kinesio.entities.Estudio;
import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import ar.edu.utn.frsf.kinesio.gestores.TratamientoFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 */
@Named("adjuntarEstudiosController")
@ViewScoped
public class AdjuntarEstudiosController implements Serializable {

    public AdjuntarEstudiosController() {

    }

    @PostConstruct
    protected void init(){
        getListaEstudios();
    }
    
    //Metodos de negocio
    public void getListaEstudios() {
        tratamiento = (Tratamiento) JsfUtil.getObjectFromRequestParameter(
                "tratamiento",
                FacesContext.getCurrentInstance().getApplication().createConverter(Tratamiento.class),
                null);
        estudiosTratamiento = tratamiento.getEstudioList();
        if(estudiosTratamiento!=null && !estudiosTratamiento.isEmpty())
            selectedFile=estudiosTratamiento.get(0);
    }
    
    private Tratamiento tratamiento;
    private Estudio selectedFile;
    private List<Estudio> estudiosTratamiento;
    @EJB
    private EstudioFacade ejbFacade;
    @EJB
    private TratamientoFacade tratFacade;
   
    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public Estudio getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(Estudio selectedFile) {
        this.selectedFile = selectedFile;
    }
    
    public List<Estudio> getEstudiosTratamiento() {
        return estudiosTratamiento;
    }

    public void setEstudiosTratamiento(List<Estudio> estudiosTratamiento) {
        this.estudiosTratamiento = estudiosTratamiento;
    }
    
    private EstudioFacade getFacade() {
        return ejbFacade;
    }

    public void handleFileUpload(FileUploadEvent event){
          try {

            String baseDirectory = "C:/deltaGestion/estudios/"+getDirectoryHashed();

            File directory = new File(baseDirectory);
            File image = new File(baseDirectory+"/"+event.getFile().getFileName());
            
            if(image.exists()){
                //Ver como manejar para avisar al usuario
                String msg = "El archivo con el nombre especificado ya existe y no será creado.";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
            }
            else{
                if(!directory.exists())
                    directory.mkdirs();
                
                //create an InputStream from the uploaded file
                InputStream inputStr = event.getFile().getInputstream();

                // write the inputStream to a FileOutputStream
                OutputStream outputStream = new FileOutputStream(image);

                int read;
                byte[] bytes = new byte[1024];

                while ((read = inputStr.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                
                Estudio e = new Estudio();
                e.setNombrearchivo(event.getFile().getFileName());
                e.setTratamientoid(tratamiento);
                tratamiento.addEstudio(e);
                tratFacade.editAndReturn(tratamiento);
            }
           
        } catch (IOException ex) {
            Logger.getLogger(AdjuntarEstudiosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdjuntarEstudiosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getDirectoryHashed() throws NoSuchAlgorithmException{
        
        String stringToEncrypt = tratamiento.getPaciente().getId()
                + tratamiento.getPaciente().getDni()
                + tratamiento.getId();
              
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(stringToEncrypt.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        String hashtext = number.toString(16);
        
        return hashtext;
        
    }
    
}
