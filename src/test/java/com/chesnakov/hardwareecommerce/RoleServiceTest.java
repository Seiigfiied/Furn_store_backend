package com.chesnakov.hardwareecommerce;

import com.chesnakov.hardwareecommerce.configuration.JwtRequestFilter;
import com.chesnakov.hardwareecommerce.dao.RoleDao;
import com.chesnakov.hardwareecommerce.entity.Role;
import com.chesnakov.hardwareecommerce.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        JwtRequestFilter.CURRENT_USER = "jsmith";
    }

    @Test
    public void testCreateNewRole() {
        Role role = new Role();
        role.setRoleName("testRole");

        when(roleDao.save(any(Role.class))).thenReturn(role);

        Role createdRole = roleService.createNewRole(role);

        verify(roleDao, times(1)).save(any(Role.class));
        assertNotNull(createdRole);
        assertEquals(role.getRoleName(), createdRole.getRoleName());
    }
}