package com.green.greengram.entity;

import com.green.greengram.config.security.SignInProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity //테이블을 만들고 DML때 사용
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { "uid", "provider_type" } //둘 다 unique
                )
        }
)
public class User extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long userId; //user_id

    @Column(nullable = false)
    private SignInProviderType providerType;

    @Column(nullable = false, length = 70)
    private String uid;

    @Column(nullable = false, length = 100)
    private String upw;

    @Column(length = 30)
    private String nickName; //nick_name

    @Column(length = 50)
    private String pic;

}
