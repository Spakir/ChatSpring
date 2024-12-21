package org.example.chat.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
@Setter
@Getter
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String authority;
}
