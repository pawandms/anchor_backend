package com.anchor.app.oauth.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import com.anchor.app.oauth.service.DatabaseRegisteredClientRepository;
import com.anchor.app.oauth.service.DatabaseUserDetailsService;
import com.anchor.app.users.repository.UserAuthRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

 private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DatabaseUserDetailsService databaseUserDetailsService;

	@Autowired
	private DatabaseRegisteredClientRepository databaseRegisteredClientRepository;

	@Autowired
	private UserAuthRepository userAuthRepository;


	//@Autowired
	//private MongoOAuth2AuthorizationService mongoOAuth2AuthorizationService;

	@Bean 
	@Order(1)
	SecurityFilterChain authorizationServerSecurityFilterChain(
			HttpSecurity http)
			throws Exception {

		// Retrieve the configurer instance
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
				
		//OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http
			.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
			.with(authorizationServerConfigurer, Customizer.withDefaults());

		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
			.oidc(Customizer.withDefaults())
			.tokenEndpoint(tokenEndpoint -> tokenEndpoint
					.accessTokenRequestConverter(new CustomCodeGrantAuthenticationConverter())
					.accessTokenRequestConverter(new PasswordlessAuthenticationConverter())
					.authenticationProvider(new CustomCodeGrantAuthenticationProvider(
							oAuth2AuthorizationService(), 
							tokenGenerator(),
							databaseUserDetailsService,
							passwordEncoder()))
					.authenticationProvider(new PasswordlessAuthenticationProvider(
							oAuth2AuthorizationService(),
							tokenGenerator(),
							databaseUserDetailsService))
					.authenticationProvider(new CustomRefreshTokenAuthenticationProvider(
							oAuth2AuthorizationService(),
							tokenGenerator()))
					.accessTokenResponseHandler(customTokenResponseHandler())		
					.errorResponseHandler(authenticationFailureHandler()));
		
		http
			.authorizeHttpRequests(authorize -> authorize
				.anyRequest().authenticated()
			)
			.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.permitAll()
			)
			.httpBasic(Customizer.withDefaults())
			.exceptionHandling((exceptions) -> exceptions
				.authenticationEntryPoint(customAuthenticationEntryPoint())
			)
			.csrf(csrf -> csrf.disable())
				
			.oauth2ResourceServer((resourceServer) -> resourceServer
				.jwt(jwt -> jwt
					.decoder(jwtDecoder(jwkSource()))
					.jwtAuthenticationConverter(jwtAuthenticationConverter())
				)
			)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		return http.build();
	}

	@Bean 
	@Order(2)
	SecurityFilterChain mainSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/api/**")
			.authorizeHttpRequests((authorize) -> authorize
				
				// Public authentication endpoints
				.requestMatchers("/api/*/public/**").permitAll()
				
				// All other requests require authentication
				.anyRequest().authenticated()
			)
			// Configure OAuth2 Resource Server for JWT tokens
			.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt
					.decoder(jwtDecoder(jwkSource()))
					.jwtAuthenticationConverter(jwtAuthenticationConverter())
				)
				.authenticationEntryPoint(customAuthenticationEntryPoint())
			)
			// Configure exception handling to return 401 for API requests
			.exceptionHandling(exceptions -> exceptions
				.defaultAuthenticationEntryPointFor(
					customAuthenticationEntryPoint(),
					new org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest")
				)
				.defaultAuthenticationEntryPointFor(
					customAuthenticationEntryPoint(),
					request -> request.getRequestURI().startsWith("/api/")
				)
				.defaultAuthenticationEntryPointFor(
					customAuthenticationEntryPoint(),
					request -> request.getRequestURI().startsWith("/rest/")
				)
				.defaultAuthenticationEntryPointFor(
					new LoginUrlAuthenticationEntryPoint("/login"),
					new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
				)
			)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			);

		return http.build();
	}


	@Bean
    PasswordEncoder passwordEncoder() {
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return new BCryptPasswordEncoder(4);
    }

	@Bean 
	JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID("anchor-key-1") // Fixed key ID instead of random UUID
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey() { 
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			// Use a fixed seed for deterministic key generation in development
			// IMPORTANT: Only use this in development, not in production!
			// For production, load keys from a keystore or external secure storage
			java.security.SecureRandom secureRandom = java.security.SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed("anchor-seed-2025".getBytes()); // Fixed seed for dev
			keyPairGenerator.initialize(2048, secureRandom);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean 
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}

	@Bean 
	AuthorizationServerSettings authorizationServerSettings() {
		/* 
		return AuthorizationServerSettings.builder()
				.authorizationEndpoint("/oauth2/authorize")
				.deviceAuthorizationEndpoint("/oauth/device_authorization")
				.deviceVerificationEndpoint("/oauth/device_verification")
				.tokenEndpoint("/oauth/token")
				.tokenIntrospectionEndpoint("/oauth/introspect")
				.tokenRevocationEndpoint("/oauth/revoke")
				.jwkSetEndpoint("/oauth/jwks")
				.oidcLogoutEndpoint("/oauth/logout")
				.oidcUserInfoEndpoint("/oauth/userinfo")
				.oidcClientRegistrationEndpoint("/oauth/connect/register")
				.build();
		*/		
		return AuthorizationServerSettings.builder()
				.build();
	}

	@Bean
	OAuth2AuthorizationService oAuth2AuthorizationService() {
		//return new JdbcOAuth2AuthorizationService(jdbcTemplate, databaseRegisteredClientRepository);
		return new InMemoryOAuth2AuthorizationService();
	}
	
	@Bean
	OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
		NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
		jwtGenerator.setJwtCustomizer(tokenCustomizer());
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
		return new DelegatingOAuth2TokenGenerator(
				jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
	}
	
	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
		return context -> {
			Authentication principal = context.getPrincipal();
			if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
				Set<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());
				context.getClaims().claim("authorities", authorities);
				
				// Add custom user claims
				addCustomUserClaims(context, principal);
			}
			
			if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
				Set<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());
				context.getClaims().claim("authorities", authorities);
				
				// Add custom user claims to ID token as well
				addCustomUserClaims(context, principal);
			}
		};
	}
	
	private void addCustomUserClaims(JwtEncodingContext context, Authentication principal) {
		if (principal != null && principal.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
			org.springframework.security.core.userdetails.UserDetails userDetails = 
				(org.springframework.security.core.userdetails.UserDetails) principal.getPrincipal();
			String username = userDetails.getUsername();
			
			try {
				
				/* 
				com.hifinite.components.user.model.User loggedInUser = userService.getActiveUserByUsername(username);
				if (loggedInUser != null) {
					java.util.List<Integer> roleArray = new java.util.ArrayList<>();
					loggedInUser.getUserRole().forEach(userRole -> roleArray.add(userRole.getRoleId()));
					
					// Add custom claims to the JWT
					context.getClaims().claim("userID", loggedInUser.getUserID().toString());
					context.getClaims().claim("userTypeID", loggedInUser.getUserTypeID().toString());
					context.getClaims().claim("roleID", roleArray);
					context.getClaims().claim("clientID", loggedInUser.getClientID().toString());
					String oauthClientID = context.getRegisteredClient().getClientId();
					context.getClaims().claim("client_id", oauthClientID);
				}
					*/
			} catch (Exception e) {
				// Log the error but don't fail token generation
				System.err.println("Error adding custom claims to JWT: " + e.getMessage());
			}
		}
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(false);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

	
	@Bean
	public CustomTokenResponseHandler customTokenResponseHandler() {
		return new CustomTokenResponseHandler(userAuthRepository);
	}


    @Bean
    public HttpFirewall getHttpFirewall() 
	{
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
		strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
		strictHttpFirewall.setAllowBackSlash(true);
		return strictHttpFirewall;
    }

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.httpFirewall(getHttpFirewall());
}

}
