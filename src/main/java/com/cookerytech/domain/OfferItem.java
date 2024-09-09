package com.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_offeritem")
public class OfferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //anlaşılamadı
    @Column(nullable = false,length = 100)
    @Size(min=1,max=100)
    private  String sku;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double sellingPrice;

    @Column(nullable = false)
    private Double tax;

    @Column(nullable = false)
    private  Double subTotal;

    @Column(nullable = false)
    private  Double discount=10.0;

    @OneToOne
    @JoinColumn(name = "product_id")
    private  Product product;

    @OneToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @OneToOne
    @JoinColumn(name = "model_id")
    private Model model;


    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = true)
    private LocalDateTime updateAt;

}