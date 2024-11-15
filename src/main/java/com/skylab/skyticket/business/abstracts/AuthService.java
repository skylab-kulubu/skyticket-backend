package com.skylab.skyticket.business.abstracts;

import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.entities.User;

public interface AuthService {

    Result register(User user);

    DataResult<String> login(User user);


}
