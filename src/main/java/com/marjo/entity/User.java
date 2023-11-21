package com.marjo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "USER")
public class User {

    @Id
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "person_id")
    @JsonBackReference
    Person person; // PK - FK vers Person

    @Column(name = "mail", length = 50, nullable = false, unique = true)
    String mail;

    @Column(name = "pasword", length = 50, nullable = false)
    String password;

    @Column(name = "picture", length = 50)
    String picture;

    @Column(name = "is_mail_confirmed", nullable = false)
    boolean isMailConfirmed = false; // Vrai si l'utilisateur à confirmer son adresse mail (par un mail de
                                     // confirmation)

    @Column(name = "mail_token", length = 50)
    String mailToken; // string à retourner pour confirmer le mail

    @Column(name = "is_himself_owner", nullable = false)
    boolean isHimselfOwner = true; // Faux dans le cas où un utilisateur crée son compte, mais sa "personne" est
                                   // géré par un responsable, l'utilisateur deviendra actif quand le responsable
                                   // lui transmettra sa responsabilité.

}