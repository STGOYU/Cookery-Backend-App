package com.cookerytech.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    @Size(min=2, max=30)
    private String firstName;

    @Column(length = 30, nullable = false)
    @Size(min=2, max=30)
    private String lastName;

    @Column(length = 80, nullable = false, unique = true)
    @Size(min=10, max=80)
    private String email;

    @Column(length = 14, nullable = false)
    private String phone;

    @Column(length = 150, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 100, nullable = false)
    private String country;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String taxNo;

    private Integer status;

    @Column(length = 120, nullable = false)
    private String password;

    @Column(length = 120)
    private String resetPasswordCode;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createAt;

    //@Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @ManyToMany //LAZY
    @JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}

