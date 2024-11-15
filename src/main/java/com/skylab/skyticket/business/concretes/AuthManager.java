package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.AuthService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.ErrorDataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.core.results.SuccessDataResult;
import com.skylab.skyticket.core.security.JwtService;
import com.skylab.skyticket.entities.Role;
import com.skylab.skyticket.entities.User;
import org.hibernate.mapping.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthManager implements AuthService {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthManager(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Result register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.addUser(user);
    }

    @Override
    public DataResult<String> login(User user) {
        if(user.getEmail() == null){
            return new ErrorDataResult<>(Messages.emailCannotBeNull, HttpStatus.BAD_REQUEST);
        }

        if(user.getPassword() == null){
            return new ErrorDataResult<>(Messages.passwordCannotBeNull, HttpStatus.BAD_REQUEST);
        }



        var userResult = userService.getUserByEmail(user.getEmail());

        if(!userResult.isSuccess()){
            return new ErrorDataResult<>(userResult.getMessage(), userResult.getHttpStatus());
        }


        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
        if (authentication.isAuthenticated()) {
            var token = jwtService.generateToken(user.getEmail(), user.getAuthorities());

            return new SuccessDataResult<>(token, Messages.tokenGeneratedSuccessfully, HttpStatus.CREATED);
        }

        return new ErrorDataResult<>(Messages.invalidUsernameOrPassword, HttpStatus.BAD_REQUEST);
    }
}
