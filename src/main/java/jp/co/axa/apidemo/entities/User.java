package jp.co.axa.apidemo.entities;

import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter
    @Setter
    private Long userId;
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private String password;
}
