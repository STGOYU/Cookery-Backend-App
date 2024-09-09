package com.cookerytech.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferReportResponse {

    private String period;
    private Integer totalProduct;
    private Double totalAmount;
}