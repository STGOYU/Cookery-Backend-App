package com.cookerytech.controller;

import com.cookerytech.dto.UserDTO;
import com.cookerytech.dto.request.*;
import com.cookerytech.dto.response.CTResponse;
import com.cookerytech.dto.response.ResponseMessage;
import com.cookerytech.dto.response.UserResponse;
import com.cookerytech.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // !!!  Sisteme giriş yapan kullanıcının bilgisi...
    @GetMapping("/auth")  // F05
    @PreAuthorize("hasRole('ADMIN') or " +
                  "hasRole('CUSTOMER') or " +
                  "hasRole('PRODUCT_MANAGER') or " +
                  "hasRole('SALES_SPECIALIST') or " +
                  "hasRole('SALES_MANAGER')")
    public ResponseEntity<UserDTO> getUser() {

        UserDTO userDTO = userService.getPrincipal();  // currently login olan kullanıcı

        return ResponseEntity.ok(userDTO);
    }
    @DeleteMapping("/{id}/admin")    //Page 76
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<UserDTO> deleteByUserWithId(@PathVariable Long id){
        UserDTO userDTO = userService.removeUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/auth")     //Page 72
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<CTResponse> deleteByUserWithAuth(@Valid @RequestBody UserDeleteRequest userDeleteRequest){
        userService.removeUserByAuth(userDeleteRequest);
        CTResponse response = new CTResponse();
        response.setMessage(ResponseMessage.USER_DELETE_RESPONSE_MESSAGE);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<Page<UserResponse>> getAllUsersByPage(

            @RequestParam(required = false, value = "q", defaultValue = "") String q,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false,value = "size", defaultValue = "20") int size,
            @RequestParam(required = false,value = "sort", defaultValue = "createAt") String prop,
            @RequestParam(required = false,value = "type", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable= PageRequest.of(page, size, Sort.by(direction,prop));

        String qLower = q.toLowerCase();

        Page<UserResponse> usersWithPage = userService.getUserPage(qLower, pageable);
        return  ResponseEntity.ok(usersWithPage);
    }

    @PutMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateAuthenticatedUser updateAuthenticatedUser){
        UserResponse userResponse = userService.updateUser(updateAuthenticatedUser);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/auth")//F06
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER') or hasRole('PRODUCT_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<CTResponse> updatePassword(
            @Valid @RequestBody UserUpdateRequest userUpdateRequest){

        userService.updatePassword(userUpdateRequest);

        CTResponse response = new CTResponse();
        response.setMessage(ResponseMessage.PASSWORD_CHANGED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return ResponseEntity.ok(response);
    }
    //deneme...

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    @GetMapping("/{id}/admin")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id){
        UserResponse userResponse = userService.getUserResponseById(id);
       return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_SPECIALIST') or hasRole('SALES_MANAGER')")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable("id") Long id,@Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest){

        UserResponse response = userService.updateUserResponseById(id,adminUserUpdateRequest);

        return ResponseEntity.ok(response);
    }




}
