package com.marjo.giftyfactoryback.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "VISIBILITY")
@AllArgsConstructor
public enum VisibilityEnum {

        PRIVATE("Private", ""),
        HOME("My home", ""),
        CIRCLE("Circle", ""),
        PUBLIC("Public", "");

    @Id
    public final byte id = (byte)ordinal();

    @Column(unique = true, nullable = false)
    public final String code = name();

    @Column(nullable = false)
    public final String label;

    @Column(nullable = false)
    public final String desc;
}