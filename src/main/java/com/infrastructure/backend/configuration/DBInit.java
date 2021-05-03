package com.infrastructure.backend.configuration;

import com.infrastructure.backend.entity.Role;
import com.infrastructure.backend.entity.User;
import com.infrastructure.backend.repository.RoleRepository;
import com.infrastructure.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DBInit {

    @Value("${infrastructure.roles}")
    private String appRole;

    @Value("${infrastructure.admin-username}")
    private String adminUsername;

    @Value("${infrastructure.admin-password}")
    private String adminPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        String[] roles = this.appRole.split(",");
        for (String role : roles) {

            if (!this.roleRepository.findByName(role).isPresent()) {
                Role roleEntity = new Role();
                roleEntity.setName(role);
                this.roleRepository.save(roleEntity);
            }

        }

        if (!this.userRepository.findByUsername(this.adminUsername).isPresent()) {
            User user = new User();
            user.setUsername(this.adminUsername);
            user.setPassword(passwordEncoder.encode(this.adminPassword));
            List<Role> roleList = new ArrayList<>();
            Role dbRole = this.roleRepository.findByName("ADMINISTRATOR").get();
            roleList.add(dbRole);
            user.setRoles(roleList);
            this.userRepository.save(user);
        }

    }
}
