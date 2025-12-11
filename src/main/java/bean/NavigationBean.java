/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;

/**
 *
 * @author rooney
 */
@Named(value="navigationController")
@RequestScoped
public class NavigationBean {
    public void home(){
        try{
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void voirLieu(){
        try{
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/lieu.xhtml");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void voirApropos(){
        try{
            FacesContext.getCurrentInstance().getExternalContext().redirect("pages/about.xhtml");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
