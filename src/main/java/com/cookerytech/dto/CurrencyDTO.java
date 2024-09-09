package com.cookerytech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {

    private Long id;

    private String code;

    private Double value;

    private String symbol;

  //  private LocalDateTime updateAt;


}
