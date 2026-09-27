package ru.yahoondex.archhelper.configurator.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .oauth2Login(Customizer.withDefaults()).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConv = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantConv = new JwtGrantedAuthoritiesConverter();
        jwtConv.setPrincipalClaimName("preferred_username");
        jwtConv.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = jwtGrantConv.convert(jwt);
            List<String> roles = (List<String>) ((Map<String, Object>) jwt.getClaim("realm_access")).get("roles");
            //return authorities.stream().toList();
            return Stream.concat(authorities.stream(),
                    roles.stream()
                            .filter(role -> role.startsWith("yahoondex-arch-helper-"))
                            .map(role -> role.replace("yahoondex-arch-helper-", ""))
                            .map(role -> "ROLE_".concat(role))
                            .map(SimpleGrantedAuthority::new)
                            .map(GrantedAuthority.class::cast))
                    .toList();
        });
        return jwtConv;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        OidcUserService service = new OidcUserService();
        return userRequest -> {
            OidcUser user = service.loadUser(userRequest);
            List<String> roles = (List<String>) ((Map<String, Object>) user.getClaim("realm_access")).get("roles");
            //List<String> roles = user.getClaimAsStringList("realm_access.roles");
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities().stream().toList();
            Stream.concat(user.getAuthorities().stream(),
                    roles.stream()
                            .filter(role -> role.startsWith("yahoondex-arch-helper-"))
                            .map(role -> role.replace("yahoondex-arch-helper-", ""))
                            .map(role -> "ROLE_".concat(role))
                            .map(SimpleGrantedAuthority::new)
                            .map(GrantedAuthority.class::cast)
            ).toList();
            return new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo());
        };
    }
}
