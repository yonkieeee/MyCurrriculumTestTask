package org.example.mycurrriculumtesttask.controllers.userControllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mycurrriculumtesttask.services.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
public class JwtController {
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/isValid")
    public ResponseEntity<Boolean> isValidToken(Authentication authentication) {
        try{
            boolean isValid = jwtUtil.validateToken((String) authentication.getDetails());

            return ResponseEntity.ok(isValid);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(
            @CookieValue(value = "refresh_token", defaultValue = "") String refreshToken
            , HttpServletResponse response) {
        if(jwtUtil.validateToken(refreshToken)) {
            String newAccessToken = jwtUtil.generateAccessTokenFromRefreshToken(refreshToken);

            String newRefreshToken = jwtUtil.generateRefreshTokenFromAccessToken(newAccessToken);

            Cookie cookie = new Cookie("refresh_token", newRefreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(907200);

            return ResponseEntity.ok(newAccessToken);
        }else{
            return ResponseEntity.badRequest().body("Invalid refresh token");
        }
    }
}
