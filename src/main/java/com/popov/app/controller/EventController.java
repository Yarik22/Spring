package com.popov.app.controller;

import com.popov.app.dto.event.EventCreateDTO;
import com.popov.app.dto.event.EventResponseDTO;
import com.popov.app.dto.event.EventUpdateDTO;
import com.popov.app.model.Event;
import com.popov.app.service.EventService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;



@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String getAllEvents(Model model) {
        List<EventResponseDTO> eventDtos = eventService.getAllEvents().stream()
                .map(event -> convertToEventResponseDTO(event))
                .collect(Collectors.toList());
        model.addAttribute("events", eventDtos);
        return "event/list";
    }

    @GetMapping("/create")
    public String showCreateEventForm(Model model) {
        model.addAttribute("event", new EventCreateDTO());
        return "event/create";
    }

    @PostMapping("/create")
    public String createEvent(@ModelAttribute("event") @Valid EventCreateDTO eventCreateDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "event/create";
        }
        eventService.createEvent(convertToEvent(eventCreateDTO));
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditEventForm(@PathVariable UUID id, Model model) {
        EventResponseDTO eventDto = eventService.getEventById(id)
                .map(this::convertToEventResponseDTO)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: " + id));
                eventDto.setDate(null);
        model.addAttribute("event", eventDto);
        return "event/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable UUID id, @ModelAttribute("event") @Valid EventUpdateDTO eventUpdateDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "event/edit";
        }
        eventService.updateEvent(id, convertToEvent(eventUpdateDTO));
        return "redirect:/events";
    }

    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }

    private Event convertToEvent(EventCreateDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        return event;
    }

    private Event convertToEvent(EventUpdateDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setDate(dto.getDate());
        return event;
    }

    private EventResponseDTO convertToEventResponseDTO(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setDate(event.getDate());
        return dto;
    }
}

@Aspect
@Component
class LoggingAspect {
    @AfterReturning(pointcut = "execution(* com.popov.app.controller.EventController.*(..))", returning = "result")
    public void logMethodArguments(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        System.out.println("Method: " + methodName);
        System.out.println("Arguments: " + Arrays.toString(args));
        System.out.println("Returned:" + result);
    }
}

@Aspect
@Component
class EventAspect {

    @Before("execution(* com.popov.app.controller.EventController.createEvent(..)) && args(eventCreateDTO, ..)")
    public void setDefaultDateForCreateEvent(JoinPoint joinPoint, EventCreateDTO eventCreateDTO) {
        if (eventCreateDTO.getDate() == null) {
            eventCreateDTO.setDate(LocalDateTime.now().plusDays(7));
        }
    }

    @Before("execution(* com.popov.app.controller.EventController.updateEvent(..)) && args(id, eventUpdateDTO, ..)")
    public void setDefaultDateForUpdateEvent(JoinPoint joinPoint, UUID id, EventUpdateDTO eventUpdateDTO) {
        if (eventUpdateDTO.getDate() == null) {
            eventUpdateDTO.setDate(LocalDateTime.now().plusDays(7));
        }
    }
}