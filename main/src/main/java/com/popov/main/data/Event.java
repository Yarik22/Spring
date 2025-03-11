package com.popov.main.data;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Event {
    private String id;
    private String name;
    private String description;
    private String location;
    private LocalDateTime date;
}
