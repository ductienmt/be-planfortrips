package com.be_planfortrips.configs;

import com.be_planfortrips.security.jwt.JwtAccessDenied;
import com.be_planfortrips.security.jwt.JwtEntryPoint;
import com.be_planfortrips.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${plan_for_trip.env}")
    private String env;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Autowired
    private JwtAccessDenied jwtAccessDenied;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtTokenFilter jwtFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList((env.equals("production")) ? "https://plan-for-trips.netlify.app/": "http://localhost:5050"));
        configuration.setAllowedOrigins(Arrays.asList((env.equals("production")) ? "https://plan-for-trips.netlify.app/": "http://localhost:5050"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(API_Provider.PUBLIC_API).permitAll()
                        .requestMatchers(API_Provider.ADMIN_API).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(API_Provider.USER_API).hasAuthority("ROLE_USER")
                        .requestMatchers(API_Provider.ENTERPRISE_API).hasAuthority("ROLE_ENTERPRISE")
                        .requestMatchers(API_Provider.ADMIN_USER_ENTERPRISE_API).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_ENTERPRISE")
                        .requestMatchers(API_Provider.USER_ADMIN_API).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(API_Provider.USER_ENTERPRISE_API).hasAnyAuthority("ROLE_USER", "ROLE_ENTERPRISE")
                        .requestMatchers(API_Provider.ADMIN_ENTERPRISE_API).hasAnyAuthority("ROLE_ADMIN", "ROLE_ENTERPRISE")
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtEntryPoint)
                        .accessDeniedHandler(jwtAccessDenied))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
