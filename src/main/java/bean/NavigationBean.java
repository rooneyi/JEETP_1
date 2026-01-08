/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rooney
 */
@Named("navigationController")
@RequestScoped
public class NavigationBean {

    public void voirApropos() {
        this.redirection("pages/about.xhtml");
    }

    public void home() {
        this.redirection("../home.xhtml");
    }

    public void redirection(String destination) {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect(destination);
        } catch (IOException ex) {
            Logger.getLogger(NavigationBean.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    // navigation vers lieu
    public void ajouterLieu() {
        try {
            FacesContext.getCurrentInstance().getExternalContext()
                    .redirect("pages/lieu.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(NavigationBean.class.getName())
                    .log(Level.SEVERE, "Erreur lors de la navigation", ex);
        }

    }

    public void voirLieu() {
        this.redirection("pages/lieu.xhtml");
    }

    public void voirVisite() {
        this.redirection("pages/visits.xhtml");
    }
}