package com.hnaya.inventra.security;

import com.hnaya.inventra.entity.User;
import com.hnaya.inventra.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_whenUserExists_returnsUserDetails() {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("john");


        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        verify(userRepository).findByUsername("john");
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_throwsException() {
        when(userRepository.findByUsername("ghost"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("ghost")
        );

        verify(userRepository).findByUsername("ghost");
    }
}
