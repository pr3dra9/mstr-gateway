/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.mas.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * @author Predrag
 */
@Component
public class CustomServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomServerAccessDeniedHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return ReactiveSecurityContextHolder.getContext()
            .map(context -> context.getAuthentication())
            .doOnNext(authentication -> {
                if (authentication != null) {
                    logger.error("User: {} attempted to access the protected URL: {}",
                            authentication.getName(),
                            exchange.getRequest().getURI());
                    authentication.getAuthorities().stream()
                            .map(athG -> athG.getAuthority())
                            .forEach(authority -> logger.error("Authority: {}", authority));
                }
            })
            .then(Mono.defer(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }));
    }    
}
