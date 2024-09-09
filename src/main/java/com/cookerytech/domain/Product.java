package com.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 150)  //DTO da @Size min 5 max 150
    private String title;

    @Column(length = 300,nullable = true)
    private String shortDesc;

    @Column(nullable = true)
    private String longDesc;

    @Column(nullable = false,length = 200)   //DTO da @Size min 5 max 200
    private String slug;

    @Column(nullable = false)
    private Integer seq=0;  //defines display order

    @Column(nullable = false)
    private Boolean isNew=false;  // If true show a“new” badge over product image

    @Column(nullable = false)
    private Boolean isFeatured=false;  // If true show the product in homepage

    @Column(nullable = false)
    private Boolean isActive=true;  // false: Not published true: published (for users)



    @Column(nullable = false)
    private Boolean builtIn=false;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = true)
    private LocalDateTime updateAt;


}
