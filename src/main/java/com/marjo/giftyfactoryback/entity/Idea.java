package com.marjo.giftyfactoryback.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "IDEA")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event__event_id")
    @JsonManagedReference
    Event event;

    @Column(name = "NAME", length = 50, nullable = false)
    String name;

    @Column(name = "URL_LINK", length = 2000)
    String urlLink;

    @Column(name = "PRICE")
    Long price;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS__idea_status_id", nullable = false)
    IdeaStatusEnum status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AUTHOR__user_person_id", nullable = false)
    @JsonManagedReference
    User author;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "VISIBILITY_visibility_id", nullable = false)
    VisibilityEnum visibility;

    @ManyToMany
    @JoinTable( name = "ASSO_IDEA_AND_PERSON_RECIPIENT",
                joinColumns = @JoinColumn( name = "idea_id" ),
                inverseJoinColumns = @JoinColumn( name = "person_id" ) )
    private List<Person> recipients = new ArrayList<>();

}