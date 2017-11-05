package com.spring.web.security.jwt.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.spring.web.security.jwt.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtTokenUtil implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7718226872997384407L;
	static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "aud";
    static final String CLAIM_KEY_CREATED = "iat";

    static final String AUDIENCE_UNKNOWN = "unknown";
    static final String AUDIENCE_WEB = "web";
    static final String AUDIENCE_MOBILE = "mobile";
    static final String AUDIENCE_TABLET = "tablet";

    @Value("${jwt.expiration}")
    private int expiration;
    
    @Value("${jwt.secret}")
    private String secret;

	public String generateToken(UserDetails userDetails, Device device) {
	        Map<String, Object> claims = new HashMap<>();
	        return doGenerateToken(claims, userDetails.getUsername(), generateAudience(device));
	}
	

	 private String doGenerateToken(Map<String, Object> claims, String subject, String audience) {
	        final Date createdDate = new Date();
	        final Date expirationDate = calculateExpirationDate(createdDate);

	        System.out.println("doGenerateToken " + createdDate);

	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(subject)
	                .setAudience(audience)
	                .setIssuedAt(createdDate)
	                .setExpiration(expirationDate)
	                .signWith(SignatureAlgorithm.HS512, secret)
	                .compact();
	    }
	    
	    private Date calculateExpirationDate(Date createdDate) {
	        return new Date(createdDate.getTime() + expiration * 1000);
	    }
	    
	    private String generateAudience(Device device) {
	        String audience = AUDIENCE_UNKNOWN;
	        if (device.isNormal()) {
	            audience = AUDIENCE_WEB;
	        } else if (device.isTablet()) {
	            audience = AUDIENCE_TABLET;
	        } else if (device.isMobile()) {
	            audience = AUDIENCE_MOBILE;
	        }
	        return audience;
	    }

		public String getUsernameFromToken(String token) {
			final Claims claims = getAllClaimsFromToken(token);
			return claims.getSubject();
		}

		private Claims getAllClaimsFromToken(String token) {
			return Jwts.parser()
	                .setSigningKey(secret)
	                .parseClaimsJws(token)
	                .getBody();
		}

		public boolean validateToken(String token, UserDetails userDetails) {
			JwtUser user = (JwtUser) userDetails;
	        final String username = getUsernameFromToken(token);
	        final Date created = getIssuedAtDateFromToken(token);
	        //final Date expiration = getExpirationDateFromToken(token);
	        return (
	              username.equals(user.getUsername())
	                    && !isTokenExpired(token)
	                    && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
	        );
		}

		private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
	        return (lastPasswordReset != null && created.before(lastPasswordReset));
	    }

		private boolean isTokenExpired(String token) {
			final Date expiration = getExpirationDateFromToken(token);
	        return expiration.before(new Date());
		}

		private Date getExpirationDateFromToken(String token) {
			final Claims claims = getAllClaimsFromToken(token);
			return claims.getExpiration();
		}

		private Date getIssuedAtDateFromToken(String token) {
			final Claims claims = getAllClaimsFromToken(token);
			 return claims.getIssuedAt();
		}

		public boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
			final Date created = getIssuedAtDateFromToken(token);
	        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
	                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
		}

		private boolean ignoreTokenExpiration(String token) {
			String audience = getAudienceFromToken(token);
	        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
		}

		private String getAudienceFromToken(String token) {
			Claims claims = getAllClaimsFromToken(token);
			return claims.getAudience();
		}

		public String refreshToken(String token) {
			final Date createdDate = new Date();
	        final Date expirationDate = calculateExpirationDate(createdDate);

	        final Claims claims = getAllClaimsFromToken(token);
	        claims.setIssuedAt(createdDate);
	        claims.setExpiration(expirationDate);

	        return Jwts.builder()
	                .setClaims(claims)
	                .signWith(SignatureAlgorithm.HS512, secret)
	                .compact();
		}

}
