package com.popov.main.data;

import lombok.Data;

@Data
public class Entertainment {
    private String id;
    private String title;
    private String genre;
    private String description;
    private int duration;
    private String rating;
}
