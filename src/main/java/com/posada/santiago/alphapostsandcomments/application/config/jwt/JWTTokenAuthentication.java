package com.posada.santiago.alphapostsandcomments.application.config.jwt;

import com.posada.santiago.alphapostsandcomments.AlphaPostsAndCommentsApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;


@RequiredArgsConstructor
public class JWTTokenAuthentication implements WebFilter {
    //Bearer
    public static final String HEADER_PREFIX ="Bearer ";

    private final JWTTokenProvider jwtTokenProvider;

    private final Logger logger = Logger.getLogger(JWTTokenAuthentication.class.getName());


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            //1. Get the request
        logger.info("Received request, verifying token");
        var token = resolveToken(exchange.getRequest());

        if(StringUtils.hasText(token) && this.jwtTokenProvider.validateToken(token)){
            logger.info("Token accepted, processing petition");
            var autenticantion = this.jwtTokenProvider.getAuthentication(token);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(autenticantion));
        }
        return chain.filter(exchange);
    }

    private String resolveToken(ServerHttpRequest request){
            //1. Get the header that has the token
        var bearerToken=request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION); //Bearer jfhkjdsgfkjsdgfkjsgdfgsjkfgsjkdg
            //2.
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX) ? bearerToken.substring(7) : null;

    }
}

