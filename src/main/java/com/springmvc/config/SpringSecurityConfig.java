package com.springmvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccess loginSuccess;

    @Autowired
    private LogoutSuccess logoutSuccess;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder
                .inMemoryAuthentication()
                .withUser("user").password("pass").roles("USER").and()
                .withUser("admin").password("pass").roles("ADMIN");

        managerBuilder.jdbcAuthentication().dataSource(dataSource)
                .withUser("user").password("pass").roles("USER").and()
                .withUser("user1").password("pass2").roles("ADMIN");

        managerBuilder
                .userDetailsService(userDetailsService);
    }*/

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/user").hasAuthority("ROLE_USER")
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
                .antMatchers("/register/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .successHandler(loginSuccess)
                .permitAll()
                .loginPage("/login")
                .loginProcessingUrl("/loginUrl")

                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccess)
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))

                .and()
                .rememberMe()
                .tokenValiditySeconds(3600)
                .rememberMeCookieName("rememberMeCookie")
                .rememberMeParameter("rememberMe")
                .tokenRepository(persistentTokenRepository())

                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }
}
