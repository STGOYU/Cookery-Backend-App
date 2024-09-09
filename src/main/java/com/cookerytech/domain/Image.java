package com.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_image")
public class Image {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String type;

    @Column(nullable = true)
    private Long model_id;

    @OneToOne(cascade = CascadeType.ALL) //ImageFile sildigimizde ImageData'yi da siliyoruz.
    private ImageData imageData;

    public Image(String name, String type, Long model_id, ImageData imageData){
        this.name = name;
        this.type = type;
        this.model_id = model_id;
        this.imageData = imageData;
    }


    public Image(String name, String type, ImageData imageData) {
        this.name = name;
        this.type = type;
        this.imageData = imageData;
    }
}
