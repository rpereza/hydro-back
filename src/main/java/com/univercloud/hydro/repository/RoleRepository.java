package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find role by name
     */
    Optional<Role> findByName(String name);
    
    /**
     * Check if role exists by name
     */
    boolean existsByName(String name);
    
    /**
     * Find roles by name containing (case-insensitive)
     */
    java.util.List<Role> findByNameContainingIgnoreCase(String name);
}
