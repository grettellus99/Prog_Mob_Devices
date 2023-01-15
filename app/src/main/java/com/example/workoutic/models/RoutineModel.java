package com.example.workoutic.models;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RoutineModel implements Serializable {
    private int id;
    private String name;
    private Long timestamp;

    public RoutineModel() {
    }

    public RoutineModel(int id, String name, Long timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long dateTimeToLong(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.getDefault());
        String formattedDateTime = currentDateTime.format(formatter);
        LocalDateTime localDate = LocalDateTime.parse(formattedDateTime, formatter);
        return localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
