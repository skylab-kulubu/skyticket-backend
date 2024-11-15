package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.ErrorDataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.core.results.SuccessDataResult;
import com.skylab.skyticket.dataAccess.UserDao;
import com.skylab.skyticket.entities.Role;
import com.skylab.skyticket.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class UserManager implements UserService {

    private final UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return getUserByEmail(username).getData();
    }

    @Override
    public DataResult<User> getUserById(String userId) {
        var result = userDao.findById(UUID.fromString(userId));

        if (!result.isPresent() ){
            return new ErrorDataResult<>(Messages.userNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<User>(result.get(), Messages.userFound, HttpStatus.OK);

    }

    @Override
    public DataResult<User> getUserByEmail(String email) {
        var result = userDao.findByEmail(email);

        if (result.isEmpty()){
            return new ErrorDataResult<>(Messages.userNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<User>(result.get(), Messages.userFound, HttpStatus.OK);
    }

    @Override
    public Result addUser(User user) {

        user.setAuthorities(Set.of(Role.ROLE_USER));



        userDao.save(user);

        return new SuccessDataResult<User>(user, Messages.userAdded, HttpStatus.CREATED);
    }
}
