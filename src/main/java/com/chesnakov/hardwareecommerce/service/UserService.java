package com.chesnakov.hardwareecommerce.service;

import com.chesnakov.hardwareecommerce.dao.RoleDao;
import com.chesnakov.hardwareecommerce.dao.UserDao;
import com.chesnakov.hardwareecommerce.entity.Role;
import com.chesnakov.hardwareecommerce.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(User user) {
        Role role = roleDao.findById("User").get();

        Set<Role> roles = new HashSet<>();

        roles.add(role);

        user.setRoles(roles);

        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        return userDao.save(user);
    }


    public void initRolesAndUser() {

        Role adminRole = new Role("Admin", "Admin role");
        roleDao.save(adminRole);

        Role userRole = new Role("User", "Default role for newly created record");
        roleDao.save(userRole);

        User adminUser = new User();

        adminUser.setUserName("admin123");
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        adminUser.setRoles(adminRoles);
        userDao.save(adminUser);


        User user = new User();

        user.setUserName("user123");
        user.setUserFirstName("user");
        user.setUserLastName("user");
        user.setUserPassword(getEncodedPassword("user@pass"));

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        user.setRoles(userRoles);
        userDao.save(user);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
