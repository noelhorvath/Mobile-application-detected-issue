package hu.mobilalkfejl.clinicalissuelog;

import java.time.LocalDateTime;

public class Mitigation {
    private String action;
    private LocalDateTime date;
    private String author;

    public Mitigation(String action, LocalDateTime date, String author) {
        this.action = action;
        this.date = date;
        this.author = author;
    }

    public Mitigation(){}

    public String getAction() {
        return action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
