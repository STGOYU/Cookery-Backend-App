package com.cookerytech.service;

import com.cookerytech.config.EmailConfig;
import com.cookerytech.domain.Offer;
import com.cookerytech.domain.Role;
import com.cookerytech.domain.User;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.dto.UserDTO;
import com.cookerytech.dto.request.*;
import com.cookerytech.dto.response.UserResponse;
import com.cookerytech.exception.BadRequestException;
import com.cookerytech.exception.ConflictException;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.mapper.UserMapper;
import com.cookerytech.repository.UserRepository;
import com.cookerytech.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final OfferService offerService;

    @Autowired
    private EmailConfig emailConfig;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    @Lazy private AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, OfferService offerService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.offerService = offerService;
        this.userMapper = userMapper;
    }


    public UserDTO saveUser(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, registerRequest.getEmail()));
        }


        Role role = roleService.findByType(RoleType.ROLE_CUSTOMER);

        Set<Role> roles = new HashSet<>();
        roles.add(role);



        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        LocalDateTime now = LocalDateTime.now();


        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setAddress(registerRequest.getAddress());
        user.setCity(registerRequest.getCity());
        user.setCountry(registerRequest.getCountry());
        user.setBirthDate(registerRequest.getBirthDate());
        user.setTaxNo(registerRequest.getTaxNo());
        user.setCreateAt(now);
        user.setPassword(encodedPassword);


        user.setRoles(roles);

       User savedUser = userRepository.save(user);
       UserDTO userDTO = userMapper.userToUserDTO(savedUser);


       return userDTO;
    }
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, email)));
        return user;
    }

    public User getById(Long id){
        User user = userRepository.findUserById(id).orElseThrow(()->new
                ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return user;
    }

    //TODO => Offer'ı (teklifi) varsa silinemez eklenecek
    public UserDTO removeUserById(Long id) {
        User user = getById(id);
        Boolean exists = offerService.existsByUser(user);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        User currentUser = getCurrentUser();

        if(exists){
            throw new BadRequestException(ErrorMessage.CAN_NOT_BE_DELETED_MESSAGE);
        }

        if (user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if(currentUser.getRoles().stream().map(role ->role.getType().getName()).collect(Collectors.toList()).contains(RoleType.ROLE_SALES_SPECIALIST.getName())){
                if(user.getRoles().stream().map(role ->role.getType().getName()).collect(Collectors.toList()).contains(RoleType.ROLE_CUSTOMER.getName())){
            userRepository.deleteById(id);
        } else {
            throw  new BadRequestException(ErrorMessage.UNAUTH_TRANSACTİON);
        }}

        if(currentUser.getRoles().stream().map(role ->role.getType().getName()).collect(Collectors.toList()).contains(RoleType.ROLE_SALES_MANAGER.getName())){
               if( (user.getRoles().stream().map(role ->role.getType().getName()).collect(Collectors.toList()).contains(RoleType.ROLE_CUSTOMER.getName()) ||
                        user.getRoles().stream().map(role ->role.getType().getName()).collect(Collectors.toList()).contains(RoleType.ROLE_SALES_SPECIALIST.getName())))
        {
            userRepository.deleteById(id);
        } else {
            throw  new BadRequestException(ErrorMessage.UNAUTH_TRANSACTİON);
        }}

        if(currentUser.getRoles().stream().map(role ->role.getType().getName()).collect(Collectors.toList()).contains(RoleType.ROLE_ADMIN.getName()) )
        {
            userRepository.deleteById(id);
        }

        return userDTO;
    }

    public Page<UserResponse> getUserPage(String qLower, Pageable pageable) {

        Page<UserResponse> usersWithPage = null;
        if (!qLower.isEmpty()) {
            usersWithPage = userRepository.getAllUserWithQAdmin(qLower, pageable);
        } else {
            usersWithPage = userRepository.findAllWithPage(pageable);
        }
        return usersWithPage;
    }
    public String createPasswordResetToken(String email) {
        User user =  getUserByEmail(email);

//        // Şifre sıfırlama tokenı oluştur
        String tokenValue = generateToken();
//        PasswordResetToken token = new PasswordResetToken();
//        token.setUser(user);
//        token.setToken(tokenValue);
//
//        passwordResetTokenRepository.save(token);

        user.setResetPasswordCode(tokenValue);
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getConstantEmail());
        message.setTo(email);
        message.setSubject("Attention!!!");

        message.setText(tokenValue);
        mailSender.send(message);
        return tokenValue;
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

        String code =  resetPasswordRequest.getCode();
        String password = passwordEncoder.encode(resetPasswordRequest.getNewPassword());
        User user = userRepository.findUserByResetPasswordCode(code).orElseThrow(()-> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_EXCEPTION));
        user.setPassword(password);
        user.setResetPasswordCode("");
        userRepository.save(user);

    }

    private String generateToken() {
        return UUID.randomUUID().toString(); // Rastgele UUID oluşturma
//        generateToken() metodu, java.util.UUID.randomUUID().toString()
//        ile rastgele bir UUID oluşturur ve bu UUID'yi string olarak döndürür.
//        Bu UUID, benzersizliği garanti eden bir dize olarak şifre sıfırlama tokenı olarak kullanılabilir.
    }

    public void updatePassword(UserUpdateRequest userUpdateRequest) {

        User user = getCurrentUser();

        if(user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if(!passwordEncoder.matches(userUpdateRequest.getOldPassword(), user.getPassword())){
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED_MESSAGE);
        }

        String hashedPassword = passwordEncoder.encode(userUpdateRequest.getNewPassword());

        user.setPassword(hashedPassword);

        userRepository.save(user);

    }

    public User getCurrentUser(){
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(
                ()-> new ResourceNotFoundException(ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
        User user = getUserByEmail(email);
        return user;
    }

    // 13.08.2023
    public UserDTO getPrincipal() {
        User user = getCurrentUser();

        UserDTO userDTO = userMapper.userToUserDTO(user);

        return userDTO;
    }

    public void removeUserByAuth(UserDeleteRequest userDeleteRequest) {
        User user = getCurrentUser();
        Boolean exists = offerService.existsByUser(user);

        if(exists){
            throw new BadRequestException(ErrorMessage.CAN_NOT_BE_DELETED_MESSAGE);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(),userDeleteRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(userDetails !=null){
            userRepository.deleteById(user.getId());
        }
    }

    public UserResponse getUserResponseById(Long id) {
        return new UserResponse(getById(id));

    }


    public UserResponse updateUser(UpdateAuthenticatedUser userRequest) {

        User user =getCurrentUser();
      if(user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
      }


        boolean emailExist = userRepository.existsByEmail(userRequest.getEmail());// burada Db de varmı baklılcak
        //Sneryo kontrolu
        if(emailExist && !userRequest.getEmail().equals(user.getEmail())) {// buraya 3 senaryoda girme kontorlu
            throw new ConflictException(
                    String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,userRequest.getEmail()));
        }

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setCity(userRequest.getCity());
        user.setCountry(userRequest.getCountry());
        user.setBirthDate(userRequest.getBirthDate());
        user.setTaxNo(userRequest.getTaxNo());
        user.setUpdateAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse(updatedUser);
        return userResponse;
    }

    public long getNumberOfCustomers() {
      return   userRepository.countCustomer();
    }

    public UserResponse updateUserResponseById(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {

        User user = getCurrentUser();
        User alteredUser = getById(id);

        boolean currentUserAdmin = user.getRoles().contains(roleService.findByType(RoleType.ROLE_ADMIN));
        boolean currentUserSalesManager = user.getRoles().contains(roleService.findByType(RoleType.ROLE_SALES_MANAGER));
        boolean currentUserSalesSpecialist = user.getRoles().contains(roleService.findByType(RoleType.ROLE_SALES_SPECIALIST));



            if(alteredUser.getBuiltIn()){
                throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
            }

            boolean emailExist = userRepository.existsByEmail(adminUserUpdateRequest.getEmail());

            if(emailExist && !adminUserUpdateRequest.getEmail().equals(alteredUser.getEmail())) {
                throw new ConflictException(
                        String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,adminUserUpdateRequest.getEmail()));
            }


            if ((currentUserAdmin)||
                    (currentUserSalesManager && !(alteredUser.getRoles().contains(roleService.findByType(RoleType.ROLE_ADMIN)) ||
                            alteredUser.getRoles().contains(roleService.findByType(RoleType.ROLE_PRODUCT_MANAGER)) ||
                            alteredUser.getRoles().contains(roleService.findByType(RoleType.ROLE_SALES_MANAGER)))) ||
                    (currentUserSalesSpecialist && !(alteredUser.getRoles().contains(roleService.findByType(RoleType.ROLE_ADMIN)) ||
                            alteredUser.getRoles().contains(roleService.findByType(RoleType.ROLE_PRODUCT_MANAGER)) ||
                            alteredUser.getRoles().contains(roleService.findByType(RoleType.ROLE_SALES_MANAGER)) ||
                            alteredUser.getRoles().contains(roleService.findByType(RoleType.ROLE_SALES_SPECIALIST))))){

                alteredUser.setFirstName(adminUserUpdateRequest.getFirstName());
                alteredUser.setLastName(adminUserUpdateRequest.getLastName());
                alteredUser.setEmail(adminUserUpdateRequest.getEmail());
                alteredUser.setPassword(passwordEncoder.encode(adminUserUpdateRequest.getPassword()));
                alteredUser.setPhone(adminUserUpdateRequest.getPhone());
                alteredUser.setAddress(adminUserUpdateRequest.getAddress());
                alteredUser.setCity(adminUserUpdateRequest.getCity());
                alteredUser.setCountry(adminUserUpdateRequest.getCountry());
                alteredUser.setBirthDate(adminUserUpdateRequest.getBirthDate());
                alteredUser.setTaxNo(adminUserUpdateRequest.getTaxNo());
                alteredUser.setStatus(adminUserUpdateRequest.getStatus());
                alteredUser.setBuiltIn(adminUserUpdateRequest.getBuiltIn());
                alteredUser.setUpdateAt(LocalDateTime.now());


            }else {

                throw new BadRequestException(String.format(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE));

            }

            if(currentUserAdmin){
                alteredUser.setRoles(convertRoles(adminUserUpdateRequest.getRoles()));
            }

            User updatedUser = userRepository.save(alteredUser);

            UserResponse response = new UserResponse(updatedUser);
            response.setRoles(updatedUser.getRoles());
            return response;

    }

    private Set<Role> convertRoles(Set<String> pRoles){
        Set<Role> roles = new HashSet<>();

        if(pRoles==null){
            Role userRole = roleService.findByType(RoleType.ROLE_CUSTOMER);
            roles.add(userRole);
        } else {
            pRoles.forEach(roleStr->{
                if(roleStr.equals(RoleType.ROLE_ADMIN.getName())){ // Administrator
                    Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
                    roles.add(adminRole); //ROLE_ADMIN
                } else if (roleStr.equals(RoleType.ROLE_CUSTOMER.getName())){
                    Role userRole = roleService.findByType(RoleType.ROLE_CUSTOMER);// Customer
                    roles.add(userRole);//ROLE_CUSTOMER
                } else if (roleStr.equals(RoleType.ROLE_PRODUCT_MANAGER.getName())){
                    Role userRole = roleService.findByType(RoleType.ROLE_PRODUCT_MANAGER);// Product Manager
                    roles.add(userRole);//ROLE_PRODUCT_MANAGER
                } else if(roleStr.equals(RoleType.ROLE_SALES_MANAGER.getName())){
                    Role userRole = roleService.findByType(RoleType.ROLE_SALES_MANAGER);// Sales Manager
                    roles.add(userRole);//ROLE_SALES_MANAGER
                } else {
                    Role userRole = roleService.findByType(RoleType.ROLE_SALES_SPECIALIST);// Sales Specialist
                    roles.add(userRole);//ROLE_SALES_SPECIALIST
                }
            });
        }
        return roles;
    }



}


