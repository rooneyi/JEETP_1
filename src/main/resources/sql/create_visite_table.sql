-- Script de création de la table VISITE pour la base de données
-- Module de Gestion des visites - jakartaMissioin

-- Créer la table VISITE
CREATE TABLE IF NOT EXISTS visite (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    utilisateur_id BIGINT NOT NULL,
    lieu_id INT NOT NULL,
    date_visite TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duree_minutes INT,
    commentaire VARCHAR(500),
    note INT CHECK (note >= 1 AND note <= 5),
    
    -- Clés étrangères
    -- Décommentez ces lignes une fois que les tables utilisateur et lieu existent
    CONSTRAINT fk_visite_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
    CONSTRAINT fk_visite_lieu FOREIGN KEY (lieu_id) REFERENCES lieu(id) ON DELETE CASCADE,
    
    -- Index pour améliorer les performances des requêtes
    INDEX idx_visite_utilisateur (utilisateur_id),
    INDEX idx_visite_lieu (lieu_id),
    INDEX idx_visite_date (date_visite)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Commentaires sur les colonnes
ALTER TABLE visite 
    MODIFY COLUMN id BIGINT AUTO_INCREMENT COMMENT 'Identifiant unique de la visite',
    MODIFY COLUMN utilisateur_id BIGINT NOT NULL COMMENT 'Référence à l utilisateur qui a effectué la visite',
    MODIFY COLUMN lieu_id INT NOT NULL COMMENT 'Référence au lieu visité',
    MODIFY COLUMN date_visite TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Date et heure de la visite',
    MODIFY COLUMN duree_minutes INT COMMENT 'Durée de la visite en minutes (optionnel)',
    MODIFY COLUMN commentaire VARCHAR(500) COMMENT 'Commentaire de l utilisateur sur la visite (max 500 caractères)',
    MODIFY COLUMN note INT COMMENT 'Note de 1 à 5 étoiles donnée par l utilisateur';

-- Données de test (optionnel - à supprimer en production)
-- Décommentez ces lignes pour insérer des données de test
/*
INSERT INTO visite (utilisateur_id, lieu_id, duree_minutes, commentaire, note) VALUES
(1, 1, 120, 'Très belle visite du lieu, paysage magnifique!', 5),
(1, 2, 90, 'Endroit intéressant mais un peu trop touristique', 4),
(2, 1, 60, 'Première visite rapide, à refaire plus longuement', 4),
(2, 3, 180, 'Journée complète passée ici, absolument incroyable!', 5),
(3, 2, 45, 'Visite décevante, pas à la hauteur des attentes', 2);
*/
