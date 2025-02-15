package org.example.service;

import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testLoadUserByUsernameWithAdminRole() {
        String username = "test user";
        org.example.entity.UserDetails userEntity = new org.example.entity.UserDetails();
        userEntity.setUserName(username);
        userEntity.setPassword("password");
        userEntity.setAdmin(true);
        when(userRepository.findByUserName(username)).thenReturn(userEntity);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsernameWithUserRole() {
        String username = "test user";
        org.example.entity.UserDetails userEntity = new org.example.entity.UserDetails();
        userEntity.setUserName(username);
        userEntity.setPassword("password");
        userEntity.setAdmin(false);
        when(userRepository.findByUserName(username)).thenReturn(userEntity);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsernameWithUserNotFound() {
        String username = "test user";
        when(userRepository.findByUserName(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }
}