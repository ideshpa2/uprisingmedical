// src/main/java/com/example/demo/SecurityConfig.java
package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
CorsConfigurationSource corsConfigurationSource() {
  CorsConfiguration cfg = new CorsConfiguration();
  cfg.setAllowedOrigins(List.of(
      "https://uprisingmedical.github.io",   // GitHub Pages
      "http://localhost:8080"                // dev (remove if not needed)
  ));
  cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
  cfg.setAllowedHeaders(List.of("*"));
  cfg.setAllowCredentials(false);            // set true only if you use cookies
  UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
  src.registerCorsConfiguration("/**", cfg);
  return src;
}

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/**", "/h2-console/**", "/teams/**", "/problems/**").permitAll()
        .anyRequest().permitAll()
      )
      .headers(headers -> headers.frameOptions(frame -> frame.disable()))
      .formLogin(form -> form.disable())
      .httpBasic(basic -> basic.disable());
    return http.build();
  }
}
