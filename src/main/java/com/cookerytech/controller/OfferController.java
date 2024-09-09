package com.cookerytech.controller;

import com.cookerytech.domain.User;
import com.cookerytech.dto.OfferDTO;
import com.cookerytech.dto.OfferDTOinItemsAndUser;
import com.cookerytech.dto.OfferItemDTO;
import com.cookerytech.dto.request.OfferCreate;
import com.cookerytech.dto.request.OfferItemsUpdate;
import com.cookerytech.dto.request.OfferUpdate;
import com.cookerytech.dto.response.OfferCreateResponse;
import com.cookerytech.dto.response.UpdateOfferResponse;
import com.cookerytech.dto.response.UsersOfferAndItemsResponse;
import com.cookerytech.service.OfferItemService;
import com.cookerytech.service.OfferService;
import com.cookerytech.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import com.cookerytech.dto.response.OfferResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    private final UserService userService;
    private final OfferItemService offerItemService;

    public OfferController(OfferService offerService, @Lazy UserService userService, OfferItemService offerItemService) {
        this.offerService = offerService;
        this.userService = userService;
        this.offerItemService = offerItemService;
    }

    @GetMapping("/admin")  // E01
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<Page<OfferDTO>> getOffersForAdmin(
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(value = "status", required = false) Integer statusValue,
            @RequestParam(value = "date1", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
            @RequestParam(value = "date2", required = false)
                @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createAt") String prop,
            @RequestParam(value = "type",
                          required = false,
                          defaultValue = "DESC") Sort.Direction direction
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

//        OfferStatus status = null;
//        if (statusValue != null) {
//            status = OfferStatus.fromValue(statusValue);
//        }
//

        Page<OfferDTO> filteredOffers = offerService.findFilteredOffers(query.toLowerCase(), statusValue, date1, date2, pageable);


        return ResponseEntity.ok(filteredOffers);
    }

    @GetMapping("/{id}/auth")
    public ResponseEntity<OfferDTO> getUserOfferById(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        OfferDTO offerDTO =offerService.findByIdAndUser(id,user);
        return ResponseEntity.ok(offerDTO);
    }

    @GetMapping("/auth")
    public ResponseEntity<Page<OfferDTO>> getOffers(
            @RequestParam(required = false, value = "q", defaultValue = "") String q,
            @RequestParam(required = false, value = "date1", defaultValue = "") String date1,
            @RequestParam(required = false, value = "date2", defaultValue = "") String date2,
            @RequestParam(required = false, value = "status", defaultValue = "") String status,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false,value = "size", defaultValue = "20") int size,
            @RequestParam(required = false,value = "sort", defaultValue = "createAt") String prop,
            @RequestParam(required = false,value = "type", defaultValue = "DESC") Sort.Direction direction)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        String qLower = q.toLowerCase();
        String statusLower = status.toLowerCase();
        Page<OfferDTO> offerDTOPage = offerService.getAllOffers(qLower,date1,date2,statusLower, pageable);
        return ResponseEntity.ok(offerDTOPage);
    }

   @GetMapping("/admin/user/{id}")//E03
   @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<Page<OfferDTO>> getOffersByUserId(@PathVariable("id") Long id,
                                                                              @RequestParam(required = false, value = "date1", defaultValue = "") String date1,
                                                                              @RequestParam(required = false, value = "date2", defaultValue = "") String date2,
                                                                              @RequestParam(required = false, value = "status", defaultValue = "") int status,
                                                                              @RequestParam(required = false, value = "page", defaultValue = "0") int page,
                                                                              @RequestParam(required = false,value = "size", defaultValue = "20") int size,
                                                                              @RequestParam(required = false,value = "sort", defaultValue = " createAt") String prop,
                                                                              @RequestParam(required = false,value = "type", defaultValue = "DESC") Sort.Direction direction){

       Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
       Page<OfferDTO> offerDTOPage = offerService.getAllOffersByUser(id,date1,date2,status, pageable);
        return ResponseEntity.ok(offerDTOPage);
    }

    @GetMapping("/{id}/admin")          //Page-58->E02
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferDTOinItemsAndUser> getOfferById(@PathVariable Long id){
        OfferDTOinItemsAndUser offerDTOinItemsAndUser = offerService.getOfferDTOWithItemsAndUser(id);
        return ResponseEntity.ok(offerDTOinItemsAndUser);
    }

    @PostMapping("/auth")               //Page 62->E06
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<OfferCreateResponse> createOffer(@Valid @RequestBody OfferCreate offerCreate){
      OfferCreateResponse offerCreateResponse = offerService.makeOffer(offerCreate);
      return ResponseEntity.ok(offerCreateResponse);
    }


    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferDTO> updateOffers(@PathVariable Long id, @Valid @org.springframework.web.bind.annotation.RequestBody OfferUpdate offerUpdate){
        OfferDTO offerDTO = offerService.updateOffers(id,offerUpdate);
        return ResponseEntity.ok(offerDTO);
    }





}
