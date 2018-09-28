package com.example.thecoop.domain;


import org.springframework.security.core.GrantedAuthority;

/**
 * @author iveshtard
 * @since 7/31/2018
 */

public enum Role implements GrantedAuthority {
    USER, ADMIN;


    @Override
    public String getAuthority() {
        return name();
    }
}
