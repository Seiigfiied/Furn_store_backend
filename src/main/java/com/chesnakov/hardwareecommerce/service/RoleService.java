package com.chesnakov.hardwareecommerce.service;

import com.chesnakov.hardwareecommerce.dao.RoleDao;
import com.chesnakov.hardwareecommerce.entity.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role createNewRole(Role role){
        return roleDao.save(role);
    }
}
