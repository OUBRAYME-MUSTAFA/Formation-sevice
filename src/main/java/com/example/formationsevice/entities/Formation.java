package com.example.formationsevice.entities;


import com.example.formationsevice.model.Chercheur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;

    @Transient
    private Chercheur responsable;
    private Long responsableId;
    @Column(columnDefinition = "LONGBLOB" )
    private  byte[] document;
    @Column(columnDefinition = "LONGBLOB" )
    private  byte[]  image;
}
