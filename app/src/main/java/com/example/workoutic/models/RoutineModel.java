package com.example.workoutic.models;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RoutineModel implements Serializable {
    private long id;
    private String name;
    private Long timestamp;

    public RoutineModel() {
    }

    public RoutineModel(int id, String name, Long timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = "";
        try{
            formattedDateTime=  formatter.format(currentDateTime);
        }catch (Exception e){
            e.printStackTrace();
        }

        LocalDateTime localDate = LocalDateTime.parse(formattedDateTime, formatter);
        return localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public String LongToDateTimeString(){
        LocalDateTime datetime = Instant.ofEpochMilli(this.timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = "";
        try{
            formattedDateTime=  formatter.format(datetime);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  formattedDateTime;
    }
}
