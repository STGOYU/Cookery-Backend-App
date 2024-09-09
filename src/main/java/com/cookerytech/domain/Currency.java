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
@Table(name = "t_currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Size(min = 2, max = 10)
    @Column(nullable = false, unique = true, length = 10)
    private String code;

//    @Size(max = 3)
    @Column(nullable = true, length = 3)
    private String symbol;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = true)
    private LocalDateTime updateAt;

}
