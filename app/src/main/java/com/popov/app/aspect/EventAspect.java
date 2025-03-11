package com.popov.app.aspect;

import com.popov.app.dto.event.EventCreateDTO;
import com.popov.app.dto.event.EventUpdateDTO;
import com.popov.app.service.EmailServiceDecorator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
public class EventAspect {

    private final EmailServiceDecorator emailService;

    public EventAspect(EmailServiceDecorator emailService) {
        this.emailService = emailService;
    }

    @Before("execution(* com.popov.app.controller.EventController.createEvent(..)) && args(eventCreateDTO, ..)")
    public void setDefaultDateForCreateEvent(JoinPoint joinPoint, EventCreateDTO eventCreateDTO) {
        if (eventCreateDTO.getDate() == null) {
            eventCreateDTO.setDate(LocalDateTime.now().plusDays(7));

            emailService.notifyAspectExecution("setDefaultDateForCreateEvent", "Default date set to: " + eventCreateDTO.getDate());
        }
    }

    @Before("execution(* com.popov.app.controller.EventController.updateEvent(..)) && args(id, eventUpdateDTO, ..)")
    public void setDefaultDateForUpdateEvent(JoinPoint joinPoint, UUID id, EventUpdateDTO eventUpdateDTO) {
        if (eventUpdateDTO.getDate() == null) {
            eventUpdateDTO.setDate(LocalDateTime.now().plusDays(7));

            emailService.notifyAspectExecution("setDefaultDateForUpdateEvent", "Default date set to: " + eventUpdateDTO.getDate());
        }
    }
}
