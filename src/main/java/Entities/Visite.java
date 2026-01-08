/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité JPA représentant une visite d'un utilisateur dans un lieu
 * @author rooney
 */
@Entity
@Table(name = "visite")
@NamedQueries({
    @NamedQuery(name = "Visite.findAll", query = "SELECT v FROM Visite v"),
    @NamedQuery(name = "Visite.findByUtilisateur", 
                query = "SELECT v FROM Visite v WHERE v.utilisateur.id = :utilisateurId ORDER BY v.dateVisite DESC"),
    @NamedQuery(name = "Visite.findByLieu", 
                query = "SELECT v FROM Visite v WHERE v.lieu.id = :lieuId ORDER BY v.dateVisite DESC"),
    @NamedQuery(name = "Visite.countByUtilisateur", 
                query = "SELECT COUNT(v) FROM Visite v WHERE v.utilisateur.id = :utilisateurId"),
    @NamedQuery(name = "Visite.countByLieu", 
                query = "SELECT COUNT(v) FROM Visite v WHERE v.lieu.id = :lieuId"),
    @NamedQuery(name = "Visite.checkVisite", 
                query = "SELECT COUNT(v) FROM Visite v WHERE v.utilisateur.id = :utilisateurId AND v.lieu.id = :lieuId")
})
public class Visite implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lieu_id", nullable = false)
    private Lieu lieu;
    
    @Column(name = "date_visite", nullable = false)
    private LocalDateTime dateVisite;
    
    @Column(name = "duree_minutes")
    private Integer dureeMinutes;
    
    @Column(length = 500)
    @Size(max = 500, message = "Le commentaire ne peut pas dépasser 500 caractères")
    private String commentaire;
    
    @Column
    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    private Integer note;
    
    // Constructeurs
    public Visite() {
        this.dateVisite = LocalDateTime.now();
    }
    
    public Visite(Utilisateur utilisateur, Lieu lieu) {
        this();
        this.utilisateur = utilisateur;
        this.lieu = lieu;
    }
    
    public Visite(Utilisateur utilisateur, Lieu lieu, Integer dureeMinutes, String commentaire, Integer note) {
        this(utilisateur, lieu);
        this.dureeMinutes = dureeMinutes;
        this.commentaire = commentaire;
        this.note = note;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public Lieu getLieu() {
        return lieu;
    }
    
    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }
    
    public LocalDateTime getDateVisite() {
        return dateVisite;
    }
    
    public void setDateVisite(LocalDateTime dateVisite) {
        this.dateVisite = dateVisite;
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
    
    // Méthodes utilitaires
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Visite)) {
            return false;
        }
        Visite other = (Visite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Visite{" + 
               "id=" + id + 
               ", utilisateur=" + (utilisateur != null ? utilisateur.getUsername() : "null") +
               ", lieu=" + (lieu != null ? lieu.getNom() : "null") +
               ", dateVisite=" + dateVisite +
               ", note=" + note +
               '}';
    }
}
