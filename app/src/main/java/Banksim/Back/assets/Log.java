package Banksim.Back.assets;

import java.time.LocalDate;

public class Log {
    private String request;
    private String tag;
    private LocalDate date;

    public Log(String request, String tag) {
        this.request = request;
        this.tag = tag;
        this.date = LocalDate.now(); // Capture the current date
    }

    // Getter methods for amount, receiver, and date
    public String getRequest() {
        return request;
    }

    public  String getTag(){
        return tag;
    }

    public LocalDate getDate() {
        return date;
    }

    public String show(){
        return String.format("Request: %s, From: %s, Date: %s", request, tag, date);
    }
}