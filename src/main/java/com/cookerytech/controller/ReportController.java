package com.cookerytech.controller;

import com.cookerytech.dto.ProductDTO;
import com.cookerytech.dto.response.DashboardResponse;
import com.cookerytech.dto.response.OfferReportResponse;
import com.cookerytech.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')  or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<DashboardResponse> getIstatistics() {
        DashboardResponse dashboardResponse = reportService.getIstatistics();
        return ResponseEntity.ok(dashboardResponse);
    }

    @GetMapping("/unoffered-products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<List<ProductDTO>> getProductsNoOffer() {  //G04

        List<ProductDTO> productsNoOffer = reportService.getProductsNoOffer();

        return ResponseEntity.ok(productsNoOffer);

    }

    @GetMapping("/most-popular-products")
    @PreAuthorize("hasRole('ADMIN')  or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<List<ProductDTO>> getMostPopularProducts(@RequestParam("amount") int amount) {  //G03

        List<ProductDTO> mostPopularProducts = reportService.getMostPopularProducts(amount);

        return ResponseEntity.ok(mostPopularProducts);

    }

    //G02
    @GetMapping("/offers")
    @PreAuthorize("hasRole('ADMIN')  or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<List<OfferReportResponse>> getAllOffersReport(
                                                            @RequestParam("type")  String type,
                                                            @RequestParam("date1") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date1,
                                                            @RequestParam("date2") @DateTimeFormat(pattern = "dd-MM-yyyy") Date date2
    ) {
        List<OfferReportResponse> allOffersReport =reportService.getAllOffersReport(type, date1, date2);
        return ResponseEntity.ok(allOffersReport);
    }
}
