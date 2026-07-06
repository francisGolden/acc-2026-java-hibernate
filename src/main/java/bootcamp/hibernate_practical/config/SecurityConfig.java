package bootcamp.hibernate_practical.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(GET, "/api/books/**").authenticated()
                        .requestMatchers(POST, "/api/books").hasRole("ADMIN")
                        .requestMatchers(PUT, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/api/books/**").hasRole("ADMIN")
                        .anyRequest().denyAll()).httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
