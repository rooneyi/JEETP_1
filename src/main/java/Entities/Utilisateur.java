/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author rooney
 */
@Entity
@Table(name="utilisateur")
public class Utilisateur implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable= false, unique= true, length= 50)
    private String username;
    
    
    @Column(nullable= false, unique= true, length= 100)
    private String email;
    
    @Column(nullable= false, length= 255)
    private String password;
    
    @Column(nullable = true, length = 255)
    private String description;

    public Utilisateur() {
    }

    public Utilisateur(String username, String email, String password, String description) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(Long id) {
        this.id = id;
    }    
}
