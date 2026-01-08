package bean;

import Business.UtilisateurEntrepriseBean;
import Entities.Utilisateur;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import Business.SessionManager;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Named("utilisateurBean")
@RequestScoped
public class Utilisateurbean {

    @Inject
    private UtilisateurEntrepriseBean utilisateurEntrepriseBean;

    @Inject
    private SessionManager sessionManager;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit avoir entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Le mot de passe doit contenir au moins une majuscule, un chiffre et un caractère spécial")
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotBlank(message = "Veuillez confirmer votre mot de passe")
    private String confirmPassword;

    private String description;

    // ===== NOUVEAUX CHAMPS =====
    private String oldPassword;
    private String newPassword;

    @PostConstruct
    public void init() {
        String sessionEmail = sessionManager.getValueFromSession("user");
        if (sessionEmail != null) {
            Utilisateur user = utilisateurEntrepriseBean.trouverUtilisateurParEmail(sessionEmail);
            if (user != null) {
                this.username = user.getUsername();
                this.email = user.getEmail();
                this.description = user.getDescription();
            }
        }
    }

    // ==========================
    // AJOUT UTILISATEUR
    // ==========================
    public void ajouterUtilisateur() {

        FacesContext context = FacesContext.getCurrentInstance();

        if (!password.equals(confirmPassword)) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Les mots de passe ne correspondent pas", null));
            return;
        }

        if (utilisateurEntrepriseBean.usernameExiste(username)) {
            context.addMessage("ajoutUserForm:username",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ce nom d'utilisateur est déjà utilisé", null));
            return;
        }

        if (utilisateurEntrepriseBean.emailExiste(email)) {
            context.addMessage("ajoutUserForm:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Cet email est déjà utilisé", null));
            return;
        }

        utilisateurEntrepriseBean.ajouterUtilisateurEntreprise(
                username, email, password, description);

        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Utilisateur ajouté avec succès", null));

        resetForm();
    }

    // ==========================
    // MODIFIER MOT DE PASSE
    // ==========================
    public void updatePassword() {

        FacesContext context = FacesContext.getCurrentInstance();

        // 1. Récupérer l'utilisateur actuel pour avoir son mot de passe haché
        Utilisateur currentU = utilisateurEntrepriseBean.trouverUtilisateurParEmail(email);

        // 2. Vérification ancien mot de passe
        if (currentU == null || !utilisateurEntrepriseBean.verifierMotDePasse(oldPassword, currentU.getPassword())) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Ancien mot de passe incorrect", null));
            return;
        }

        // 3. Vérification confirmation
        if (newPassword == null || newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Les nouveaux mots de passe ne correspondent pas", null));
            return;
        }

        utilisateurEntrepriseBean.modifierMotDePasse(username, newPassword);

        context.addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Mot de passe modifié avec succès", null));

        oldPassword = "";
        newPassword = "";
        confirmPassword = "";
    }

    // ==========================
    // DÉCONNEXION
    // ==========================
    public String logout() {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();

        return "../index?faces-redirect=true";
    }

    // ==========================
    // UTILS
    // ==========================
    private void resetForm() {
        username = "";
        email = "";
        password = "";
        confirmPassword = "";
        description = "";
    }

    // ==========================
    // GETTERS / SETTERS
    // ==========================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
