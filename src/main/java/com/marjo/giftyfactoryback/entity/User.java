package com.marjo.giftyfactoryback.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "USER")
public class User {

    @Id
    long personId;

    @OneToOne(cascade = CascadeType.DETACH)
    //@JoinColumn(name = "personId")
    //@JsonBackReference
    @MapsId
    Person person; // PK - FK vers Person

    @Column(name = "username", length = 50, nullable = false, unique = true)
    String username;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    String email;

    @Column(name = "password", length = 100, nullable = false)
    String password;

    @Column(name = "picture", length = 50)
    String picture;

    @Column(name = "is_email_confirmed", nullable = false)
    boolean isEmailConfirmed = false; // Vrai si l'utilisateur à confirmer son adresse mail (par un mail de
                                     // confirmation)

    @Column(name = "email_token", length = 50)
    String emailToken; // string à retourner pour confirmer le mail

    @Column(name = "is_himself_owner", nullable = false)
    boolean isHimselfOwner = true; // Faux dans le cas où un utilisateur crée son compte, mais sa "personne" est
                                   // géré par un responsable, l'utilisateur deviendra actif quand le responsable
                                   // lui transmettra sa responsabilité.

}