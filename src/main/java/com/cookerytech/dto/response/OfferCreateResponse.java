package com.cookerytech.dto.response;

import com.cookerytech.domain.OfferItem;
import com.cookerytech.dto.OfferItemDTO;
import com.cookerytech.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferCreateResponse {

    private Long id;
    private String code;
    private String status;
    List<OfferItemDTO> items = new ArrayList<>();
    private UserDTO userDTO;

    @Override
    public String toString() {
        return "OfferCreateResponse{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", items=" + items +
                ", userDTO=" + userDTO +
                '}';
    }
}
