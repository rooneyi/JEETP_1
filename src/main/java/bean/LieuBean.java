package bean;

import Business.LieuEntrepriseBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import Entities.Lieu;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;

@Named(value = "LieuBean")
@RequestScoped
public class LieuBean implements Serializable {

    private String nom;
    private String description;
    private double longitude;
    private double latitude;
    private List<Lieu> lieux = new ArrayList<>();
    private String weatherMessage;
    private Integer selectedLieu;

    @Inject
    private LieuEntrepriseBean lieuEntrepriseBean;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<Lieu> getLieux() {
        return lieuEntrepriseBean.listerTousLesLieux();
    }

    public List<Lieu> getListeLieux() {
        return getLieux();
    }

    public Integer getSelectedLieu() {
        return selectedLieu;
    }

    public void setSelectedLieu(Integer selectedLieu) {
        this.selectedLieu = selectedLieu;
    }

    public void ajouterLieu() {
        if (nom != null && !nom.isEmpty() && description != null && !description.isEmpty()) {
            lieuEntrepriseBean.ajouterLieuEntreprise(nom, description, latitude, longitude);
            // Reset fields
            nom = "";
            description = "";
            latitude = 0;
            longitude = 0;
        }
    }

    public void fetchWeatherMessage(Lieu l) {
        if (selectedLieu != null && l != null) {
            try {
                // Appel au service web pour obtenir les données météorologiques
                String serviceURL = "http://localhost:8080/j-Weater/webapi/JakartaWeather?latitude="
                        + l.getLatitude() + "&longitude=" + l.getLongitude();
                Client client = ClientBuilder.newClient();
                String response = client.target(serviceURL)
                        .request(MediaType.TEXT_PLAIN)
                        .get(String.class);
                // Enregistrement du message météo dans la variable weatherMessage
                this.weatherMessage = response;
            } catch (Exception e) {
                this.weatherMessage = "Impossible de récupérer la météo : " + e.getMessage();
            }
        }
    }

    public void updateWeatherMessage(AjaxBehaviorEvent event) {
        if (selectedLieu != null) {
            Lieu lieu = lieuEntrepriseBean.getLieuById(selectedLieu);
            this.fetchWeatherMessage(lieu);
        }
    }

    public String getWeatherMessage() {
        return weatherMessage;
    }

    public void setWeatherMessage(String weatherMessage) {
        this.weatherMessage = weatherMessage;
    }

    public void supprimerLieu(int id) {
        lieuEntrepriseBean.supprimerLieu(id);
    }

    public String preparerModification(Lieu lieu) {
        this.nom = lieu.getNom();
        this.description = lieu.getDescription();
        this.latitude = lieu.getLatitude();
        this.longitude = lieu.getLongitude();
        return null;
    }
}