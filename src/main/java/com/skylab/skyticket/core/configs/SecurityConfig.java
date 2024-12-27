package com.skylab.skyticket.core.configs;

import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.core.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x ->
                        x
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                // Auth endpoints
                                .requestMatchers("/api/auth/login").permitAll()
                                .requestMatchers("/api/auth/register").permitAll()

                                // Event endpoints
                                .requestMatchers("/api/events/addEvent").hasAnyRole("ADMIN")
                                .requestMatchers("/api/events/getEventById/**").hasAnyRole("ADMIN")

                                // Event day endpoints
                                .requestMatchers("/api/event-days/addEventDay/**").hasAnyRole("ADMIN")
                                .requestMatchers("/api/event-days/getEventDayById/**").hasAnyRole("ADMIN")
                                .requestMatchers("/api/event-days/getEventDaysByEventId/**").hasAnyRole("ADMIN")

                                // Session endpoints
                                .requestMatchers("/api/sessions/addSession/**").hasAnyRole("ADMIN")

                                // Ticket endpoints
                                .requestMatchers("/api/tickets/addTicket").permitAll()
                                .requestMatchers("/api/tickets/getTicketById/**").permitAll()
                                .requestMatchers("/api/tickets/submitTicket/**").permitAll()

                                // User endpoints
                                .requestMatchers("/api/users/getUserById/**").hasAnyRole("ADMIN")
                                .requestMatchers("/api/users/getUserByEmail/**").hasAnyRole("ADMIN")
                                .requestMatchers("/api/users/getUserByPhoneNumber/**").hasAnyRole("ADMIN")

                                .anyRequest().authenticated()

                )
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

