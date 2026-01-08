package Business;

import Entities.Utilisateur;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UtilisateurEntrepriseBean {

    @PersistenceContext
    private EntityManager em;

    // ==========================
    // CRÉATION UTILISATEUR
    // ==========================
    @Transactional
    public void ajouterUtilisateurEntreprise(
            String username,
            String email,
            String password,
            String description
    ) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Utilisateur utilisateur = new Utilisateur(username, email, hashedPassword, description);
        em.persist(utilisateur);
    }

    // ==========================
    // LECTURE
    // ==========================
    public List<Utilisateur> listerTousLesUtilisateurs() {
        return em.createQuery(
                "SELECT u FROM Utilisateur u",
                Utilisateur.class
        ).getResultList();
    }

    public Utilisateur trouverUtilisateurParId(Long id) {
        return em.find(Utilisateur.class, id);
    }

    public Utilisateur trouverUtilisateurParEmail(String email) {
        try {
            return em.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.email = :email",
                    Utilisateur.class
            ).setParameter("email", email)
             .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Utilisateur trouverUtilisateurParUsername(String username) {
        try {
            return em.createQuery(
                    "SELECT u FROM Utilisateur u WHERE u.username = :username",
                    Utilisateur.class
            ).setParameter("username", username)
             .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // ==========================
    // SUPPRESSION
    // ==========================
    @Transactional
    public void supprimerUtilisateur(Long id) {
        Utilisateur utilisateur = em.find(Utilisateur.class, id);
        if (utilisateur != null) {
            em.remove(utilisateur);
        }
    }

    // ==========================
    // VÉRIFICATIONS
    // ==========================
    public boolean emailExiste(String email) {
        Long count = em.createQuery(
                "SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :email",
                Long.class
        ).setParameter("email", email)
         .getSingleResult();

        return count > 0;
    }

    public boolean usernameExiste(String username) {
        Long count = em.createQuery(
                "SELECT COUNT(u) FROM Utilisateur u WHERE u.username = :username",
                Long.class
        ).setParameter("username", username)
         .getSingleResult();

        return count > 0;
    }

    // ==========================
    // SÉCURITÉ MOT DE PASSE
    // ==========================
    public boolean verifierMotDePasse(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    /**
     * Vérifie l'ancien mot de passe à partir du username
     */
//    public boolean verifierMotDePasse(String username, String oldPassword) {
//        Utilisateur utilisateur = trouverUtilisateurParUsername(username);
//        if (utilisateur == null) {
//            return false;
//        }
//        return BCrypt.checkpw(oldPassword, utilisateur.getPassword());
//    }

    /**
     * Modification sécurisée du mot de passe
     */
    @Transactional
    public void modifierMotDePasse(String username, String newPassword) {
        Utilisateur utilisateur = trouverUtilisateurParUsername(username);
        if (utilisateur != null) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            utilisateur.setPassword(hashedPassword);
            em.merge(utilisateur);
        }
    }

    // ==========================
    // AUTHENTIFICATION
    // ==========================
    public Utilisateur authentifier(String email, String password) {
        Utilisateur utilisateur = this.trouverUtilisateurParEmail(email);
        if (utilisateur != null
                && BCrypt.checkpw(password, utilisateur.getPassword())) {
            return utilisateur;
        }
        return null;
    }
}
