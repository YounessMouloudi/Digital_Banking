package com.projects.digital_bank_backend.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.projects.digital_bank_backend.services.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /* had l'annotation Value tandiroha pour récupérer un paramétre d'une variable d'environnement dans
    application.properties w tandiroha wsst ${} pour l'injecter dans la variable ici */
    @Value("${jwt.secretK}")
    private String secretKey;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
//
//        PasswordEncoder passwordEncoder = passwordEncoder();
//
//        return new InMemoryUserDetailsManager(
//                User.withUsername("user1").password(passwordEncoder.encode("aaaa")).authorities("USER").build(),
//                User.withUsername("admin").password(passwordEncoder.encode("aaaa")).authorities("USER","ADMIN").build()
//        );
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                /* had SessionCreationPolicy.STATELESS càd tat3ni maghadich ngérer la session en coté serveur
                ay ghadi ndiro authentification stateless en utilisant un Token JWT */
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /* hna ila darna authentification stateless darori khassna ndésactiviw la protection CSRF */
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login/**").permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .userDetailsService(userDetailServiceImpl)
                /* had httpBasic c'est une authentification basic de http
                .httpBasic(Customizer.withDefaults()) */
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    /* hna had jwtEncoder bach ghadi ngéneriw un Token a partir d 'un secret Key privée had secret key
    nta li tadiro kima bghiti lmhm howa ykon fih 64 caractéres */
    JwtEncoder jwtEncoder() {
//        String secretKey = "RMD7r9a10j31A08bi2p0z2q4Wna7ze5rk6y9Ch11sd8x4ogm7t0afr6iq3ue9Ivl";
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey.getBytes()));
    }

    @Bean
    /* jwtDecoder a chaque fois il reçois une requete il va vers http il cherche header authorizations il
    récupere token jwt aprés il vérifie la signature de Token en utilisant le secretKey  */
    JwtDecoder jwtDecoder() {
//        String secretKey = "RMD7r9a10j31A08bi2p0z2q4Wna7ze5rk6y9Ch11sd8x4ogm7t90afr6iq3ueIvl";
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(),"RSA");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        return new ProviderManager(daoAuthenticationProvider);
//    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailServiceImpl userDetailServiceImpl) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailServiceImpl);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    /* mnin tankhadmo b spring sécurity darori tandiro had l filter pour autoriser le cors hit wakha kona
       darna f les controllers d customer et account hadi "@CrossOrigin("http://localhost:4200/")" mnin
       tandiro spring sécurity matatbe9ach khdama dik l'annotation darori khass had corsConfigurationSource
       bach t autoriser l'Origin ,Methode et Header
    */
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        // hadi pour autoriser le client ou javascript lire le headers de l'app
        // corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
}
