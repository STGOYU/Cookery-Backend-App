package com.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_imagedata")
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob //Buyuk objeleri DB tutmak icin bu annotation kullanilir.
    private byte[] data;

    public ImageData(byte[] data){
        this.data = data;
    }

    public ImageData(Long id){
        this.id = id;
    }
}
