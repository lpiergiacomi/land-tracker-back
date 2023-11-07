package com.api.landtracker.model.entities;

import com.api.landtracker.model.dto.View;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "files")
@JsonView(View.Basic.class)
public class File {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    @Column(name = "lot_id")
    private Long lotId;


    public File(String name, String type, byte[] data, Long lotId) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.lotId = lotId;
    }

}