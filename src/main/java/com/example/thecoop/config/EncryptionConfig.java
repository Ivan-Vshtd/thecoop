package com.example.thecoop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author iveshtard
 * @since 8/6/2018
 */

@Configuration
public class EncryptionConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new MessageDigestPasswordEncoder("MD5");
    }

}
