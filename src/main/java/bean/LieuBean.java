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

@Named
@RequestScoped
public class LieuBean {

    private String nom;
    private String description;
    private double latitude;
    private double longitude;

    public void ajouterLieu() {
        System.out.println("Nouveau lieu ajouté:");
        System.out.println("Nom : " + nom);
        System.out.println("Description : " + description);
        System.out.println("Latitude : " + latitude);
        System.out.println("Longitude : " + longitude);

        // TODO : enregistrer en base de données
    }

    // Getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
