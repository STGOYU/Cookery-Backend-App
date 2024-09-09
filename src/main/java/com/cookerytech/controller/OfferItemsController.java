package com.cookerytech.controller;

import com.cookerytech.dto.OfferItemDTO;
import com.cookerytech.dto.request.OfferItemsUpdate;
import com.cookerytech.service.OfferItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/offer-items")
public class OfferItemsController {

    private final OfferItemService offerItemService;

    public OfferItemsController(OfferItemService offerItemService) {
        this.offerItemService = offerItemService;
    }


    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferItemDTO> updateOfferItems(@PathVariable Long id, @RequestBody OfferItemsUpdate offerItemsUpdate){
        OfferItemDTO offerItemDTO = offerItemService.updateOfferItems(id,offerItemsUpdate);
        return ResponseEntity.ok(offerItemDTO);
    }

    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferItemDTO> deleteOfferItems(@PathVariable Long id){
        OfferItemDTO deleteOfferItems = offerItemService.deleteOfferItemsById(id);
        return ResponseEntity.ok(deleteOfferItems);
    }

}
