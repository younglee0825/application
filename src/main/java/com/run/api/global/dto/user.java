package com.run.api.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class user {
    private String nickname;
    private String password;
    private String email;
    private String phone;
    private String m_name;

}