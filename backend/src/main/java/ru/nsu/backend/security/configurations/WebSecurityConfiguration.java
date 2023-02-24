package ru.nsu.backend.security.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.nsu.backend.security.role.Roles;

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
//        http.csrf().disable()
//                .authorizeHttpRequests()
//                .antMatchers("**/root/**")
//                .hasRole(StaticRoles.ROOT)
//                .antMatchers("**/admin/**")
//                .authenticated()
//                .anyRequest()
//                .hasAnyRole(StaticRoles.ADMIN, StaticRoles.ROOT)
////                .permitAll()
//                .and()
//                .authenticationProvider(authenticationProvider)
//                .httpBasic(withDefaults())
//                .sessionManagement()
//                .sessionCreationPolicy(STATELESS);
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/auth")
                .permitAll()
                .antMatchers("/accounts/roles")
                .hasAnyRole(Roles.ROOT_, Roles.ADMIN_)
                .antMatchers("**/admin/**")
                .hasAnyRole(Roles.ADMIN_)
                .antMatchers("**/admin")
                .hasAnyRole(Roles.ADMIN_)
                .antMatchers("**/user/**")
                .hasAnyRole(Roles.USER_)
                .antMatchers("**/user")
                .hasAnyRole(Roles.USER_)
                .antMatchers("**/root/**")
                .hasAnyRole(Roles.ROOT_)
                .antMatchers("**/root")
                .hasAnyRole(Roles.ROOT_)
                .anyRequest()
                .hasAnyRole(Roles.ROOT_)
//                .permitAll()
                .and()
                .authenticationProvider(authenticationProvider)
                .httpBasic(withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
        ;

        return http.build();
    }
}