package com.cookerytech;

import com.cookerytech.domain.Role;
import com.cookerytech.domain.User;
import com.cookerytech.domain.enums.RoleType;
import com.cookerytech.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.cookerytech.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@EnableScheduling


public class CookerytechApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookerytechApplication.class, args);

	}

}


	@Component
	@AllArgsConstructor
	class DemoCommandLineRunner implements CommandLineRunner {

		RoleRepository roleRepository;
		UserRepository userRepository;
		PasswordEncoder encoder;

		@Override
		public void run(String... args) throws Exception {

			if (!roleRepository.findByType(RoleType.ROLE_CUSTOMER).isPresent()) {
				Role roleCustomer = new Role();
				roleCustomer.setType(RoleType.ROLE_CUSTOMER);
				roleRepository.save(roleCustomer);
			}

			if (!roleRepository.findByType(RoleType.ROLE_ADMIN).isPresent()) {
				Role roleAdmin = new Role();
				roleAdmin.setType(RoleType.ROLE_ADMIN);
				roleRepository.save(roleAdmin);
			}

			if (!roleRepository.findByType(RoleType.ROLE_PRODUCT_MANAGER).isPresent()) {
				Role roleAdmin = new Role();
				roleAdmin.setType(RoleType.ROLE_PRODUCT_MANAGER);
				roleRepository.save(roleAdmin);
			}

			if (!roleRepository.findByType(RoleType.ROLE_SALES_SPECIALIST).isPresent()) {
				Role roleAdmin = new Role();
				roleAdmin.setType(RoleType.ROLE_SALES_SPECIALIST);
				roleRepository.save(roleAdmin);
			}

			if (!roleRepository.findByType(RoleType.ROLE_SALES_MANAGER).isPresent()) {
				Role roleAdmin = new Role();
				roleAdmin.setType(RoleType.ROLE_SALES_MANAGER);
				roleRepository.save(roleAdmin);
			}

			if (!userRepository.findByEmail("superadmin@gmail.com").isPresent()) {
				User admin = new User();
				Role role = roleRepository.findByType(RoleType.ROLE_ADMIN).get();
				admin.setRoles(new HashSet<>(Collections.singletonList(role)));
				admin.setAddress("super user address");
				admin.setEmail("superadmin@gmail.com");
				admin.setFirstName("superadminfirstname");
				admin.setLastName("superadminlastname");
				admin.setPassword(encoder.encode("Password1!"));
				admin.setPhone("(541) 317-8828");
				admin.setCity("New York");
				admin.setCountry("US");
				LocalDate birthday = LocalDate.of(2000,10,10);
				admin.setBirthDate(birthday);
				admin.setTaxNo("12345");
				admin.setCreateAt(LocalDateTime.now());
				admin.setBuiltIn(true);
				userRepository.save(admin);
			}


		}


	}


