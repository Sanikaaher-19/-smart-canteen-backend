package canteen_bite.example.Canteen_bite.security;

import canteen_bite.example.Canteen_bite.entity.Role;
import canteen_bite.example.Canteen_bite.service.AuthUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity  // Enables method-level security for @PreAuthorize
public class SecurityConfig {
    private final JwtService jwtService;
    private final AuthUserService authUserService;

    public SecurityConfig(JwtService jwtService, AuthUserService authUserService) {
        this.jwtService = jwtService;
        this.authUserService = authUserService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, authUserService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors()  // Enable CORS
            .and()
            .csrf().disable()  // Disable CSRF since you are stateless with JWT
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session for JWT
            .authorizeHttpRequests(auth -> auth
                // Permit open access to authentication endpoints, errors, Swagger docs, and menu GET
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/menu/**").permitAll()

                // Restrict /api/admin/** only to users with ADMIN role
                .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())

                // Restrict /api/kitchen/** to STAFF (or KITCHEN_STAFF) and ADMIN
                .requestMatchers("/api/kitchen/**").hasAnyRole(Role.KITCHEN_STAFF.name(), Role.ADMIN.name())

                // All other requests require authentication
                .anyRequest().authenticated()
            )
            // Add JWT filter before the UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
