package com.green.greengram.config.security.oauth;

import com.green.greengram.common.CookieUtils;
import com.green.greengram.common.GlobalOauth2;
import com.green.greengram.config.jwt.JwtUser;
import com.green.greengram.config.jwt.TokenProvider;
import com.green.greengram.config.security.MyUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final TokenProvider tokenProvider;
    private final GlobalOauth2 globalOauth2;
    private final CookieUtils cookieUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException, ServletException {
        if (res.isCommitted()) { // 응답 객체가 만료된 경우 (이전 프로세스에서 응답처리를 한 상태)
            log.error("onAuthenticationSuccess called with a committed response {}", res);
            return;
        }
        String targetUrl = "";
        clearAuthenticationAttributes(req, res);
        getRedirectStrategy().sendRedirect(req, res, targetUrl); // "fe/redirect?access_token=dddd&user_id=12"
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest req, HttpServletResponse res, Authentication auth) {
        String redirectUrl = cookieUtils.getValue(req, globalOauth2.getRedirectUriParamCookieName(), String.class);
        log.info("determineTargetUrl > getDefaultTargetUrl(): {}", getDefaultTargetUrl());

        String targetUrl = redirectUrl == null ? getDefaultTargetUrl() : redirectUrl;

        //쿼리스트링 생성
        MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
        OAuth2JwtUser oAuth2JwtUser = (OAuth2JwtUser)myUserDetails.getJwtUser();

        JwtUser jwtUser = new JwtUser(oAuth2JwtUser.getSignedUserId(), oAuth2JwtUser.getRoles());

        //AT, RT 샏성
        String accessToken = tokenProvider.generateToken(oAuth2JwtUser, Duration.ofHours(8));
        String refreshToken = tokenProvider.generateToken(oAuth2JwtUser, Duration.ofDays(15));

        int maxAge = 1_296_000;
        cookieUtils.setCookie(res,"refreshToken", refreshToken, maxAge, "/api/user/access_token");

        //쿼리스트링 생성(우리가 소셜 로그인 만들때 달라질 수 있는 부분)
        /*
            targetUrl: /fe/redirect
            accessToken: aaa
            userId: 20
            NickName: 홍길동
            pic: abc.jpg
            갑이 있다고 가정하고
            "fe/redirect?access_token=aaa&user_id=20&nick_name=홍길동&pic=abc.jpg"
         */
        return UriComponentsBuilder.fromUriString(targetUrl)
                                    .queryParam("access_token", accessToken)
                                    .queryParam("user_id",oAuth2JwtUser.getSignedUserId())
                                    .queryParam("nick_name",oAuth2JwtUser.getNickName())
                                    .queryParam("pic",oAuth2JwtUser.getPic())
                                    .build()
                                    .toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest req, HttpServletResponse res) {
        super.clearAuthenticationAttributes(req);
        repository.removeAuthorizationCookies(res);
    }
}