package jp.co.axa.apidemo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${jwt.token.secret:1593170fd173bb64fe7c62f1debbe428}")
    private String jwtSecret;
    @Value("${jwt.token.expiration.time: 86400000}") // 1 day in miliseconds;
    private long jwtExpiredTime;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator(this.jwtSecret, this.jwtExpiredTime);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(),tokenGenerator() ))
                .addFilter(new AuthorizationFilter(authenticationManager(),jwtSecret))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(encoder().encode("user1Pass")).roles("USER")
                .and()
                .withUser("user2").password(encoder().encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
                .and().passwordEncoder(encoder());
    }
}

