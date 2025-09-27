package com.esporte.myapp.security;

import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String requiredAudience;

    public AudienceValidator(String requiredAudience) {
        this.requiredAudience = requiredAudience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        List<String> audiences = token.getAudience();
        if (!CollectionUtils.isEmpty(audiences) && audiences.contains(requiredAudience)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error err = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_TOKEN,
                "Missing/invalid audience",
                null
        );
        return OAuth2TokenValidatorResult.failure(err);
    }
}
