package ru.nsu.backend.security.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.nsu.backend.security.role.StaticRoles;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfiguration {
    private final AccountAuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers(POST, "/api/**")
                .permitAll()
                .antMatchers("/hello")
                .authenticated()
                .anyRequest()
                .hasAnyRole(StaticRoles.ADMIN, StaticRoles.SUPER_ADMIN)
//                .permitAll()
                .and()
                .authenticationProvider(authenticationProvider)
                .httpBasic(withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);
        return http.build();
    }
}