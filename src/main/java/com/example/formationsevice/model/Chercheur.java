package com.example.formationsevice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Chercheur {
    @Id
    private Long id ;
    private String name;
    private String role;

}
