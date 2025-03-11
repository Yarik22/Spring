package com.popov.app.model;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "entertainments") 
public class Entertainment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;
    private String genre;
    private String description;
    private int duration;
    private String rating;

    public Entertainment() {}

    public Entertainment(String title, String genre, String description, int duration, String rating) {
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.duration = duration;
        this.rating = rating;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
