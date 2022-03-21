package com.axonactive.jpa.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.axonactive.jpa.entities.Token;
import com.axonactive.jpa.entities.User;
import com.axonactive.jpa.helper.AppConfigService;
import com.axonactive.jpa.service.impl.UserServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

public class JWTAuthenticationServices {

    @Inject
    UserServiceImpl userService;

    public Token createToken(User user) {
        validateUser(user);

        String token = null;
        String secretKey = AppConfigService.getSecretKey();
        String issuer = AppConfigService.getIssuer();
        int timeToLive = AppConfigService.getTimeToLive();
        try{
            long millis = System.currentTimeMillis();
            System.out.println(new Date(millis + 1000000));
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            token = JWT.create()
                    .withIssuer(issuer)
                    .withJWTId(UUID.randomUUID().toString())
                    .withClaim("username", user.getName())
                    .withExpiresAt(setTokenTimeToLive(timeToLive))
                    .sign(algorithm);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        if(Objects.isNull(token)){
            throw  new WebApplicationException(Response.status(BAD_REQUEST).entity("Could not create Token").build());
        }
        return new Token(token,timeToLive/(60*1000));
    }



    private void validateUser(User user) {
        User userInDataBase = userService.findUserByNameAndPassword(user.getName(), user.getPassword());
        if(Objects.isNull(userInDataBase)){
            throw new WebApplicationException(Response.status(FORBIDDEN).entity("User not in system").build());
        }
    }

    private Date setTokenTimeToLive(int timeToLive) {
        long millis = System.currentTimeMillis();
        return new Date(millis + timeToLive);
    }


    public void checkAuthorized(String authorization) {
        if (Objects.isNull(authorization)){
            throw new WebApplicationException(Response.status(400).entity("Required token to authorization").build());
        }
        String[] parts = authorization.split("\\s+");
        if (parts.length < 2 || !"Bearer".equals(parts[0])){
            throw new WebApplicationException(Response.status(400).entity("Invalid request").build());
        }
        String token = parts[1];
        String secretKey = AppConfigService.getSecretKey();
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(AppConfigService.getIssuer())
                    .build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new WebApplicationException(Response.status(400).entity(e.getMessage()).build());
        }

    }

//    public static void main(String[] args) {
//        User user = new User();
//        user.setName("admin");
//        user.setPassword("admin");
//        JWTAuthenticationServices jwtAuthenticationServices = new JWTAuthenticationServices();
//        Token token = jwtAuthenticationServices.createToken(user);
//    }
}


