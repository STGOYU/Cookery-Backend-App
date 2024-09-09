package com.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(length = 80, nullable = false)  //DTO da @Size min 2 max 80
    @NotBlank
    private String title;

    private String description;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @Column(nullable = false)
    private Integer seq=0;


    @Column(length = 200, nullable = false)  //DTO da @Size min 5 max 200
    private String slug;

    @Column(nullable = false)
    private Boolean isActive=true;

    @Column(nullable = false)
    private Integer mainCategoryId;  //fk

    @Column(nullable = false)
    private LocalDateTime createAt;

    // @Column(nullable = true)
    private LocalDateTime updateAt;

}