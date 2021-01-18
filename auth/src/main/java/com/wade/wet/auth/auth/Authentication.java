package com.wade.wet.auth.auth;

import com.wade.wet.auth.dto.UserDto;
import com.wade.wet.auth.exception.*;
import com.wade.wet.auth.mapper.Mapper;
import com.wade.wet.auth.model.User;
import com.wade.wet.auth.repository.UserRepository;
import com.wade.wet.auth.security.Hashing;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Authentication {
    final UserRepository userRepository;

    public Authentication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto login(UserDto userDto) throws Exception {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());

        if (user.isEmpty())
            throw new UserInexistentException();

        if (Hashing.verifyPassword(userDto.getPassword(), user.get().getPassword()))
            return Mapper.toUserDto(user.get());
        else
            throw new WrongPasswordException();
    }

    public UserDto register(UserDto userDto) throws Exception {
        if (!EmailValidator.getInstance().isValid(userDto.getEmail()))
            throw new InvalidEmailException();

        if (userRepository.findByEmail(userDto.getEmail()).isPresent())
            throw new UserAlreadyExistsException();

        if (!(userDto.getFirstName().matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")
                && userDto.getLastName().matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$")))
            throw new InvalidNameException();


        User user = userRepository.save(Mapper.toUser(userDto));

        return Mapper.toUserDto(user);
    }
}
