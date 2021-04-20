package hu.mobilalkfejl.clinicalissuelog;

import java.time.LocalDateTime;

public class Mitigation {
    private String action;
    private LocalDateTime date;

    public Mitigation(String action, LocalDateTime date) {
        this.action = action;
        this.date = date;
    }

    public Mitigation() {
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
