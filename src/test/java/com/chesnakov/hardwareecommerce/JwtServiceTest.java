package com.chesnakov.hardwareecommerce;

import com.chesnakov.hardwareecommerce.dao.UserDao;
import com.chesnakov.hardwareecommerce.entity.*;
import com.chesnakov.hardwareecommerce.service.JwtService;
import com.chesnakov.hardwareecommerce.util.JwtUtil;
import com.chesnakov.hardwareecommerce.entity.JwtRequest;
import com.chesnakov.hardwareecommerce.entity.JwtResponse;
import com.chesnakov.hardwareecommerce.entity.Role;
import com.chesnakov.hardwareecommerce.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class JwtServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDao userDao;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testCreateJwtToken() throws Exception {
        String userName = "testUser";
        String userPassword = "testPassword";
        String generatedToken = "generatedToken";

        JwtRequest jwtRequest = new JwtRequest(userName, userPassword);

        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setRoles(Collections.singleton(new Role("User")));

        Mockito.when(userDao.findById(userName)).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.generateToken(Mockito.any(UserDetails.class))).thenReturn(generatedToken);

        JwtResponse jwtResponse = jwtService.createJwtToken(jwtRequest);

        assertNotNull(jwtResponse);
        assertEquals(userName, jwtResponse.getUser().getUserName());
        assertEquals(generatedToken, jwtResponse.getJwtToken());
    }

    @Test
    public void testLoadUserByUsername() {
        String userName = "testUser";
        String userPassword = "testPassword";

        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setRoles(Collections.singleton(new Role("User")));

        Mockito.when(userDao.findById(userName)).thenReturn(Optional.of(user));

        UserDetails userDetails = jwtService.loadUserByUsername(userName);

        assertNotNull(userDetails);
        assertEquals(userName, userDetails.getUsername());
        assertEquals(userPassword, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_User")));
    }

}