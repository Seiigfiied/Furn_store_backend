package com.chesnakov.hardwareecommerce;

import com.chesnakov.hardwareecommerce.dao.RoleDao;
import com.chesnakov.hardwareecommerce.dao.UserDao;
import com.chesnakov.hardwareecommerce.entity.Role;
import com.chesnakov.hardwareecommerce.entity.User;
import com.chesnakov.hardwareecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private RoleDao roleDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterNewUser() {
        Role role = new Role("User", "Default role for newly created record");
        when(roleDao.findById("User")).thenReturn(java.util.Optional.of(role));

        User user = new User();
        user.setUserName("john.doe");
        user.setUserFirstName("John");
        user.setUserLastName("Doe");
        user.setUserPassword("password");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(userDao.save(user)).thenReturn(user);

        userService.registerNewUser(user);

        verify(roleDao, times(1)).findById("User");
        verify(userDao, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testGetEncodedPassword() {
        String password = "testPassword";
        String encodedPassword = "encodedTestPassword";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        String result = userService.getEncodedPassword(password);

        assertEquals(encodedPassword, result);
        verify(passwordEncoder).encode(password);
    }
}