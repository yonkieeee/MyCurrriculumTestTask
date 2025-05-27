package org.example.mycurrriculumtesttask.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.example.mycurrriculumtesttask.repository.userReposiories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    private final static String JWT_SECRET = "v3ryS3cr3tKeyThatIsLongEnoughToBeSecure!!";

    private static final byte[] SECRET_BYTES = JWT_SECRET.getBytes(StandardCharsets.UTF_8);

    private final static int ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60;

    private final static int REFRESH_TOKEN_EXPIRATION = 907200;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(Authentication authentication, boolean refresh_token) {
        String fullName = authentication.getName();
        UserEntity user = userRepository.findByUsername(fullName).orElseThrow();
        long userId = user.getId();
        Date currentDate = new Date();
        String roleWithPrefix = authentication.getAuthorities().iterator().next().getAuthority();
        String role = roleWithPrefix.startsWith("ROLE_") ? roleWithPrefix.substring(5) : roleWithPrefix;

        int expiredTime = refresh_token ? REFRESH_TOKEN_EXPIRATION : ACCESS_TOKEN_EXPIRATION;
        Date expirationDate = new Date(currentDate.getTime() + expiredTime);

        String token = Jwts.builder()
                .setSubject(fullName)
                .claim("role", role)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(SECRET_BYTES), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        String fullName = getUsernameFromJwt(refreshToken);
        int userId = getIdFromJwt(refreshToken);
        String role = getRoleFromJwt(refreshToken);

        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(fullName)
                .claim("role", role)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(SECRET_BYTES), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public String generateRefreshTokenFromAccessToken(String accessToken) {
        String username = getUsernameFromJwt(accessToken);
        int userId = getIdFromJwt(accessToken);
        String role = getRoleFromJwt(accessToken);

        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + REFRESH_TOKEN_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(SECRET_BYTES), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_BYTES))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public int getIdFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_BYTES))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("id", Integer.class);
    }

    public String getRoleFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_BYTES))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(SECRET_BYTES))
                    .build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }
}
