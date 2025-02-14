package com.green.greengram.config.jwt;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor //모든 파라미터가 있는 생성자
@NoArgsConstructor //기본 생성자
@EqualsAndHashCode //Equals, HashCode 메소드 오버라이딩
public class JwtUser {
    private long signedUserId;
    private List<String> roles; //인가(권한)처리 때 사용, ROLE_이름, ROLE_USER, ROLE_ADMIN
}
