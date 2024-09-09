package com.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 70)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double profitRate=0.0;

    @Column(nullable = false)
    private Boolean isActive=true;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @Column(nullable = false)
    private LocalDateTime createAt;


    private LocalDateTime updateAt;

}
