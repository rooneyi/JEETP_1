import Business.LieuEntrepriseBean;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import Entities.Lieu;
import jakarta.inject.Named;

@Named(value ="LieuBean")
@RequestScoped
public class LieuBean implements Serializable{

    private String nom;
    private String description;
    private double longitude;
    private double latitude;
    private List<Lieu> lieux = new ArrayList<>();

    @Inject
    private LieuEntrepriseBean lieuEntrepriseBean;

    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public List<Lieu> getLieux() { return lieuEntrepriseBean.listerTousLesLieux(); }

    public void ajouterLieu() {
        if (nom != null && !nom.isEmpty() && description != null && !description.isEmpty()) {
            lieuEntrepriseBean.ajouterLieuEntreprise(nom, description, latitude, longitude);
        }
    }
}