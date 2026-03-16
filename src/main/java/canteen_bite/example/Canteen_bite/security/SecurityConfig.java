package canteen_bite.example.Canteen_bite.security;

import canteen_bite.example.Canteen_bite.entity.Role;
import canteen_bite.example.Canteen_bite.service.AuthUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
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

        http.cors(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers("/error").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/menu/**").permitAll()

                .requestMatchers("/api/admin/**")
                .hasRole(Role.ADMIN.name())

                .requestMatchers("/api/kitchen/**")
                .hasAnyRole(Role.KITCHEN_STAFF.name(), Role.ADMIN.name())

                .anyRequest().authenticated()
        );

        http.addFilterBefore(
                jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}