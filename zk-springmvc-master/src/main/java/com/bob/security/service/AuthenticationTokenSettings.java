package com.bob.security.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bob.utils.Config;

/**
 * Settings for signing and verifying JWT tokens.
 *
 * @author cassiomolin
 */
@Component
class AuthenticationTokenSettings {

    @Autowired
    Config config;

    @PostConstruct
    public void init() {
        secret = config.getProperty("authentication.jwt.secret");
        clockSkew = Long.parseLong(config.getProperty("authentication.jwt.clockSkew"));
        audience = config.getProperty("authentication.jwt.audience");
        issuer = config.getProperty("authentication.jwt.issuer");
        authoritiesClaimName = config.getProperty("authentication.jwt.claimNames.authorities", "authorities");
        refreshCountClaimName = config.getProperty("authentication.jwt.claimNames.refreshCount", "refreshCount");
        refreshLimitClaimName = config.getProperty("authentication.jwt.claimNames.refreshLimit", "refreshLimit");
    }

    /**
     * Secret for signing and verifying the token signature.
     */
    private String secret;

    /**
     * Allowed clock skew for verifying the token signature (in seconds).
     */
    private Long clockSkew;

    /**
     * Identifies the recipients that the JWT token is intended for.
     */
    private String audience;

    /**
     * Identifies the JWT token issuer.
     */
    private String issuer;

    /**
     * JWT claim for the authorities.
     */
    private String authoritiesClaimName = "authorities";

    /**
     * JWT claim for the token refreshment count.
     */
    private String refreshCountClaimName = "refreshCount";

    /**
     * JWT claim for the maximum times that a token can be refreshed.
     */
    private String refreshLimitClaimName = "refreshLimit";

    public String getSecret() {
        return secret;
    }

    public Long getClockSkew() {
        return clockSkew;
    }

    public String getAudience() {
        return audience;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthoritiesClaimName() {
        return authoritiesClaimName;
    }

    public String getRefreshCountClaimName() {
        return refreshCountClaimName;
    }

    public String getRefreshLimitClaimName() {
        return refreshLimitClaimName;
    }
}
