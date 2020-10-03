package com.tensquare.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //In this project, we only use Spring Security's PasswordEncoder, not authorization.
        //Instead, JWT is used for authorization
        //So here, we let go all the traffic
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()//no authorization is needed
                .anyRequest().authenticated() // needs authentication
                .and().csrf().disable(); //disable CSRF
    }
}
