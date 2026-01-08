/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package Business;

import jakarta.ejb.Stateless;
import jakarta.ejb.LocalBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import Entities.Lieu;
import java.util.List;

/**
 *
 * @author rooney
 */
@Stateless
@LocalBean
public class LieuEntrepriseBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void ajouterLieuEntreprise(String nom, String description, double latitude, double longitude) {
        Lieu lieu = new Lieu(nom, description, latitude, longitude);
        em.persist(lieu);
    }

    public List<Lieu> listerTousLesLieux() {
        return em.createQuery("SELECT L FROM Lieu L", Lieu.class).getResultList();
    }

    @Transactional
    public void supprimerLieu(int id) {
        Lieu lieu = em.find(Lieu.class, id);
        if (lieu != null) {
            em.remove(lieu);
        }
    }

    public Lieu trouverLieuParId(int id) {
        return em.find(Lieu.class, id);
    }

    public Lieu getLieuById(int id) {
        return trouverLieuParId(id);
    }
}
