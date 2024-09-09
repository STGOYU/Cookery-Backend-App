package com.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private long numberOfCategories;
    private long numberOfBrands;
    private long numberOfProducts;
    private Double numberOfOffersPerDay;
    private long numberOfCustomers;
}
