package by.itacademy.userservice.config;

import by.itacademy.userservice.endponts.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter filter) throws Exception  {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/users/registration").permitAll()
                                .requestMatchers("/users/login").permitAll()
                                .requestMatchers("/users/verification").permitAll()
                                .requestMatchers("/users/me").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/users/general").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/users/**").hasAnyRole("ADMIN")
                                .requestMatchers("/users").hasAnyRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.authenticationEntryPoint((request, response, ex) ->
                                        response.setStatus(
                                                HttpServletResponse.SC_UNAUTHORIZED
                                        ))
                                .accessDeniedHandler((request, response, ex) ->
                                        response.setStatus(
                                                HttpServletResponse.SC_FORBIDDEN
                                        ))
                )
                .addFilterBefore(
                        filter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}