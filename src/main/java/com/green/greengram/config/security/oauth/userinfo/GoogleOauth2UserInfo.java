package com.green.greengram.config.security.oauth.userinfo;

import java.util.Map;

public class GoogleOauth2UserInfo extends Oauth2UserInfo{
    public GoogleOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("picture").toString();
    }
}
