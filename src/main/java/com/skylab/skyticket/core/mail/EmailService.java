package com.skylab.skyticket.core.mail;


import com.skylab.skyticket.core.results.Result;

public interface EmailService {

    Result sendMail(String to, String subject, String body);

}
