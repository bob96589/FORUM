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
// @Dependent
@Component
class AuthenticationTokenSettings {

    @Autowired
    Config config;

    @PostConstruct // after Creating bean init() will be execute.
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
    // @Inject
    // @Configurable("authentication.jwt.secret")
    private String secret;

    /**
     * Allowed clock skew for verifying the token signature (in seconds).
     */
    // @Inject
    // @Configurable("authentication.jwt.clockSkew")
    private Long clockSkew;

    /**
     * Identifies the recipients that the JWT token is intended for.
     */
    // @Inject
    // @Configurable("authentication.jwt.audience")
    private String audience;

    /**
     * Identifies the JWT token issuer.
     */
    // @Inject
    // @Configurable("authentication.jwt.issuer")
    private String issuer;

    /**
     * JWT claim for the authorities.
     */
    // @Inject
    // @Configurable("authentication.jwt.claimNames.authorities")
    private String authoritiesClaimName = "authorities";

    /**
     * JWT claim for the token refreshment count.
     */
    // @Inject
    // @Configurable("authentication.jwt.claimNames.refreshCount")
    private String refreshCountClaimName = "refreshCount";

    /**
     * JWT claim for the maximum times that a token can be refreshed.
     */
    // @Inject
    // @Configurable("authentication.jwt.claimNames.refreshLimit")
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
