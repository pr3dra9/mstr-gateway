/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.mas.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

/**
 *
 * @author Predrag
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ServerAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(ServerAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange((authorize) -> authorize
                .pathMatchers("/actuator/**").permitAll()
                //.pathMatchers("/api/v1/**").hasRole("USER")  // zahteva prilagodjavanja
                //.pathMatchers("/api/v1/**").hasAuthority("SCOPE_profile1") // ne zahteva prilagodjavanja
                .anyExchange().authenticated())
                .exceptionHandling((handlingSpec) -> handlingSpec
                        .accessDeniedHandler(accessDeniedHandler)
                )
                /*
                .oauth2ResourceServer(
                        (oauth2) -> oauth2
                                .jwt(jwtSpec -> jwtSpec
                                        .jwtAuthenticationConverter(customConverter())
                                )
                );*/
                .oauth2ResourceServer(
                        (oauth2) -> oauth2
                                .jwt(Customizer.withDefaults())
                );

        return http.build();
    }

    /*
    private ReactiveJwtAuthenticationConverter customConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new CustomJwtAuthenticationConverter());
        return converter;
    }
    */


}
