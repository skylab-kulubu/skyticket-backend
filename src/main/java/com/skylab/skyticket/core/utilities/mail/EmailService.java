package com.skylab.skyticket.core.utilities.mail;


import com.skylab.skyticket.core.results.Result;

public interface EmailService {

    Result sendMail(String to, String subject, String body);

}
