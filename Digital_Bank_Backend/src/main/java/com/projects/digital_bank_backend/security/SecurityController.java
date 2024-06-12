package com.projects.digital_bank_backend.security;

import com.projects.digital_bank_backend.entities.User;
import com.projects.digital_bank_backend.exceptions.UserNotFoundException;
import com.projects.digital_bank_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    // pour authentifier un user Spring utilisé un obj qui s'appelle AuthenticationManager
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @GetMapping("/profile")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

    // la methode de l'authentification
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(String email,String password) {

        try {

            User user = userService.getUser(email);

            if(user == null) throw new UserNotFoundException("User Not Found with the specified email");

//            System.out.println(user);

            if(!passwordEncoder.matches(password,user.getPassword())){
                throw new RuntimeException("Invalid password");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email,password)
            );

            Instant instant = Instant.now();

        /* hna ghadi njibo ga3 les roles li 3andna w drna lihom dik joining b espace ay bach yfara9 binathom
        b espace ay ghadi nrajj3o type String w machi list w t9dar traj3hom list labghiti

        hna f map ima dir haka : .map(a -> a.getAuthority()) aw b7al li darna */
//        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

//            System.out.println(scope);
//            System.out.println(scope.equals("USER"));

            // had JwtClaimSet howa bach ghadi ncréyiw le token w ghadi n7adedo lih bzf d lkhassiat
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant) // Hadi la date de géneration de token
                    .expiresAt(instant.plus(2, ChronoUnit.MINUTES))
                    // Hadi la date d'expiration de token ay aprés 20 min de la date de création
                    .subject(user.getUsername())
                    .claim("email",user.getEmail())
                    .claim("scope",scope)
                    .build();

            System.out.println(jwtClaimsSet.getExpiresAt());
            System.out.println(instant);

            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS512).build(),jwtClaimsSet
            );

            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

            return ResponseEntity.ok(Map.of("access-token", jwt));

        } catch (UserNotFoundException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", e.getMessage()));
        }
    }
}
