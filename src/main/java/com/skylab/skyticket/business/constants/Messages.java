package com.skylab.skyticket.business.constants;

import com.skylab.skyticket.entities.Event;
import com.skylab.skyticket.entities.Session;
import com.skylab.skyticket.entities.Ticket;
import org.springframework.http.HttpStatus;

import java.util.List;

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
    public static String htmlNotFound = "HTML dosyası bulunamadı!";
    public static String mailNotSent = "Mail gönderilemedi!";
    public static String ticketSent = "Bilet başarıyla gönderildi!";
    public static String startDateMustBeBeforeEndDate = "Başlangıç tarihi bitiş tarihinden önce olmalıdır!";
    public static String startDateAndEndDateCannotBeNull = "Başlangıç ve bitiş tarihleri boş olamaz!";
    public static String eventDayAdded = "Etkinlik günü başarıyla eklendi!";
    public static String sessionNameCannotBeNull = "Oturum adı boş olamaz!";
    public static String sessionAdded = "Oturum başarıyla eklendi!";
    public static String sessionDeleted = "Oturum başarıyla silindi!";
    public static String eventDayNotFound = "Etkinlik günü bulunamadı!";
    public static String sessionsFound = "Oturumlar başarıyla getirildi!";
    public static String sessionNotFound = "Oturum bulunamadı!";
}
