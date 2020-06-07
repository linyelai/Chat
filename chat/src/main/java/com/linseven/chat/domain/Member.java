package com.linseven.chat.domain;

import lombok.Data;

/**
 * @Author linseven
 * @Date 2020/5/21
 */
@Data
public class Member {

    private String username;
    private String nickname;
    private String avatar;
    private String password;
}
