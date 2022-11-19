package com.serhiiostapenko.socnet.security;

public class SecurityConstants {
    public static final String SIGNUP_URLS = "/socnet/auth/**";

    public static final String SECRET = "SecretKeyGenJWT";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER = "Authorization";

    public static final String CONTENT_TYPE = "application/json";

    public static final long EXPIRATION_TIME = 600_000;
}
