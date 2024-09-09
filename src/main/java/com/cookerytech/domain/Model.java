package com.cookerytech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_model")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)  //DTO da @Size min 5 max 150
    private String title;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private Integer stockAmount;

    @Column(nullable = false)
    private Integer inBoxQuantity=1;

    @Column(nullable = false)
    private Integer seq=0;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name="model_id")
    private Set<Image> images;

    @Column(nullable = false)
    private Double buyingPrice;

    @Column(nullable = false)
    private Double taxRate=0.0;

    @Column(nullable = false)
    private Boolean isActive=true;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @OneToOne
    @JoinColumn(name = "currency_id",referencedColumnName = "id")
    private Currency currency;  //fk

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private  Product product;  //fk

    @Column(nullable = false)
    private LocalDateTime createAt;
    
    private LocalDateTime updateAt;

}
