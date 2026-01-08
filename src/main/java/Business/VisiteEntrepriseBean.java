/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Business;

import Entities.Visite;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * Enterprise Bean pour la gestion des visites
 * 
 * @author rooney
 */
@Stateless
public class VisiteEntrepriseBean {

    @PersistenceContext(unitName = "indonesiaPU")
    private EntityManager em;

    @jakarta.inject.Inject
    private VisiteRestClient restClient;

    /**
     * Enregistre une nouvelle visite simple (sans détails)
     * 
     * @param utilisateurId ID de l'utilisateur
     * @param lieuId        ID du lieu
     * @return La visite créée
     */
    public Visite enregistrerVisite(Long utilisateurId, int lieuId) {
        VisiteRestClient.ApiVisite apiVisite = new VisiteRestClient.ApiVisite();
        apiVisite.setUtilisateurId(utilisateurId);
        apiVisite.setLieuId((long) lieuId);

        VisiteRestClient.ApiVisite result = restClient.create(apiVisite);
        return mapToEntity(result);
    }

    /**
     * Enregistre une nouvelle visite complète avec tous les détails
     * 
     * @param utilisateurId ID de l'utilisateur
     * @param lieuId        ID du lieu
     * @param dureeMinutes  Durée de la visite en minutes
     * @param commentaire   Commentaire de l'utilisateur
     * @param note          Note de 1 à 5
     * @return La visite créée
     */
    public Visite enregistrerVisite(Long utilisateurId, int lieuId, Integer dureeMinutes,
            String commentaire, Integer note) {
        VisiteRestClient.ApiVisite apiVisite = new VisiteRestClient.ApiVisite();
        apiVisite.setUtilisateurId(utilisateurId);
        apiVisite.setLieuId((long) lieuId);
        apiVisite.setDureeMinutes(dureeMinutes);
        apiVisite.setCommentaire(commentaire);
        apiVisite.setNote(note);

        VisiteRestClient.ApiVisite result = restClient.create(apiVisite);
        return mapToEntity(result);
    }

    /**
     * Récupère toutes les visites
     * 
     * @return Liste de toutes les visites
     */
    public List<Visite> obtenirToutesLesVisites() {
        List<VisiteRestClient.ApiVisite> apiVisites = restClient.findAll();
        return apiVisites.stream().map(this::mapToEntity).collect(java.util.stream.Collectors.toList());
    }

    /**
     * Récupère toutes les visites d'un utilisateur
     * 
     * @param utilisateurId ID de l'utilisateur
     * @return Liste des visites de l'utilisateur
     */
    public List<Visite> obtenirVisitesParUtilisateur(Long utilisateurId) {
        List<VisiteRestClient.ApiVisite> apiVisites = restClient.findByUtilisateur(utilisateurId);
        return apiVisites.stream().map(this::mapToEntity).collect(java.util.stream.Collectors.toList());
    }

    /**
     * Récupère toutes les visites d'un lieu
     * 
     * @param lieuId ID du lieu
     * @return Liste des visites du lieu
     */
    public List<Visite> obtenirVisitesParLieu(int lieuId) {
        List<VisiteRestClient.ApiVisite> apiVisites = restClient.findByLieu(lieuId);
        return apiVisites.stream().map(this::mapToEntity).collect(java.util.stream.Collectors.toList());
    }

    /**
     * Trouve une visite par son ID
     * 
     * @param id ID de la visite
     * @return La visite trouvée ou null
     */
    public Visite trouverVisite(Long id) {
        VisiteRestClient.ApiVisite apiVisite = restClient.findById(id);
        return mapToEntity(apiVisite);
    }

    /**
     * Met à jour une visite existante
     * 
     * @param visite La visite à mettre à jour
     * @return La visite mise à jour
     */
    public Visite mettreAJourVisite(Visite visite) {
        VisiteRestClient.ApiVisite apiVisite = mapToApi(visite);
        VisiteRestClient.ApiVisite result = restClient.update(visite.getId(), apiVisite);
        return mapToEntity(result);
    }

    /**
     * Supprime une visite
     * 
     * @param id ID de la visite à supprimer
     */
    public void supprimerVisite(Long id) {
        restClient.delete(id);
    }

    /**
     * Compte le nombre de visites d'un utilisateur
     * 
     * @param utilisateurId ID de l'utilisateur
     * @return Nombre de visites
     */
    public Long compterVisitesUtilisateur(Long utilisateurId) {
        return restClient.countByUtilisateur(utilisateurId);
    }

    /**
     * Compte le nombre de visites d'un lieu
     * 
     * @param lieuId ID du lieu
     * @return Nombre de visites
     */
    public Long compterVisitesLieu(int lieuId) {
        return restClient.countByLieu(lieuId);
    }

    /**
     * Récupère les N dernières visites d'un utilisateur
     * 
     * @param utilisateurId ID de l'utilisateur
     * @param limite        Nombre maximum de visites à retourner
     * @return Liste des visites récentes
     */
    public List<Visite> obtenirVisitesRecentesUtilisateur(Long utilisateurId, int limite) {
        // L'API ne supporte pas directement la limite, on filtre localement pour
        // l'instant
        List<VisiteRestClient.ApiVisite> apiVisites = restClient.findByUtilisateur(utilisateurId);
        return apiVisites.stream()
                .limit(limite)
                .map(this::mapToEntity)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Vérifie si un utilisateur a déjà visité un lieu
     * 
     * @param utilisateurId ID de l'utilisateur
     * @param lieuId        ID du lieu
     * @return true si l'utilisateur a déjà visité ce lieu, false sinon
     */
    public boolean aDejaVisite(Long utilisateurId, int lieuId) {
        return restClient.checkVisite(utilisateurId, lieuId);
    }

    /**
     * Mappage de l'objet API vers l'entité locale
     */
    private Visite mapToEntity(VisiteRestClient.ApiVisite apiVisite) {
        if (apiVisite == null)
            return null;

        Visite visite = new Visite();
        visite.setId(apiVisite.getId());
        visite.setDureeMinutes(apiVisite.getDureeMinutes());
        visite.setCommentaire(apiVisite.getCommentaire());
        visite.setNote(apiVisite.getNote());

        if (apiVisite.getDateVisite() != null) {
            try {
                visite.setDateVisite(java.time.LocalDateTime.parse(apiVisite.getDateVisite()));
            } catch (Exception e) {
                // Log and use default
            }
        }

        // Récupération des entités liées localement pour conserver les relations
        if (apiVisite.getUtilisateurId() != null) {
            visite.setUtilisateur(em.find(Entities.Utilisateur.class, apiVisite.getUtilisateurId()));
        }
        if (apiVisite.getLieuId() != null) {
            visite.setLieu(em.find(Entities.Lieu.class, apiVisite.getLieuId().intValue()));
        }

        return visite;
    }

    /**
     * Mappage de l'entité locale vers l'objet API
     */
    private VisiteRestClient.ApiVisite mapToApi(Visite visite) {
        if (visite == null)
            return null;

        VisiteRestClient.ApiVisite apiVisite = new VisiteRestClient.ApiVisite();
        apiVisite.setId(visite.getId());
        apiVisite.setUtilisateurId(visite.getUtilisateur() != null ? visite.getUtilisateur().getId() : null);
        apiVisite.setLieuId(visite.getLieu() != null ? (long) visite.getLieu().getId() : null);
        apiVisite.setDureeMinutes(visite.getDureeMinutes());
        apiVisite.setCommentaire(visite.getCommentaire());
        apiVisite.setNote(visite.getNote());
        if (visite.getDateVisite() != null) {
            apiVisite.setDateVisite(visite.getDateVisite().toString());
        }

        return apiVisite;
    }
}
