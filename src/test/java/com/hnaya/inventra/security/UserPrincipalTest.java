package com.hnaya.inventra.security;

import com.hnaya.inventra.entity.User;
import com.hnaya.inventra.entity.Warehouse;
import com.hnaya.inventra.entity.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserPrincipalTest {

    @Test
    @DisplayName("Should create UserPrincipal correctly from User entity")
    void create_ShouldMapAllFields() {
        // Arrange
        Warehouse warehouse = new Warehouse();
        warehouse.setId(100L);

        User user = new User();
        user.setId(1L);
        user.setUsername("john.doe");
        user.setPassword("secret");
        user.setName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setRole(Role.ADMIN);
        user.setActive(true);
        user.setAssignedWarehouse(warehouse);

        UserPrincipal principal = UserPrincipal.create(user);

        assertEquals(1L, principal.getId());
        assertEquals("john.doe", principal.getUsername());
        assertEquals("secret", principal.getPassword());
        assertEquals("John", principal.getName());
        assertEquals(100L, principal.getWarehouseId());
        assertTrue(principal.isEnabled());
        assertTrue(principal.isAccountNonExpired());
        assertTrue(principal.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should handle null warehouse during creation")
    void create_ShouldHandleNullWarehouse() {
        User user = new User();
        user.setAssignedWarehouse(null);

        UserPrincipal principal = UserPrincipal.create(user);

        assertNull(principal.getWarehouseId());
    }

    @Test
    @DisplayName("Should return correct authorities based on role")
    void getAuthorities_ShouldReturnRoleWithPrefix() {
        UserPrincipal principal = new UserPrincipal(1L, "u", "p", "n", "l", "e", Role.ADMIN, true, null);

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }
}