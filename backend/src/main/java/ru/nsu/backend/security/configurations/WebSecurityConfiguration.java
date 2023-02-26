package ru.nsu.backend.security.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nsu.backend.security.configurations.filter.CustomAuthFilter;
import ru.nsu.backend.security.configurations.filter.CustomAuthorizationFilter;
import ru.nsu.backend.security.role.Roles;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfiguration //implements WebSecurityConfigurerAdapter
{
    private final AccountAuthenticationProvider authenticationProvider;

    @Value("${security}")
    private String security;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (security.equals("token")){
            CustomAuthFilter filter = new CustomAuthFilter(authenticationProvider);
            filter.setFilterProcessesUrl("/login");
            http.csrf().disable();
            http.authorizeHttpRequests()

                    .antMatchers("/**/root/**")
                    .hasRole(Roles._ROOT)
                    .antMatchers("/**/admin/**")
                    .hasRole(Roles._ADMIN)
                    .antMatchers("/**/user/**")
                    .hasRole(Roles._USER)
                    .antMatchers("/**").hasRole(Roles._USER);
            http
//                .antMatcher("/log")
//                .authenticationProvider(authenticationProvider)
//                .httpBasic(withDefaults())
                    .sessionManagement()
                    .sessionCreationPolicy(STATELESS);
            http.addFilter(filter);
            http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        }
        else if (security.equals("no")){
            http.csrf().disable().sessionManagement()
                    .sessionCreationPolicy(STATELESS);
            http.authorizeRequests().antMatchers("/**").permitAll();
        }
        else {
            http.csrf().disable();
            http.authorizeRequests().antMatchers("/**").hasRole("USER").and().httpBasic();
            http.sessionManagement()
                    .sessionCreationPolicy(STATELESS);
        }


        return http.build();
    }


}