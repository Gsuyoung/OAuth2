package com.green.greengram.config.security.oauth.userinfo;

import com.green.greengram.config.security.SignInProviderType;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.green.greengram.config.security.SignInProviderType.KAKAO;

/*
    Google, Naver, Kakao 플랫폼에서 받은 유저정보 JSON을
    HashMap 형식으로 파싱
    HashMap을 이용한 규격화된 객체로 파싱
 */

@Component
public class Oauth2UserInfoFactory {
    public Oauth2UserInfo getOauth2UserInfo(SignInProviderType signInProviderType, Map<String, Object> attributes) {
        return switch (signInProviderType) {
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            default -> null;
        };
    }
}
