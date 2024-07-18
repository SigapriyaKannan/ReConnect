package com.dal.asdc.reconnect;
import com.dal.asdc.reconnect.model.UserType;
import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UsersRepository;
import com.dal.asdc.reconnect.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JWTServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private JWTService jwtService;

    private final long jwtExpiration = 3600000; // 1 hour

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "secretKey", "649d9d23fbb80c52a706fe67c673511974e57906363f4f0401721e99ec05c231");
    }

    @Test
    void testGenerateToken() {
        // Mock user data
        Users user = new Users();
        user.setUserEmail("test@example.com");
        UserType userType = new UserType();
        userType.setTypeID(1);
        user.setUserType(userType);
        when(usersRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));

        Collection<? extends SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("test@example.com", "password", authorities);

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testExtractUsername() {
        String token = createSampleToken();

        String username = jwtService.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void testExtractEmail() {
        String token = createSampleToken();

        String email = jwtService.extractEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void testExtractUserType() {
        String token = createSampleToken();

        int userType = jwtService.extractUserType(token);

        assertEquals(1, userType);
    }


    private String createSampleToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "test@example.com");
        claims.put("userType", 1);

        Users user = new Users();
        user.setUserEmail("test@example.com");

        return jwtService.buildToken(claims,user,jwtExpiration);
    }

    private String createExpiredSampleToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "test@example.com");
        claims.put("userType", 1);

        Users user = new Users();
        user.setUserEmail("test@example.com");

        return jwtService.buildToken(claims,user,-1000);
    }
}
