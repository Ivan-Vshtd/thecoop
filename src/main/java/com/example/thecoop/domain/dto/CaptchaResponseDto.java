package com.example.thecoop.domain.dto;

/**
 * @author iveshtard
 * @since 8/6/2018
 */

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter @Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponseDto {
    private boolean success;

    @JsonAlias("error-codes")
    private Set<String> errorCodes;
}
