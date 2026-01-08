/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business;

import Entities.Lieu;
import Entities.Utilisateur;
import Entities.Visite;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Enterprise Bean pour la gestion des visites
 * @author rooney
 */
@Stateless
public class VisiteEntrepriseBean {
    
    @PersistenceContext(unitName = "indonesiaPU")
    private EntityManager em;
    
    /**
     * Enregistre une nouvelle visite simple (sans détails)
     * @param utilisateurId ID de l'utilisateur
     * @param lieuId ID du lieu
     * @return La visite créée
     */
    public Visite enregistrerVisite(Long utilisateurId, int lieuId) {
        Utilisateur utilisateur = em.find(Utilisateur.class, utilisateurId);
        Lieu lieu = em.find(Lieu.class, lieuId);
        
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + utilisateurId);
        }
        if (lieu == null) {
            throw new IllegalArgumentException("Lieu non trouvé avec l'ID: " + lieuId);
        }
        
        Visite visite = new Visite(utilisateur, lieu);
        em.persist(visite);
        return visite;
    }
    
    /**
     * Enregistre une nouvelle visite complète avec tous les détails
     * @param utilisateurId ID de l'utilisateur
     * @param lieuId ID du lieu
     * @param dureeMinutes Durée de la visite en minutes
     * @param commentaire Commentaire de l'utilisateur
     * @param note Note de 1 à 5
     * @return La visite créée
     */
    public Visite enregistrerVisite(Long utilisateurId, int lieuId, Integer dureeMinutes, 
                                     String commentaire, Integer note) {
        Utilisateur utilisateur = em.find(Utilisateur.class, utilisateurId);
        Lieu lieu = em.find(Lieu.class, lieuId);
        
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + utilisateurId);
        }
        if (lieu == null) {
            throw new IllegalArgumentException("Lieu non trouvé avec l'ID: " + lieuId);
        }
        
        Visite visite = new Visite(utilisateur, lieu, dureeMinutes, commentaire, note);
        em.persist(visite);
        return visite;
    }
    
    /**
     * Récupère toutes les visites
     * @return Liste de toutes les visites
     */
    public List<Visite> obtenirToutesLesVisites() {
        TypedQuery<Visite> query = em.createNamedQuery("Visite.findAll", Visite.class);
        return query.getResultList();
    }
    
    /**
     * Récupère toutes les visites d'un utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @return Liste des visites de l'utilisateur
     */
    public List<Visite> obtenirVisitesParUtilisateur(Long utilisateurId) {
        TypedQuery<Visite> query = em.createNamedQuery("Visite.findByUtilisateur", Visite.class);
        query.setParameter("utilisateurId", utilisateurId);
        return query.getResultList();
    }
    
    /**
     * Récupère toutes les visites d'un lieu
     * @param lieuId ID du lieu
     * @return Liste des visites du lieu
     */
    public List<Visite> obtenirVisitesParLieu(int lieuId) {
        TypedQuery<Visite> query = em.createNamedQuery("Visite.findByLieu", Visite.class);
        query.setParameter("lieuId", lieuId);
        return query.getResultList();
    }
    
    /**
     * Trouve une visite par son ID
     * @param id ID de la visite
     * @return La visite trouvée ou null
     */
    public Visite trouverVisite(Long id) {
        return em.find(Visite.class, id);
    }
    
    /**
     * Met à jour une visite existante
     * @param visite La visite à mettre à jour
     * @return La visite mise à jour
     */
    public Visite mettreAJourVisite(Visite visite) {
        return em.merge(visite);
    }
    
    /**
     * Supprime une visite
     * @param id ID de la visite à supprimer
     */
    public void supprimerVisite(Long id) {
        Visite visite = em.find(Visite.class, id);
        if (visite != null) {
            em.remove(visite);
        }
    }
    
    /**
     * Compte le nombre de visites d'un utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @return Nombre de visites
     */
    public Long compterVisitesUtilisateur(Long utilisateurId) {
        TypedQuery<Long> query = em.createNamedQuery("Visite.countByUtilisateur", Long.class);
        query.setParameter("utilisateurId", utilisateurId);
        return query.getSingleResult();
    }
    
    /**
     * Compte le nombre de visites d'un lieu
     * @param lieuId ID du lieu
     * @return Nombre de visites
     */
    public Long compterVisitesLieu(int lieuId) {
        TypedQuery<Long> query = em.createNamedQuery("Visite.countByLieu", Long.class);
        query.setParameter("lieuId", lieuId);
        return query.getSingleResult();
    }
    
    /**
     * Récupère les N dernières visites d'un utilisateur
     * @param utilisateurId ID de l'utilisateur
     * @param limite Nombre maximum de visites à retourner
     * @return Liste des visites récentes
     */
    public List<Visite> obtenirVisitesRecentesUtilisateur(Long utilisateurId, int limite) {
        TypedQuery<Visite> query = em.createNamedQuery("Visite.findByUtilisateur", Visite.class);
        query.setParameter("utilisateurId", utilisateurId);
        query.setMaxResults(limite);
        return query.getResultList();
    }
    
    /**
     * Vérifie si un utilisateur a déjà visité un lieu
     * @param utilisateurId ID de l'utilisateur
     * @param lieuId ID du lieu
     * @return true si l'utilisateur a déjà visité ce lieu, false sinon
     */
    public boolean aDejaVisite(Long utilisateurId, int lieuId) {
        TypedQuery<Long> query = em.createNamedQuery("Visite.checkVisite", Long.class);
        query.setParameter("utilisateurId", utilisateurId);
        query.setParameter("lieuId", lieuId);
        Long count = query.getSingleResult();
        return count > 0;
    }
}
