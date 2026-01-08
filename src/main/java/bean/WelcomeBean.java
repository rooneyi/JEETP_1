/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bean;

/**
 *
 * @author rooney
 */

import Business.SessionManager;
import Business.UtilisateurEntrepriseBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import Entities.Utilisateur;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@RequestScoped
@Named
public class WelcomeBean {
    @Inject
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;
    @Inject
    private SessionManager sessionManager;

    private String email;
    private String password;
    private String message;

    public WelcomeBean() {
    }

    public WelcomeBean(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String sAuthentifier() {
        Utilisateur utilisateur = utilisateurEntrepriseBean.authentifier(email, password);
        FacesContext context = FacesContext.getCurrentInstance();
        if (utilisateur != null) {

            sessionManager.createSession("user", email);
            return "home?faces-redirect=true";
        } else {
            this.message = "Email ou mot de passe incorrecte.! ";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            return null;
        }
    }

}
