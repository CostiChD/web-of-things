package com.wade.wet.data.controller;

import com.wade.wet.data.auth.Authentication;
import com.wade.wet.data.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    final Authentication auth;


    public AuthController(Authentication auth) {
        this.auth = auth;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody UserDto dto) throws Exception {
        return auth.register(dto);
    }

    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    public UserDto login(@RequestBody UserDto dto) throws Exception {
        return auth.login(dto);
    }
}
