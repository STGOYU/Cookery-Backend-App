package com.cookerytech.dto;

import com.cookerytech.domain.Model;
import com.cookerytech.domain.Offer;
import com.cookerytech.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferItemDTO {

    private Long id;
    private String sku;
    private Integer quantity;
    private Double sellingPrice;
    private Double tax;
    private Double subTotal;
    private Double discount=10.0;
    private Long productId;
    //private String productName;
    private Long offerId;
    private Long modelId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Override
    public String toString() {
        return "OfferItemDTO{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                ", sellingPrice=" + sellingPrice +
                ", tax=" + tax +
                ", subTotal=" + subTotal +
                ", discount=" + discount +
                ", productId=" + productId +
                ", offerId=" + offerId +
                ", modelId=" + modelId +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
