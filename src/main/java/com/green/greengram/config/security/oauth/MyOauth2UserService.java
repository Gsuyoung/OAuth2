package com.green.greengram.config.security.oauth;

import com.green.greengram.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyOauth2UserService {
    private final UserRepository userRepository;

}
