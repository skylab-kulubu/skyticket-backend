package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {


    DataResult<User> getUserById(String userId);

    DataResult<User> getUserByEmail(String email);

    DataResult<User> getUserByPhoneNumber(String phoneNumber);

    Result addUser(User user);

    DataResult<User> getAuthenticatedUser();

}
