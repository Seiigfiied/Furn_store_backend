    package com.chesnakov.hardwareecommerce.dao;

    import com.chesnakov.hardwareecommerce.entity.Role;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface RoleDao extends JpaRepository<Role, String> {
    }
