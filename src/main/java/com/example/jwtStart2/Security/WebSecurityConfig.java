package com.example.jwtStart2.Security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // ToDo: 1- define add filter before authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                // We filter the api/login requests
                //Here we create token if not found and validate user if authenticated or not
                // The key point here we create class called JWTLogin filter and passed to it login url and credential made by user (authenticationManager)
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                // And filter other requests to check the presence of JWT in header
                //Here we validate if the user is login by token value we parse it and validate it
                .addFilterBefore(new JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }


    // please note here keep in memory user name and password for test
    //nothing should change here from the application
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Create a default account
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("admin")
//                .password("password")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN");
    }
}