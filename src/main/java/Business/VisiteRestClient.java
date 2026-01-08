package Business;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

/**
 * Client REST pour consommer l'API j-Weater
 */
@RequestScoped
public class VisiteRestClient implements Serializable {

    private static final String BASE_URL = "http://localhost:8080/j-Weater/webapi/visites";
    private final Client client;

    public VisiteRestClient() {
        this.client = ClientBuilder.newClient();
    }

    public List<ApiVisite> findAll() {
        WebTarget target = client.target(BASE_URL);
        return target.request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<ApiVisite>>() {
                });
    }

    public ApiVisite findById(Long id) {
        WebTarget target = client.target(BASE_URL).path(String.valueOf(id));
        return target.request(MediaType.APPLICATION_JSON)
                .get(ApiVisite.class);
    }

    public List<ApiVisite> findByUtilisateur(Long utilisateurId) {
        WebTarget target = client.target(BASE_URL).path("utilisateur").path(String.valueOf(utilisateurId));
        return target.request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<ApiVisite>>() {
                });
    }

    public List<ApiVisite> findByLieu(int lieuId) {
        WebTarget target = client.target(BASE_URL).path("lieu").path(String.valueOf(lieuId));
        return target.request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<ApiVisite>>() {
                });
    }

    public ApiVisite create(ApiVisite visite) {
        WebTarget target = client.target(BASE_URL);
        Response response = target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(visite, MediaType.APPLICATION_JSON));

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            String error = response.readEntity(String.class);
            throw new RuntimeException("Erreur API (" + response.getStatus() + "): " + error);
        }

        return response.readEntity(ApiVisite.class);
    }

    public void delete(Long id) {
        WebTarget target = client.target(BASE_URL).path(String.valueOf(id));
        target.request().delete();
    }

    public ApiVisite update(Long id, ApiVisite visite) {
        WebTarget target = client.target(BASE_URL).path(String.valueOf(id));
        Response response = target.request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(visite, MediaType.APPLICATION_JSON));

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            String error = response.readEntity(String.class);
            throw new RuntimeException("Erreur API (" + response.getStatus() + "): " + error);
        }

        return response.readEntity(ApiVisite.class);
    }

    public Long countByUtilisateur(Long utilisateurId) {
        WebTarget target = client.target(BASE_URL).path("utilisateur").path(String.valueOf(utilisateurId))
                .path("count");
        return Long.valueOf(target.request(MediaType.TEXT_PLAIN).get(String.class));
    }

    public Long countByLieu(int lieuId) {
        WebTarget target = client.target(BASE_URL).path("lieu").path(String.valueOf(lieuId)).path("count");
        return Long.valueOf(target.request(MediaType.TEXT_PLAIN).get(String.class));
    }

    public boolean checkVisite(Long utilisateurId, int lieuId) {
        WebTarget target = client.target(BASE_URL).path("check")
                .queryParam("utilisateurId", utilisateurId)
                .queryParam("lieuId", lieuId);
        return Boolean.parseBoolean(target.request(MediaType.TEXT_PLAIN).get(String.class));
    }

    /**
     * DTO matching the API response/request structure
     */
    public static class ApiVisite implements Serializable {
        private Long id;
        private Long utilisateurId;
        private Long lieuId;
        private String dateVisite; // ISO string or LocalDateTime mapping
        private Integer dureeMinutes;
        private String commentaire;
        private Integer note;

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getUtilisateurId() {
            return utilisateurId;
        }

        public void setUtilisateurId(Long utilisateurId) {
            this.utilisateurId = utilisateurId;
        }

        public Long getLieuId() {
            return lieuId;
        }

        public void setLieuId(Long lieuId) {
            this.lieuId = lieuId;
        }

        public String getDateVisite() {
            return dateVisite;
        }

        public void setDateVisite(String dateVisite) {
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
    }
}
