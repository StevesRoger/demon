package com.demon.auth.component;

public interface Const {

    String CORS_ALLOW_HEADERS = "Content-Type,Keep-Alive,User-Agent,Authorization,X-Requested-With,Cache-Control,Content-Length";
    String CORS_EXPOSE_HEADERS = "Cache-Control,Content-Language,Content-Length,Content-Type,Expires,Last-Modified,Pragma";
    String CORS_ALLOW_METHODS = "POST,PUT,GET,OPTIONS,DELETE,PATCH";

    String RESOURCE_OWNER = "password";
    String REFRESH_TOKEN = "refresh_token";

    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String USERNAME = "username";
    String PASSWORD = "password";
    String GRANT_TYPE = "grant_type";

    String SPRING_PREF_ROLE = "ROLE_";
}
