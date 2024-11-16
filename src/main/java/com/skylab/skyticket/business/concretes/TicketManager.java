package com.skylab.skyticket.business.concretes;

import com.skylab.skyticket.business.abstracts.EventService;
import com.skylab.skyticket.business.abstracts.ImageService;
import com.skylab.skyticket.business.abstracts.TicketService;
import com.skylab.skyticket.business.abstracts.UserService;
import com.skylab.skyticket.business.constants.Messages;
import com.skylab.skyticket.core.results.DataResult;
import com.skylab.skyticket.core.results.ErrorDataResult;
import com.skylab.skyticket.core.results.Result;
import com.skylab.skyticket.core.results.SuccessDataResult;
import com.skylab.skyticket.dataAccess.TicketDao;
import com.skylab.skyticket.entities.Option;
import com.skylab.skyticket.entities.Role;
import com.skylab.skyticket.entities.Ticket;
import com.skylab.skyticket.entities.User;
import com.skylab.skyticket.entities.dtos.ticket.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class TicketManager implements TicketService {

    private final TicketDao ticketDao;

    private final EventService eventService;

    private final UserService userService;

    private final ImageService imageService;

    public TicketManager(TicketDao ticketDao, EventService eventService, UserService userService, ImageService imageService) {
        this.ticketDao = ticketDao;
        this.eventService = eventService;
        this.userService = userService;
        this.imageService = imageService;
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
       }else {
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

       if (addTicketDto.getView() == null || addTicketDto.getFavouriteCharacter() == null ||addTicketDto.getView().isEmpty() || addTicketDto.getFavouriteCharacter().isEmpty()){
              return new ErrorDataResult<>(Messages.invalidOptions, HttpStatus.UNPROCESSABLE_ENTITY);
       }


       var ticketResult = getTicketByUserIdAndEventId(user.getId(), event.getId());
       if (ticketResult.isSuccess()){
           return new ErrorDataResult<>(Messages.ticketAlreadyExists, HttpStatus.CONFLICT);
       }

        Set<Option> optionsOfTicket = new HashSet<>();
        try {
            Option viewOption = Option.fromDescription(addTicketDto.getView());
            Option characterOption = Option.fromDescription(addTicketDto.getFavouriteCharacter());

            if (characterOption.equals(Option.ADVENTURE_TIME)){
                int random = (int) (Math.random() *2);

                if (random == 0){
                    characterOption = Option.FINN;

                }else {
                    characterOption = Option.JAKE;
                }
            }

                if (characterOption.equals(Option.GROOT)){
                    characterOption = Option.WOODY;
                }else if (characterOption.equals(Option.ROGUE)){
                    characterOption = Option.RICK;
                }
                else if (characterOption.equals(Option.KIM_POSSIBLE)){
                    characterOption = Option.HARLEY_QUINN;
                }


            optionsOfTicket.add(viewOption);
            optionsOfTicket.add(characterOption);

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

            ticketDao.save(ticket);

            return new SuccessDataResult<Ticket>(ticket, Messages.ticketAdded, HttpStatus.CREATED);


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
                        .university(result.get().getOwner().getUniversity())
                        .faculty(result.get().getOwner().getFaculty())
                        .department(result.get().getOwner().getDepartment())
                        .birthDate(result.get().getOwner().getBirthDate().toString())
                        .yildizskylabId(result.get().getOwner().getYildizskylabId())
                        .build())
                .isUsed(result.get().isUsed())
                .options(result.get().getOptions())
                .build();

        return new SuccessDataResult<GetTicketDto>(getTicketDto, Messages.ticketFound, HttpStatus.OK);
    }
}
