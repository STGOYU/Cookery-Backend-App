package com.cookerytech.service;

import com.cookerytech.domain.Role;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.exception.ResourceNotFoundException;
import com.cookerytech.exception.message.ErrorMessage;
import com.cookerytech.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByType(RoleType roleType){
        Role role = roleRepository.findByType(roleType).orElseThrow(()->
                new ResourceNotFoundException(
                        String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION, roleType.name())));
        return role;
    }

}
