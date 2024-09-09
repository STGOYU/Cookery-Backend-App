package com.cookerytech.repository;

import com.cookerytech.domain.User;
import com.cookerytech.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository//optional
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmail(String email);
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "id")
    Optional<User> findUserById(Long id);


    @Query("SELECT new com.cookerytech.dto.response.UserResponse(u) FROM User u")
    Page<UserResponse> findAllWithPage (Pageable pageable);

    @Query("SELECT new com.cookerytech.dto.response.UserResponse(user)  FROM User user where  lower(user.firstName) like %?1% OR lower(user.lastName) like %?1% OR lower(user.email) like %?1% or user.phone like %?1%")
    Page<UserResponse> getAllUserWithQAdmin(String q, Pageable pageable);

    void deleteByEmail(User user);
    @EntityGraph(attributePaths = "roles")
    User save(User user);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.type = 'ROLE_CUSTOMER'")
    long countCustomer();

    Optional<User> findUserByResetPasswordCode(String code);
}
