package com.cookerytech.service;

import com.cookerytech.domain.*;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.dto.ProductDTO;
import com.cookerytech.dto.response.DashboardResponse;
import com.cookerytech.dto.response.OfferReportResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Service
public class ReportService {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductService productService;
    private final UserService userService;
    private final OfferService offerService;
    private final OfferItemService offerItemService;

    public ReportService(CategoryService categoryService, BrandService brandService, ProductService productService, UserService userService, OfferService offerService, OfferItemService offerItemService) {
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productService = productService;
        this.userService = userService;
        this.offerService = offerService;
        this.offerItemService = offerItemService;
    }

    public DashboardResponse getIstatistics() {
    long getNumberOfCategories= categoryService.getNumberOfCategories();
    long getNumberOfBrands= brandService.getNumberOfBrands();
    long getNumberOfProducts= productService.getNumberOfProducts();
    long getNumberOfCustomers= userService.getNumberOfCustomers();
    Double numberOfOffersPerDay= offerService.numberOfOffersPerDay();

    DashboardResponse dashboardResponse =
            new DashboardResponse(
                    getNumberOfCategories,
                    getNumberOfBrands,
                    getNumberOfProducts,
                    numberOfOffersPerDay,
                    getNumberOfCustomers);
return dashboardResponse;
    }

    public List<ProductDTO> getProductsNoOffer() {  // G04

        List<ProductDTO> productsNoOffer = productService.getProductsNoOffer();
        return productsNoOffer;

    }

    public List<ProductDTO> getMostPopularProducts(int amount) {
      return   productService.getMostPopularProducts(amount);
    }

    public List<OfferReportResponse> getAllOffersReport(String type, Date date1, Date date2) {

        List<OfferReportResponse> allOffersReport=new ArrayList<>();

        User currentUser = userService.getCurrentUser(); // Assuming you have a method to get the current user
        boolean authorized = false;

        for (Role role : currentUser.getRoles()) {
            if (    role.getType().equals(RoleType.ROLE_ADMIN)||
                    role.getType().equals(RoleType.ROLE_SALES_SPECIALIST)||
                    role.getType().equals(RoleType.ROLE_SALES_MANAGER)||
                    role.getType().equals(RoleType.ROLE_PRODUCT_MANAGER)) {
                authorized = true;
                break;
            }
        }

        LocalDateTime dateTime1 = dateToLocalDateTime(date1);
        LocalDateTime dateTime2 = dateToLocalDateTime(date2);
        // Saat ve dakikaları ayarlama
        dateTime1 = dateTime1.withHour(0).withMinute(0).withSecond(0).withNano(0);
        dateTime2 = dateTime2.withHour(23).withMinute(59).withSecond(0).withNano(0);

        List<Offer> offers = null;
        List<OfferItem> offersItem = null;
        offers= offerService.getAllOffersBetweenDate(dateTime1, dateTime2);

            for (Offer offer : offers) {
                allOffersReport.add(getOfferReportResponse(offer));
            }

        return allOffersReport;
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public OfferReportResponse getOfferReportResponse(Offer offer) {
       List<OfferItem> offerItems = offerItemService.getOfferItems(offer.getId());
        Set<Product> products = new HashSet<>();
        Double totalQuantity = 0.0;
        OfferReportResponse offerReportResponse = new OfferReportResponse();
        for (OfferItem offerItem : offerItems) {
            products.add(offerItem.getProduct());
            totalQuantity += offerItem.getQuantity();
        }
        offerReportResponse.setTotalAmount(totalQuantity);
        offerReportResponse.setTotalProduct(products.size());
        offerReportResponse.setPeriod("Day");
        return offerReportResponse;
    }
}
