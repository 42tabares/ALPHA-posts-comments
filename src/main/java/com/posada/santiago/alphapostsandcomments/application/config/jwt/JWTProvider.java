package com.posada.santiago.alphapostsandcomments.application.config.jwt;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JWTProvider {

    private String secretKey="cor-leonis-ad-principem-pertinet";

    //In milliseconds
    private long validTime= 3600000; //1h*
}
