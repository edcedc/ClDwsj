package com.d1540173108.hrz.event;

/**
 * Created by edison on 2019/1/28.
 */

public class LoginSuccessInEvent {

    public String name;
    public String head;

    public LoginSuccessInEvent(String name, String head) {
        this.name = name;
        this.head = head;
    }

}
