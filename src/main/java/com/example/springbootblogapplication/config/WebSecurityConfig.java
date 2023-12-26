package com.example.springbootblogapplication.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(auth -> {
                    auth
                            .requestMatchers(antMatcher("/css/**")).permitAll()
                            .requestMatchers(antMatcher("/js/**")).permitAll()
                            .requestMatchers(antMatcher("/images/**")).permitAll()
                            .requestMatchers(antMatcher("/fonts/**")).permitAll()
                            .requestMatchers(antMatcher("/webjars/**")).permitAll()
                            .requestMatchers(antMatcher("/")).permitAll()
                            .requestMatchers(antMatcher("/rss/**")).permitAll()
                            .requestMatchers(antMatcher("/register/**")).permitAll()
                            .requestMatchers(antMatcher("/posts/**")).permitAll()
                            .requestMatchers(antMatcher("/profile/**")).permitAll()
                            .requestMatchers(antMatcher("/posts/**/comments/**")).authenticated()
                            .anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}