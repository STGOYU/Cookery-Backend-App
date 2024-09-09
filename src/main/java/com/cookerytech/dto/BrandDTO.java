package com.cookerytech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {


    private Long id;


    private String name;


    private Double profitRate;


    private Boolean isActive;


    private Boolean builtIn;


    private LocalDateTime createAt;


    private LocalDateTime updateAt;

}
