package br.edu.ifpb.pweb2.projeto.simpleevents.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("customUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public RefererRedirectionAuthenticationSuccessHandler successHandler() {
        return new RefererRedirectionAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(permitedList()).permitAll().and()
                .authorizeRequests().antMatchers(anonymousList()).access("isAnonymous()").and()
                .authorizeRequests().antMatchers(authenticatedList()).access("isAuthenticated()")
                .and().formLogin().loginPage("/login").successHandler(successHandler()).and().logout()
                .deleteCookies("remember-me").and().rememberMe().tokenValiditySeconds(1800);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
    }

    private String[] permitedList(){
        return new String[]{
            "/", 
            "/js/**", 
            "/css/**", 
            "/images/**", 
            "/resources/**", 
            "/events/{id}",
            "/events"
        };
    }

    private String[] anonymousList(){
        return new String[]{
            "/signup",
             "/login"
        };
    }

    private String[] authenticatedList(){
        return new String[]{
            "/logout", 
            "/especialidades/**", 
            "/my-events", 
            "/events/**"
        };
    }

    @Bean
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }
}
