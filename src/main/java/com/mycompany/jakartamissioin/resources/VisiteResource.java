package com.mycompany.jakartamissioin.resources;

import Business.VisiteEntrepriseBean;
import Entities.Visite;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API REST pour la gestion des visites
 * Base URL: /resources/api/visites
 * @author rooney
 */
@Path("api/visites")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisiteResource {
    
    @EJB
    private VisiteEntrepriseBean visiteEntrepriseBean;
    
    /**
     * GET /api/visites - Obtenir toutes les visites
     */
    @GET
    public Response obtenirToutesLesVisites() {
        try {
            List<Visite> visites = visiteEntrepriseBean.obtenirToutesLesVisites();
            return Response.ok(visites).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la récupération des visites: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * GET /api/visites/{id} - Obtenir une visite par ID
     */
    @GET
    @Path("{id}")
    public Response obtenirVisiteParId(@PathParam("id") Long id) {
        try {
            Visite visite = visiteEntrepriseBean.trouverVisite(id);
            if (visite == null) {
                return Response.status(Response.Status.NOT_FOUND)
                              .entity(creerMessageErreur("Visite non trouvée avec l'ID: " + id))
                              .build();
            }
            return Response.ok(visite).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la récupération de la visite: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * GET /api/visites/utilisateur/{utilisateurId} - Obtenir les visites d'un utilisateur
     */
    @GET
    @Path("utilisateur/{utilisateurId}")
    public Response obtenirVisitesParUtilisateur(@PathParam("utilisateurId") Long utilisateurId) {
        try {
            List<Visite> visites = visiteEntrepriseBean.obtenirVisitesParUtilisateur(utilisateurId);
            return Response.ok(visites).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la récupération des visites: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * GET /api/visites/lieu/{lieuId} - Obtenir les visites d'un lieu
     */
    @GET
    @Path("lieu/{lieuId}")
    public Response obtenirVisitesParLieu(@PathParam("lieuId") Integer lieuId) {
        try {
            List<Visite> visites = visiteEntrepriseBean.obtenirVisitesParLieu(lieuId);
            return Response.ok(visites).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la récupération des visites: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * GET /api/visites/utilisateur/{utilisateurId}/count - Compter les visites d'un utilisateur
     */
    @GET
    @Path("utilisateur/{utilisateurId}/count")
    public Response compterVisitesUtilisateur(@PathParam("utilisateurId") Long utilisateurId) {
        try {
            Long count = visiteEntrepriseBean.compterVisitesUtilisateur(utilisateurId);
            Map<String, Object> response = new HashMap<>();
            response.put("utilisateurId", utilisateurId);
            response.put("nombreVisites", count);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors du comptage: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * GET /api/visites/lieu/{lieuId}/count - Compter les visites d'un lieu
     */
    @GET
    @Path("lieu/{lieuId}/count")
    public Response compterVisitesLieu(@PathParam("lieuId") Integer lieuId) {
        try {
            Long count = visiteEntrepriseBean.compterVisitesLieu(lieuId);
            Map<String, Object> response = new HashMap<>();
            response.put("lieuId", lieuId);
            response.put("nombreVisites", count);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors du comptage: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * GET /api/visites/check?utilisateurId={id}&lieuId={id} - Vérifier si un utilisateur a déjà visité un lieu
     */
    @GET
    @Path("check")
    public Response verifierVisite(@QueryParam("utilisateurId") Long utilisateurId,
                                   @QueryParam("lieuId") Integer lieuId) {
        try {
            if (utilisateurId == null || lieuId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                              .entity(creerMessageErreur("Les paramètres utilisateurId et lieuId sont requis"))
                              .build();
            }
            
            boolean dejaVisite = visiteEntrepriseBean.aDejaVisite(utilisateurId, lieuId);
            Map<String, Object> response = new HashMap<>();
            response.put("utilisateurId", utilisateurId);
            response.put("lieuId", lieuId);
            response.put("dejaVisite", dejaVisite);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la vérification: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * POST /api/visites - Créer une nouvelle visite complète
     * Body JSON: {"utilisateurId": 1, "lieuId": 5, "dureeMinutes": 120, "commentaire": "...", "note": 5}
     */
    @POST
    public Response creerVisiteComplete(VisiteDTO visiteDTO) {
        try {
            if (visiteDTO.getUtilisateurId() == null || visiteDTO.getLieuId() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                              .entity(creerMessageErreur("Les champs utilisateurId et lieuId sont requis"))
                              .build();
            }
            
            Visite visite = visiteEntrepriseBean.enregistrerVisite(
                visiteDTO.getUtilisateurId(),
                visiteDTO.getLieuId(),
                visiteDTO.getDureeMinutes(),
                visiteDTO.getCommentaire(),
                visiteDTO.getNote()
            );
            
            return Response.status(Response.Status.CREATED).entity(visite).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                          .entity(creerMessageErreur(e.getMessage()))
                          .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la création de la visite: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * POST /api/visites/simple?utilisateurId={id}&lieuId={id} - Créer une visite simple
     */
    @POST
    @Path("simple")
    public Response creerVisiteSimple(@QueryParam("utilisateurId") Long utilisateurId,
                                      @QueryParam("lieuId") Integer lieuId) {
        try {
            if (utilisateurId == null || lieuId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                              .entity(creerMessageErreur("Les paramètres utilisateurId et lieuId sont requis"))
                              .build();
            }
            
            Visite visite = visiteEntrepriseBean.enregistrerVisite(utilisateurId, lieuId);
            return Response.status(Response.Status.CREATED).entity(visite).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                          .entity(creerMessageErreur(e.getMessage()))
                          .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la création de la visite: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * PUT /api/visites/{id} - Mettre à jour une visite
     */
    @PUT
    @Path("{id}")
    public Response mettreAJourVisite(@PathParam("id") Long id, VisiteDTO visiteDTO) {
        try {
            Visite visite = visiteEntrepriseBean.trouverVisite(id);
            if (visite == null) {
                return Response.status(Response.Status.NOT_FOUND)
                              .entity(creerMessageErreur("Visite non trouvée avec l'ID: " + id))
                              .build();
            }
            
            // Mise à jour des champs
            if (visiteDTO.getDureeMinutes() != null) {
                visite.setDureeMinutes(visiteDTO.getDureeMinutes());
            }
            if (visiteDTO.getCommentaire() != null) {
                visite.setCommentaire(visiteDTO.getCommentaire());
            }
            if (visiteDTO.getNote() != null) {
                visite.setNote(visiteDTO.getNote());
            }
            
            Visite visiteMiseAJour = visiteEntrepriseBean.mettreAJourVisite(visite);
            return Response.ok(visiteMiseAJour).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la mise à jour: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * DELETE /api/visites/{id} - Supprimer une visite
     */
    @DELETE
    @Path("{id}")
    public Response supprimerVisite(@PathParam("id") Long id) {
        try {
            Visite visite = visiteEntrepriseBean.trouverVisite(id);
            if (visite == null) {
                return Response.status(Response.Status.NOT_FOUND)
                              .entity(creerMessageErreur("Visite non trouvée avec l'ID: " + id))
                              .build();
            }
            
            visiteEntrepriseBean.supprimerVisite(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Visite supprimée avec succès");
            response.put("id", id.toString());
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                          .entity(creerMessageErreur("Erreur lors de la suppression: " + e.getMessage()))
                          .build();
        }
    }
    
    /**
     * Crée un message d'erreur au format JSON
     */
    private Map<String, String> creerMessageErreur(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
    
    /**
     * DTO pour la création/mise à jour de visites
     */
    public static class VisiteDTO {
        private Long utilisateurId;
        private Integer lieuId;
        private Integer dureeMinutes;
        private String commentaire;
        private Integer note;
        
        // Getters et Setters
        public Long getUtilisateurId() { return utilisateurId; }
        public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }
        
        public Integer getLieuId() { return lieuId; }
        public void setLieuId(Integer lieuId) { this.lieuId = lieuId; }
        
        public Integer getDureeMinutes() { return dureeMinutes; }
        public void setDureeMinutes(Integer dureeMinutes) { this.dureeMinutes = dureeMinutes; }
        
        public String getCommentaire() { return commentaire; }
        public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
        
        public Integer getNote() { return note; }
        public void setNote(Integer note) { this.note = note; }
    }
}
