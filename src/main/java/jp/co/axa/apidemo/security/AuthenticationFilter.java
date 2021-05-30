package jp.co.axa.apidemo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jp.co.axa.apidemo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.function.Supplier;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private TokenGenerator tokenGenerator;
    public AuthenticationFilter(AuthenticationManager authenticationManager,TokenGenerator tokenGenerator){
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(),User.class);
            return  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword(), Collections.emptyList()));
        } catch (IOException e) {
            throw new RuntimeException("Can not read user data");
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String subject = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        response.addHeader("Authorization","Bearer " + this.tokenGenerator.generateToken(subject));
    }
}
