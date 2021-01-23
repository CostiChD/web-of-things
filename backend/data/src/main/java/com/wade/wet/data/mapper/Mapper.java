package com.wade.wet.data.mapper;


import com.wade.wet.data.dto.UserDto;
import com.wade.wet.data.model.User;
import com.wade.wet.data.security.Hashing;


public final class Mapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(Hashing.hashPassword(userDto.getPassword()).get());
        return user;
    }
}
