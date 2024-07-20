package com.dal.asdc.reconnect.service;

import com.dal.asdc.reconnect.model.Users;
import com.dal.asdc.reconnect.repository.UserDetailsRepository;
import com.dal.asdc.reconnect.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JWTService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails)
    {
        Optional<Users> user = usersRepository.findByUserEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Users currentUser = user.get();
        int userType = currentUser.getUserType().getTypeID();

        com.dal.asdc.reconnect.model.UserDetails userDetailsEntity = userDetailsRepository.findById(currentUser.getUserDetails().getDetailId())
                .orElseThrow(() -> new RuntimeException("UserDetails not found"));

        extraClaims.put("email", user.get().getUserEmail());
        extraClaims.put("userType", userType);
        extraClaims.put("userID", user.get().getUserID());
        extraClaims.put("userName", userDetails.getUsername());
        extraClaims.put("profile", userDetailsEntity.getProfilePicture());
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public int extractUserType(String token) {
        Claims claims = extractAllClaims(token);
        return (int) claims.get("userType");
    }

    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("email");
    }

    public int extractID(String token) {
        Claims claims = extractAllClaims(token);
        return (int) claims.get("userID");
    }
}
