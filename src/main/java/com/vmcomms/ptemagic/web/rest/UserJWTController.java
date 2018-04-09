package com.vmcomms.ptemagic.web.rest;

import com.vmcomms.ptemagic.security.jwt.JWTConfigurer;
import com.vmcomms.ptemagic.security.jwt.TokenProvider;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;
import com.vmcomms.ptemagic.web.rest.vm.LoginVM;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.http.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

    	// Username = Email
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        if (authentication != null && StringUtils.equals(loginVM.getRole(), "ROLE_ADMIN")) {
        	// Check role
        	SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
        	
        	if (authentication.getAuthorities().contains(adminRole)) {
        		// OK
        	} else {
        		throw new BadRequestAlertException("User not Admin role", "user", "not_permission");
        	}
        } else {
        	// Check role
        	SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("ROLE_ADMIN");
        	
        	if (authentication.getAuthorities().contains(adminRole)) {
        		throw new BadRequestAlertException("User not Admin role", "user", "not_permission");
        	}
        }
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
