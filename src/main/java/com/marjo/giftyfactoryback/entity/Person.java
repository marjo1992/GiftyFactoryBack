package com.marjo.giftyfactoryback.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "PERSON")
public class Person {

    @Id
    @SequenceGenerator(name = "personSeq", sequenceName = "person_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "personSeq")
    long id;

    @Column(name = "NAME", length = 50, nullable = false, unique = false)
    String name;

    @Column(length = 50, nullable = false)
    String firstname;

    @Column(nullable = false)
    LocalDate birthdate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner__user_id")
    @JsonManagedReference
    User owner; /*
                 * créateur de la personne / utilisateur
                 * responsable = elle validera les liens de la personnes tant que
                 * la personne n'est pas associé à un utilisateur, elle peux modifier
                 * la personne (nom prénom date de naissance), elle peux transmettre
                 * sa "responsabilité" à un autre utilisateur (modification à l'acceptation)
                 * (vide si responsable soi meme)
                 */

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "next_owner__user_id")
    @JsonManagedReference
    User next_owner; // nouveaux responsable demandé

    @Column(length = 50)
    String owner_asked_message; // message de demande de récupération de

}
