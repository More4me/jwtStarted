package com.example.jwtStart2.Security;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.springframework.security
        .authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static java.util.Collections.emptyList;

//ToDo": 2-Define class and attributes to create token
class TokenAuthenticationService {
    //time of token to be expired 10 days
    // suggest to be smaller
    static final long EXPIRATIONTIME = 864_000_000;
    // this string is used to verify if the content changed while token is send or not
    // as if the user could take the creditional and hash method and
    // create the token he never create the same token as he don't have this secret string
    static final String SECRET = "ThisIsASecret";
    // This prefix is mandatory as to make spring recogonize the token
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    // toDo: 3- create token
    static void addAuthentication(HttpServletResponse res, String username) {
        // create the username and expiration date from now to 10 days coming
        // use algorithm Hs512 to encode the token
        String JWT = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        // add in the reponse header the token
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    //toDo: 4-get the user from the token and verify it
    static Authentication getAuthentication(HttpServletRequest request) {
        // parse the header and find the header
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                    null;
        }
        return null;
    }
}