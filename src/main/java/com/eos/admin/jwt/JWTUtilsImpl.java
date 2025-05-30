package com.eos.admin.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtilsImpl {
	 private SecretKey Key;
	    
	    private static final long EXPIRATION_TIME = 86400000; //24 hours
	    
	    public JWTUtilsImpl() {
	    	String secreteString = "84356789369697645327597443269R634976R73846756644646464646464648779945121002336698774455511555789545664555555JKKK858855456HJJ";
	    	byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
	    	this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
	    }
	    
	    public String generateToken(UserDetails userDetails) {

	    	  HashMap<String, Object> claims = new HashMap<>();
	    	    claims.put("authorities", userDetails.getAuthorities().stream()
	    	        .map(auth -> auth.getAuthority())
	    	        .toList());
	    	return Jwts.builder()
	    			.claims(claims) 
	    			.subject(userDetails.getUsername())
	    			.issuedAt(new Date(System.currentTimeMillis()))
	    			.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	    			.signWith(Key)
	    			.compact();
	    }

	    public String generateRefreshToken(UserDetails userDetails) {
	    	HashMap<String, Object> claims = new HashMap<>();
    	    claims.put("authorities", userDetails.getAuthorities().stream()
    	        .map(auth -> auth.getAuthority())
    	        .toList());
	    	return Jwts.builder()
	    			.claims(claims)
	    			.subject(userDetails.getUsername())
	    			.issuedAt(new Date(System.currentTimeMillis()))
	    			.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	    			.signWith(Key)
	    			.compact();
	    }
	    
	    public String extractUsername(String token) {
	    	return extractClaims(token, Claims::getSubject);
	    }
	    
	    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
	        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());	
	    }
	    
	    public boolean isTokenValid(String token, UserDetails userDetails) {
	    	final String username = extractUsername(token);
	    	return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }

		private boolean isTokenExpired(String token) {
			// TODO Auto-generated method stub
			return extractClaims(token, Claims:: getExpiration).before(new Date());
		}

		
		public Claims extractAllClaims(String token) {
		    return Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload();
		}

}
