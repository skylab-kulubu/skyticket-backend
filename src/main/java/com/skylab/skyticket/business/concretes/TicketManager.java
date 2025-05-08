package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.business.abstracts.ImageService;
import com.skylab.skyticket.business.abstracts.TicketService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.mail.EmailService;
import com.skylab.skyticket.core.results.*;
import com.skylab.skyticket.dataAccess.TicketDao;
import com.skylab.skyticket.entities.Option;
import com.skylab.skyticket.entities.Role;
import com.skylab.skyticket.entities.Ticket;
import com.skylab.skyticket.entities.User;
import com.skylab.skyticket.entities.dtos.ticket.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Random;

@Service
public class TicketManager implements TicketService {

    private final TicketDao ticketDao;

    private final EventService eventService;

    private final UserService userService;

    private final EmailService emailService;

    public TicketManager(TicketDao ticketDao, EventService eventService, UserService userService, ImageService imageService, EmailService emailService) {
        this.ticketDao = ticketDao;
        this.eventService = eventService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public Result addTicket(AddTicketDto addTicketDto) {
        var eventResult = eventService.getEventById(addTicketDto.getEventId());
        if (!eventResult.isSuccess()){
            return eventResult;
        }
        var event = eventResult.getData();

        User user;
        var userResult = userService.getUserByEmail(addTicketDto.getEmail());

        if (userResult.isSuccess()) {
            user = userResult.getData();
        } else {
            var userToAdd = User.builder()
                    .firstName(addTicketDto.getFirstName())
                    .lastName(addTicketDto.getLastName())
                    .email(addTicketDto.getEmail())
                    .phoneNumber(addTicketDto.getPhoneNumber())
                    .faculty(addTicketDto.getFaculty())
                    .department(addTicketDto.getDepartment())
                    .university(addTicketDto.getUniversity())
                    .birthDate(addTicketDto.getBirthDate())
                    .authorities(Set.of(Role.ROLE_USER))
                    .build();

            var userAddResult = userService.addUser(userToAdd);
            if (!userAddResult.isSuccess()) {
                return userAddResult;
            }
            var userGetResult = userService.getUserByEmail(userToAdd.getEmail());
            if (!userGetResult.isSuccess()){
                return userGetResult;
            }

            user = userGetResult.getData();
        }

        boolean hasView = addTicketDto.getView() != null && !addTicketDto.getView().isEmpty();
        boolean hasFavouriteCharacter = addTicketDto.getFavouriteCharacter() != null && !addTicketDto.getFavouriteCharacter().isEmpty();
        boolean hasSpecialOption = addTicketDto.getSpecialOption() != null && !addTicketDto.getSpecialOption().isEmpty();

        if (!( (hasView && hasFavouriteCharacter) || hasSpecialOption )) {
            return new ErrorDataResult<>(Messages.invalidOptions, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (hasSpecialOption && (hasView || hasFavouriteCharacter)) {
            return new ErrorDataResult<>(Messages.invalidOptions, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var ticketResult = getTicketByUserIdAndEventId(user.getId(), event.getId());
        if (ticketResult.isSuccess()){
            return new ErrorDataResult<>(Messages.ticketAlreadyExists, HttpStatus.CONFLICT);
        }

        Set<Option> optionsOfTicket = new HashSet<>();
        try {
            if (hasSpecialOption) {
                Option specialOption = Option.fromDescription(addTicketDto.getSpecialOption());

                if (specialOption != Option.GECENIN_YILDIZI && specialOption != Option.SKYDAYS && specialOption != Option.YILDIZJAM) {
                    return new ErrorDataResult<>(Messages.invalidOptions, HttpStatus.BAD_REQUEST);
                }

                optionsOfTicket.add(specialOption);
            } else {
                Option viewOption = Option.fromDescription(addTicketDto.getView());
                Option characterOption = Option.fromDescription(addTicketDto.getFavouriteCharacter());

                Random random = new Random();

                switch (characterOption) {
                    case ADVENTURE_TIME:
                        characterOption = random.nextBoolean() ? Option.FINN : Option.JAKE;
                        break;
                    case GROOT:
                        characterOption = Option.WOODY;
                        break;
                    case ROGUE:
                        characterOption = Option.RICK;
                        break;
                    case KIM_POSSIBLE:
                        characterOption = Option.HARLEY_QUINN;
                        break;
                    default:
                        break;
                }

                optionsOfTicket.add(viewOption);
                optionsOfTicket.add(characterOption);
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return new ErrorDataResult<>(Messages.invalidOptions, HttpStatus.BAD_REQUEST);
        }

        var ticket = Ticket.builder()
                .owner(user)
                .event(event)
                .used(false)
                .options(optionsOfTicket)
                .isSent(false)
                .usedAt(null)
                .build();

        ticket = ticketDao.save(ticket);

        var mailResult = sendUserTicketViaMail(ticket);
        if (!mailResult.isSuccess()){
            return mailResult;
        }

        return new SuccessDataResult<Ticket>(ticket, Messages.ticketAdded, HttpStatus.CREATED);
    }

    private Result sendUserTicketViaMail(Ticket ticket) {

        var user = ticket.getOwner();
        var event = ticket.getEvent();

        var subject = event.getName()+" Etkinliğine Katılım Biletiniz: "+ticket.getOwner().getFirstName()+" "+ticket.getOwner().getLastName();
        String htmlContent;
        try {

            String mailFileName = getMailFilneNameFromTicketOptions(ticket.getOptions());
            var htmlUrl = getClass().getClassLoader().getResource(mailFileName);
            htmlContent = Files.readString(Paths.get(htmlUrl.toURI()));

           //change ticketId to string
            htmlContent = htmlContent.replace("{ticket_id}", ticket.getId().toString());
            htmlContent = htmlContent.replace("{first_name}", ticket.getOwner().getFirstName());
            htmlContent = htmlContent.replace("{last_name}", ticket.getOwner().getLastName());
            htmlContent = htmlContent.replace("{mail}", ticket.getOwner().getEmail());

            var result = emailService.sendMail(user.getEmail(), subject, htmlContent);

            if (!result.isSuccess()){
                return result;
            }

            return new SuccessDataResult<>(Messages.ticketSent, HttpStatus.OK);

        } catch (Exception e) {
            return new ErrorResult(Messages.mailNotSent, HttpStatus.BAD_REQUEST);
        }

    }

    private String getMailFilneNameFromTicketOptions(Set<Option> options) {
        if (options.contains(Option.GECENIN_YILDIZI)){
            return "katilim-mail.html"; //ozellestirilecek
        } else if (options.contains(Option.SKYDAYS)){
            return "katilim-mail-skydays.html";
        } else if (options.contains(Option.YESIL) || options.contains(Option.MAVI) || options.contains(Option.KIRMIZI) || options.contains(Option.MOR)){
            return "katilim-mail-artlab.html";
        }
        else {
            return "katilim-mail.html"; //default mail hazirlanacak
        }

    }


    @Override
    public DataResult<GetTicketDto> submitTicket(UUID ticketId) {
     var ticket = ticketDao.findById(ticketId);

      if (!ticket.isPresent()){
            return new ErrorDataResult<>(Messages.ticketNotFound, HttpStatus.NOT_FOUND);
      }

        if (ticket.get().isUsed()){
            return new ErrorDataResult<>(Messages.ticketAlreadyUsed, HttpStatus.CONFLICT);
        }


        ticket.get().setUsed(true);
        ticket.get().setUsedAt(LocalDateTime.now());

        ticket.get().getEvent().getParticipants().add(ticket.get().getOwner());

        var ticketResult = ticketDao.save(ticket.get());

        var getTicketDto = GetTicketDto.builder()
                .id(ticketResult.getId())
                .event(GetEventDto.builder()
                        .id(ticketResult.getEvent().getId())
                        .name(ticketResult.getEvent().getName())
                        .description(ticketResult.getEvent().getDescription())
                        .startDate(ticketResult.getEvent().getStartDate())
                        .endDate(ticketResult.getEvent().getEndDate())
                        .build())
                .owner(GetUserDto.builder()
                        .id(ticketResult.getOwner().getId())
                        .firstName(ticketResult.getOwner().getFirstName())
                        .lastName(ticketResult.getOwner().getLastName())
                        .email(ticketResult.getOwner().getEmail())
                        .phoneNumber(ticketResult.getOwner().getPhoneNumber())
                        .build())
                .isUsed(ticketResult.isUsed())
                .usedAt(ticketResult.getUsedAt())
                .options(ticketResult.getOptions())
                .build();

        return new SuccessDataResult<>(getTicketDto, Messages.ticketSubmitted, HttpStatus.OK);



    }

    @Override
    public DataResult<Ticket> getTicketByUserIdAndEventId(UUID userId, UUID eventId) {
        if (userId == null || eventId == null){
            return new ErrorDataResult<>(Messages.fieldsCannotBeNull, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var result = ticketDao.findByOwnerIdAndEventId(userId, eventId);

        if (result.isEmpty()){
            return new ErrorDataResult<>(Messages.ticketNotFound, HttpStatus.NOT_FOUND);
        }

        return new SuccessDataResult<Ticket>(result.get(), Messages.ticketFound, HttpStatus.OK);
    }

    @Override
    public DataResult<GetTicketDto> getTicketById(UUID ticket) {
        var result = ticketDao.findById(ticket);

        if (result.isEmpty()){
            return new ErrorDataResult<>(Messages.ticketNotFound, HttpStatus.NOT_FOUND);
        }


        var getTicketDto = GetTicketDto.builder()
                .id(result.get().getId())
                .event(GetEventDto.builder()
                        .id(result.get().getEvent().getId())
                        .name(result.get().getEvent().getName())
                        .description(result.get().getEvent().getDescription())
                        .startDate(result.get().getEvent().getStartDate())
                        .endDate(result.get().getEvent().getEndDate())
                        .build())
                .owner(GetUserDto.builder()
                        .id(result.get().getOwner().getId())
                        .firstName(result.get().getOwner().getFirstName())
                        .lastName(result.get().getOwner().getLastName())
                        .email(result.get().getOwner().getEmail())
                        .phoneNumber(result.get().getOwner().getPhoneNumber())
                        .yildizskylabId(result.get().getOwner().getYildizskylabId())
                        .build())
                .isUsed(result.get().isUsed())
                .usedAt(result.get().getUsedAt())
                .options(result.get().getOptions())
                .build();

        return new SuccessDataResult<GetTicketDto>(getTicketDto, Messages.ticketFound, HttpStatus.OK);
    }
}
