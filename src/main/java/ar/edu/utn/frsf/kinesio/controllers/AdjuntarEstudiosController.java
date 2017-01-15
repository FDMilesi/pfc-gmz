package ar.edu.utn.frsf.kinesio.controllers;

import ar.edu.utn.frsf.kinesio.entities.Tratamiento;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.UploadedFile;

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
        getListaOrdenesReporte();
    }
    
    //Metodos de negocio
    public void getListaOrdenesReporte() {
        tratamiento = (Tratamiento) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("tratamientoParaEstudios");
        estudiosTratamiento = new ArrayList<String>();
        estudiosTratamiento.add("biceps.jpg");
        estudiosTratamiento.add("dorsal.jpg");
    }
    
    private Tratamiento tratamiento;
    private UploadedFile uploadFile;
    private String selectedFile;
    private List<String> estudiosTratamiento;
   
    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
    }

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

    public List<String> getEstudiosTratamiento() {
        return estudiosTratamiento;
    }

    public void setEstudiosTratamiento(List<String> estudiosTratamiento) {
        this.estudiosTratamiento = estudiosTratamiento;
    }

    public void handleFileUpload(){
        try {
            
            String baseDirectory = "C:\\deltaGestion\\estudios";
            String childDirectory = getDirectoryHashed();
            String fileName = uploadFile.getFileName() + (new Date()).getTime();


            //create an InputStream from the uploaded file
            InputStream inputStr = uploadFile.getInputstream();
            
            //create destination File
            String destPath = baseDirectory + "\\" + childDirectory + "\\" + fileName;
            File destFile = new File(destPath);

            //use org.apache.commons.io.FileUtils to copy the File                  
            FileUtils.copyInputStreamToFile(inputStr, destFile);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdjuntarEstudiosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdjuntarEstudiosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String getDirectoryHashed() throws NoSuchAlgorithmException{
        
        String stringToEncrypt = tratamiento.getPaciente().getApellido()
                + tratamiento.getPaciente().getNombre()
                + tratamiento.getPaciente().getDni();
        
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(stringToEncrypt.getBytes());
        String encryptedString = new String(messageDigest.digest());
        
        return encryptedString;
        
    }
    
}
