package com.univercloud.hydro.config;

import com.univercloud.hydro.entity.Role;
import com.univercloud.hydro.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }
    
    private void initializeRoles() {
        // Create default roles if they don't exist
        createRoleIfNotExists("USER", "Default user role");
        createRoleIfNotExists("MODERATOR", "Moderator role with elevated privileges");
        createRoleIfNotExists("ADMIN", "Administrator role with full access");
        createRoleIfNotExists("SUPER_ADMIN", "Super Administrator role with full access");
        
        logger.info("Default roles initialized successfully");
    }
    
    private void createRoleIfNotExists(String roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName, description);
            roleRepository.save(role);
            logger.info("Created role: {}", roleName);
        } else {
            logger.debug("Role already exists: {}", roleName);
        }
    }
}
