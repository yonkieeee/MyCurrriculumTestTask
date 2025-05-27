package org.example.mycurrriculumtesttask.controllers.userControllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mycurrriculumtesttask.dto.AuthResponseDto;
import org.example.mycurrriculumtesttask.dto.LoginDto;
import org.example.mycurrriculumtesttask.dto.RegisterDto;
import org.example.mycurrriculumtesttask.models.user.Role;
import org.example.mycurrriculumtesttask.models.user.UserEntity;
import org.example.mycurrriculumtesttask.repository.userReposiories.RoleRepository;
import org.example.mycurrriculumtesttask.repository.userReposiories.UserRepository;
import org.example.mycurrriculumtesttask.services.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto, HttpServletResponse response) {
        try{
            if(userRepository.existsByUsername(registerDto.username())){
                return ResponseEntity.badRequest().body("Username is already in use");
            }

            UserEntity user = new UserEntity();

            user.setUsername(registerDto.username());
            user.setPassword(passwordEncoder.encode(registerDto.password()));

            Role roles = roleRepository.findByName("USER").get();

            user.setRoles(Collections.singletonList(roles));

            userRepository.save(user);

            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(registerDto.username()
                                    , registerDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtil.generateToken(authentication, false);
            String refreshToken = jwtUtil.generateToken(authentication, true);

            Cookie cookie = new Cookie("refresh_token", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(907200);

            response.addCookie(cookie);

            return ResponseEntity.ok(new AuthResponseDto(accessToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto,
                                   HttpServletResponse response) {
        try{
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.username()
                                    , loginDto.password()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtUtil.generateToken(authentication, false);
            String refreshToken = jwtUtil.generateToken(authentication, true);

            Cookie cookie = new Cookie("refresh_token", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(907200);

            response.addCookie(cookie);

            return ResponseEntity.ok(new AuthResponseDto(accessToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
