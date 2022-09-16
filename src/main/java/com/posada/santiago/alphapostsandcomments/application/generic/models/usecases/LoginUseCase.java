package com.posada.santiago.alphapostsandcomments.application.generic.models.usecases;

import com.posada.santiago.alphapostsandcomments.application.config.jwt.JWTTokenProvider;
import com.posada.santiago.alphapostsandcomments.application.generic.models.AuthenticationRequest;
import com.posada.santiago.alphapostsandcomments.application.handlers.CommandHandle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
    private final JWTTokenProvider jwtTokenProvider;

    private final ReactiveAuthenticationManager authenticationManager;

    private final Logger logger = Logger.getLogger(CommandHandle.class.getName());

    public Mono<ServerResponse> logIn(Mono<AuthenticationRequest> authenticationRequest){

        logger.info("Handling login request...");

        return authenticationRequest
                .flatMap(authRequest -> this.authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()))
                        .onErrorMap(BadCredentialsException.class, err -> new Throwable(HttpStatus.FORBIDDEN.toString()))
                        .map(this.jwtTokenProvider::createToken))
                .flatMap(jwt-> {
                    logger.info("Login successful, token created");
                    //HttpHeaders httpHeaders = new HttpHeaders();
                    //httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
                    var tokenBody = Map.of("access_token", jwt);
                    return ServerResponse
                            .ok()
                            .headers(httpHeaders1 -> httpHeaders1.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                            .bodyValue(tokenBody);

                });
    }
}
