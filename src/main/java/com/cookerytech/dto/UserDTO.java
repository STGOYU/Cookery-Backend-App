package com.cookerytech.dto;

import com.cookerytech.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {


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
    private Boolean builtIn;
    private Set<String> roles;
    public void setRoles(Set<Role> roles) {
        Set<String> roleStr = new HashSet<>();
        roles.forEach(r->{
            roleStr.add(r.getType().getName());
        });

        this.roles=roleStr;

    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", birthDate=" + birthDate +
                ", taxNo='" + taxNo + '\'' +
                ", status=" + status +
                '}';
    }
}
