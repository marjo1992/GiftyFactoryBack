package com.marjo.giftyfactoryback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "IDEA_STATUS")
@AllArgsConstructor
public enum IdeaStatusEnum {

        DRAFT("Draft", "initial status"),
        TO_OFFER("To offer", "put by idea creator"),
        RESERVED("Reserved", "put by person wich want to offer the idea"),
        BOUGHT("Bought", "put by person wich want to offer the idea"),
        OFFERED("Offered", "put by person wich want to offer the idea, or list manager"),
        RECEIVED("Received", "put by person for wich is the gift idea or by his owner");

    @Id
    public final byte id = (byte)ordinal();

    @Column(unique = true, nullable = false)
    public final String code = name();

    @Column(nullable = false)
    public final String label;

    @Column(nullable = false)
    public final String desc;
}