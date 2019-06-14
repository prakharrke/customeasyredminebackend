package jwt;
import java.security.Key;
import java.util.Date;

import customeasyredmineexception.CustomEasyRedmineException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JWTTokenCreation {

	Key key = null;
	Date expirationDate = null;
	public JWTTokenCreation() {
		this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		long currentMillis = System.currentTimeMillis();
		long expirationMillis = currentMillis + (3600000 *  2);
		this.expirationDate = new Date(expirationMillis);
	}
	
	public String createJWTToken(String username) {
		
		String jws = Jwts.builder().setSubject(username).signWith(key).setExpiration(this.expirationDate).compact();
		return jws;
	}
	
	public String validateJWTToken(String jwsString) throws CustomEasyRedmineException {
		Jws<Claims> jws;
		try {
		jws = Jwts.parser()        
			    .setSigningKey(this.key)         
			    .parseClaimsJws(jwsString);
		Claims claims = jws.getBody();
		String username = claims.getSubject();
		return username;
		}catch(JwtException e) {
			
			throw  new CustomEasyRedmineException("JWT Validation failed");
		}
		
		
	}
}
