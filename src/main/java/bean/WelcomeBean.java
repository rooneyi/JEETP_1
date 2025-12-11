/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bean;

/**
 *
 * @author rooney
 */



import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@RequestScoped
@Named
public class WelcomeBean {
    private String nom;
    private String message;
    private String messageMontant;
    private Double montant;
    

    private static final double TauxUSDToIDR = 16670.00; 

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getMessage() {
        return message;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void afficher() {
        this.message = "Welcome to Indonesia, dear " + this.nom;
    }

    public String getMessageMontant() {
        return messageMontant;
    }

    
    public void usdToIdr() {
        if (montant != null) {
            montant = montant * TauxUSDToIDR;
            messageMontant = "Montant en IDR : " + montant;
        }
    }

    // Conversion IDR -> USD
    public void idrToUsd() {
        if (montant != null) {
            montant = montant / TauxUSDToIDR;
            messageMontant = "Montant en USD : " + montant;
        }
    }
}
