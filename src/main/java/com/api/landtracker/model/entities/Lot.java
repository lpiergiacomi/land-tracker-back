package com.api.landtracker.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
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
    private BigDecimal price;

    @Column(name = "saleDate")
    private Date saleDate;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "client")
    private Client client;

    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "files", referencedColumnName = "id")
    private List<File> files;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "lot_id", referencedColumnName = "id")
    private List<Payment> items;


}
