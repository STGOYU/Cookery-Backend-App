package com.cookerytech.domain.enums;

public enum RoleType {
    ROLE_CUSTOMER("Customer"),
    ROLE_ADMIN("Administrator"),
    ROLE_PRODUCT_MANAGER("ProductManager"),
    ROLE_SALES_SPECIALIST("SalesSpecialist"),
    ROLE_SALES_MANAGER("SalesManager");

    private String name;

    private RoleType (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
