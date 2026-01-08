package bean;

import Business.LieuEntrepriseBean;
import Business.UtilisateurEntrepriseBean;
import Business.VisiteEntrepriseBean;
import Entities.Lieu;
import Entities.Utilisateur;
import Entities.Visite;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Managed Bean pour la gestion des visites dans l'interface JSF
 * 
 * @author rooney
 */
@Named(value = "VisiteBean")
@RequestScoped
public class VisiteBean implements Serializable {

    @EJB
    private VisiteEntrepriseBean visiteEntrepriseBean;

    @EJB
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;

    @EJB
    private LieuEntrepriseBean lieuEntrepriseBean;

    private Visite visite;
    private List<Visite> visites;
    private Long utilisateurId;
    private Integer lieuId;
    private Integer dureeMinutes;
    private String commentaire;
    private Integer note;

    @PostConstruct
    public void init() {
        visite = new Visite();
        chargerToutesLesVisites();
    }

    /**
     * Retourne la liste de tous les utilisateurs pour le menu déroulant
     */
    public List<Utilisateur> getListeUtilisateurs() {
        return utilisateurEntrepriseBean.listerTousLesUtilisateurs();
    }

    /**
     * Retourne la liste de tous les lieux pour le menu déroulant
     */
    public List<Lieu> getListeLieux() {
        return lieuEntrepriseBean.listerTousLesLieux();
    }

    /**
     * Charge toutes les visites
     */
    public void chargerToutesLesVisites() {
        try {
            visites = visiteEntrepriseBean.obtenirToutesLesVisites();
        } catch (Exception e) {
            diagnosticException("Erreur de Chargement", e);
        }
    }

    /**
     * Enregistre une nouvelle visite simple
     */
    public String enregistrerVisiteSimple() {
        try {
            if (utilisateurId == null || lieuId == null) {
                ajouterMessage(FacesMessage.SEVERITY_WARN, "Attention",
                        "Veuillez sélectionner un utilisateur et un lieu");
                return null;
            }

            visiteEntrepriseBean.enregistrerVisite(utilisateurId, lieuId);
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Succès", "Visite enregistrée avec succès!");
            chargerToutesLesVisites();
            reinitialiser();
            return "visits?faces-redirect=true";
        } catch (Exception e) {
            diagnosticException("Erreur Enregistrement", e);
            return null;
        }
    }

    /**
     * Enregistre une nouvelle visite complète avec détails
     */
    public String enregistrerVisiteComplete() {
        try {
            if (utilisateurId == null || lieuId == null) {
                ajouterMessage(FacesMessage.SEVERITY_WARN, "Attention",
                        "Veuillez sélectionner un utilisateur et un lieu");
                return null;
            }

            visiteEntrepriseBean.enregistrerVisite(utilisateurId, lieuId, dureeMinutes, commentaire, note);
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Succès", "Visite enregistrée avec succès!");
            chargerToutesLesVisites();
            reinitialiser();
            return "visits?faces-redirect=true";
        } catch (Exception e) {
            diagnosticException("Erreur Technique", e);
            return null;
        }
    }

    /**
     * Déballe les exceptions EJB pour montrer la cause réelle
     */
    private void diagnosticException(String prefix, Exception e) {
        Throwable cause = e;
        while (cause.getCause() != null && cause != cause.getCause()) {
            cause = cause.getCause();
        }
        String msg = (cause.getMessage() != null) ? cause.getMessage() : cause.toString();
        ajouterMessage(FacesMessage.SEVERITY_ERROR, prefix, msg);
        e.printStackTrace();
    }

    /**
     * Charge les visites d'un utilisateur spécifique
     */
    public void chargerVisitesUtilisateur() {
        try {
            if (utilisateurId != null) {
                visites = visiteEntrepriseBean.obtenirVisitesParUtilisateur(utilisateurId);
            }
        } catch (Exception e) {
            diagnosticException("Erreur", e);
        }
    }

    /**
     * Charge les visites d'un lieu spécifique
     */
    public void chargerVisitesLieu() {
        try {
            if (lieuId != null) {
                visites = visiteEntrepriseBean.obtenirVisitesParLieu(lieuId);
            }
        } catch (Exception e) {
            diagnosticException("Erreur", e);
        }
    }

    /**
     * Supprime une visite
     */
    public void supprimerVisite(Long visiteId) {
        try {
            visiteEntrepriseBean.supprimerVisite(visiteId);
            ajouterMessage(FacesMessage.SEVERITY_INFO, "Succès", "Visite supprimée avec succès!");
            chargerToutesLesVisites();
        } catch (Exception e) {
            diagnosticException("Erreur de Suppression", e);
        }
    }

    /**
     * Compte les visites d'un utilisateur
     */
    public Long compterVisitesUtilisateur(Long userId) {
        try {
            return visiteEntrepriseBean.compterVisitesUtilisateur(userId);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Compte les visites d'un lieu
     */
    public Long compterVisitesLieu(Integer placeId) {
        try {
            return visiteEntrepriseBean.compterVisitesLieu(placeId);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Vérifie si un utilisateur a déjà visité un lieu
     */
    public boolean aDejaVisite(Long userId, Integer placeId) {
        try {
            return visiteEntrepriseBean.aDejaVisite(userId, placeId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Réinitialise le formulaire
     */
    private void reinitialiser() {
        visite = new Visite();
        utilisateurId = null;
        lieuId = null;
        dureeMinutes = null;
        commentaire = null;
        note = null;
    }

    /**
     * Ajoute un message FacesMessage
     */
    private void ajouterMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    // Getters et Setters
    public Visite getVisite() {
        return visite;
    }

    public void setVisite(Visite visite) {
        this.visite = visite;
    }

    public List<Visite> getVisites() {
        return visites;
    }

    public void setVisites(List<Visite> visites) {
        this.visites = visites;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Integer getLieuId() {
        return lieuId;
    }

    public void setLieuId(Integer lieuId) {
        this.lieuId = lieuId;
    }

    public Integer getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(Integer dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public List<Integer> getStars(int note) {
        List<Integer> stars = new java.util.ArrayList<>();
        if (note > 0) {
            for (int i = 0; i < note; i++)
                stars.add(i);
        }
        return stars;
    }

    public List<Integer> getEmptyStars(int note) {
        List<Integer> stars = new java.util.ArrayList<>();
        int empty = 5 - note;
        if (empty > 0) {
            for (int i = 0; i < empty; i++)
                stars.add(i);
        }
        return stars;
    }

    public String afficherFormulaireVisite() {
        return "/pages/visits?faces-redirect=true";
    }
}
