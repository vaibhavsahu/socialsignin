package com.vaibhav.example.socialsignin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@SpringBootApplication
@RestController
public class SocialsigninApplication extends WebSecurityConfigurerAdapter {

    @RequestMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User oAuth2User){
        return Collections.singletonMap("name", oAuth2User.getAttribute("name"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests( a ->
                a.antMatchers("/", "/error", "/webjars/**")
                .permitAll().anyRequest().authenticated()
        )
                .logout(
                        l ->  l.logoutSuccessUrl("/").permitAll()
                )
                .csrf(
                        c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .exceptionHandling( e -> e.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                ))
                .oauth2Login();
    }

    public static void main(String[] args) {
        SpringApplication.run(SocialsigninApplication.class, args);
    }

}
