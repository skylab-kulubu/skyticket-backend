package com.skylab.skyticket.business.constants;

import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.Ticket;
import org.springframework.http.HttpStatus;

public class Messages {
    public static String eventNameCannotBeNull = "Etkinlik adı boş olamaz!";
    public static String eventAdded = "Etkinlik başarıyla eklendi!";
    public static String eventNotFound = "Etkinlik bulunamadı!";
    public static String eventFound = "Etlinlik başarıyla getirildi!";
    public static String userNotFound = "Kullanıcı bulunamadı!";
    public static String userFound = "Kullanıcı başarıyla getirildi!";
    public static String userAdded = "Kullanıcı başarıyla eklendi!";
    public static String ticketNotFound = "Bilet bulunamadı!";
    public static String ticketFound = "Bilet başarıyla getirildi!";
    public static String ticketAlreadyExists = "Bilet zaten mevcut!";
    public static String ticketAdded = "Bilet başarıyla eklendi!";
    public static String ticketImageNotFound = "Bilet resmi bulunamadı!";
    public static String invalidOptions = "Geçersiz seçenekler!";
    public static String fieldsCannotBeNull = "Alanlar boş olamaz!";
    public static String ticketAlreadyUsed = "Bilet zaten kullanıldı!";
    public static String ticketSubmitted = "Bilet başarıyla kullanıldı!";
    public static String emailCannotBeNull = "Email boş olamaz!";
    public static String passwordCannotBeNull = "Şifre boş olamaz!";
    public static String tokenGeneratedSuccessfully =   "Token başarıyla oluşturuldu!";
    public static String invalidUsernameOrPassword = "Geçersiz kullanıcı adı veya şifre!";
}
