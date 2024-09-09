package com.cookerytech.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_modelPropertyValue")
public class ModelPropertyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String value;

    @OneToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @OneToOne
    @JoinColumn(name = "modelPropertyKey")
    private ProductPropertyKey productPropertyKey;
}
