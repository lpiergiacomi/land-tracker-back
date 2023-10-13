package com.api.landtracker.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "lot")
@NoArgsConstructor
@AllArgsConstructor
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "area")
    private Integer area;

    @Column(name = "block")
    private String block;

    @Column(name = "zone")
    private String zone;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private LotState state;

    @Embedded
    private LotPosition position;

    @Column(name = "metersFront")
    private Double metersFront;

    @Column(name = "metersBack")
    private Double metersBack;

    @Column(name = "cadastralAccNumber")
    private String cadastralAccNumber;

    @Column(name = "municipalAccNumber")
    private String municipalAccNumber;

    @Column(name = "hasLight")
    private Boolean hasLight;

    @Column(name = "hasWater")
    private Boolean hasWater;

    @Column(name = "price")
    private Double price;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "client")
    private Client client;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(
            name = "lot_assigment",
            joinColumns = { @JoinColumn(name = "lote_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private List<User> assignedUsers = new ArrayList<>();


}
