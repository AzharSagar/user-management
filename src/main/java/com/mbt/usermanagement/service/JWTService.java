package com.mbt.usermanagement.service;

import com.mbt.usermanagement.beans.*;

import com.mbt.usermanagement.beans.User;
import com.mbt.usermanagement.repository.UserRepository;
import com.mbt.usermanagement.util.Parameters;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This class defines methods for coping with JWT.
 *
 */
@Service
public class JWTService {


	// jab bhi login hoga to token encrypt hokar database mn store ho jae ga
	// or logout ke time wo token database mn delete ho jae ga



	@Autowired
	private UserRepository userRepository;

	@Value(Parameters.KEY_JWT_EXPIRE_HOURS)
	private Long expireHours;

	@Value(Parameters.KEY_JWT_SECRET)
	private String plainSecret;

	private String encodedSecret;

	@Autowired
	private PasswordEncoder passwordEncoder;



	public Authentication getAuthorization(String token) throws ExpiredJwtException, SignatureException, Exception {
		return getUser(this.encodedSecret, token);
	}
	public Authentication getCurrentUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}



	@PostConstruct
	protected void init() {
		this.encodedSecret = generateEncodedSecret(this.plainSecret);
	}

	private String generateEncodedSecret(String plainSecret) {
		if (StringUtils.isEmpty(plainSecret)) {
			throw new IllegalArgumentException("JWT secret cannot be null or empty.");
		}
		return Base64.getEncoder().encodeToString(this.plainSecret.getBytes());
	}

	private Date getExpirationTime() {
		Date now = new Date();
		Long expireInMilis = TimeUnit.HOURS.toMillis(expireHours);
		return new Date(expireInMilis + now.getTime() * 1000);
	}

	private Authentication getUser(String encodedSecret, String token) throws ExpiredJwtException, SignatureException, Exception {

		Claims claims = Jwts.parser().setSigningKey(encodedSecret).parseClaimsJws(token).getBody();

			String username = claims.getSubject();
			User user = userRepository.findByEmail(username);
			if (user == null) {
				return null;
			}

			if(user.getLogin()){

				List<String> roleList = new ArrayList<>();
				roleList.add(user.getDesignation().getDesignation());
				Collection<? extends GrantedAuthority> authorities = roleList.stream()
						.map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
				authorities.forEach(s -> {
				});
				return new UsernamePasswordAuthenticationToken(username, "", authorities);
			}
			else return  null;
	}

	private String getToken(String encodedSecret, MinimalJWTUser jwtUser) {
		Date now = new Date();
		return Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(jwtUser.getEmail())
				.claim("roles", jwtUser.getRole()).setIssuedAt(now).setExpiration(getExpirationTime())
				.signWith(SignatureAlgorithm.HS512, encodedSecret).compact();
	}

	public String getToken(MinimalJWTUser jwtUser) {
		return  getToken(this.encodedSecret, jwtUser);

	}

}
