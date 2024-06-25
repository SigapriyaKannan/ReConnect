package com.dal.asdc.reconnect;
import com.dal.asdc.reconnect.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import java.security.Key;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String secretKey = "649d9d23fbb80c52a706fe67c673511974e57906363f4f0401721e99ec05c231";
    private final long jwtExpiration = 3600000;

    @BeforeEach
    public void setUp() throws Exception
    {
        java.lang.reflect.Field secretKeyField = JWTService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, secretKey);
        java.lang.reflect.Field jwtExpirationField = JWTService.class.getDeclaredField("jwtExpiration");
        jwtExpirationField.setAccessible(true);
        jwtExpirationField.set(jwtService, jwtExpiration);
        when(userDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    public void testGenerateToken()
    {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignInKey(secretKey)).build().parseClaimsJws(token).getBody();
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    public void testExtractUsername()
    {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    public void testIsTokenValid()
    {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    private Key getSignInKey(String secretKey)
    {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
