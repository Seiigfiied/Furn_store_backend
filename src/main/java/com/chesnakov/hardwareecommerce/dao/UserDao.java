package com.chesnakov.hardwareecommerce.dao;

import com.chesnakov.hardwareecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, String> {
}
