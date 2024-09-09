package com.cookerytech.dto.response;

import com.cookerytech.domain.Role;
import com.cookerytech.domain.User;
import com.cookerytech.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {



    //test
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String country;

    private LocalDate birthDate;

    private String taxNo;

    private Integer status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Boolean builtIn=false;

    private Set<String> roles;

    // Converting Set<Role> roles in DB to Set<String> roles as dto
    public void setRoles(Set<Role> roles) {
        Set<String> rolesStr = new HashSet<>();

        for (Role r:roles
        ) {
            if(r.getType().equals(RoleType.ROLE_CUSTOMER)) {
                rolesStr.add("Customer");
            } else if (r.getType().equals(RoleType.ROLE_ADMIN)) {
                rolesStr.add("Administrator");
            } else if (r.getType().equals(RoleType.ROLE_PRODUCT_MANAGER)) {
                rolesStr.add("ProductManager");
            }else if (r.getType().equals(RoleType.ROLE_SALES_SPECIALIST)) {
                rolesStr.add("SalesSpecialist");
            }else if (r.getType().equals(RoleType.ROLE_SALES_MANAGER)) {
                rolesStr.add("SalesManager");
            }
            else {
                rolesStr.add("Anonymous");
            }
        }

        this.roles=rolesStr;
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.birthDate = user.getBirthDate();
        this.email = user.getEmail();
        this.city = user.getCity();
        this.country = user.getCountry();
        this.builtIn = user.getBuiltIn();
        this.status = user.getStatus();
        this.taxNo = user.getTaxNo();
        this.updateAt = user.getUpdateAt();
        this.createAt = user.getCreateAt();
       setRoles(user.getRoles());

       // this.roles=getRoles();  //  Set<Role> leri Set<String> e
        // yukardaki methodan dan donusturuyor.
    }
}
