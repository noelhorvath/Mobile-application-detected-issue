package hu.mobilalkfejl.clinicalissuelog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DetectedIssue {
    private String id;
    private String status;
    private String code;
    private String severity;
    private String patient;
    private LocalDate identifiedDateTime;
    private String author;
    private String detail;
    private Mitigation[] mitigation;

    public DetectedIssue(String status, String code, String severity, String patient, LocalDate identifiedDateTime, String author, String detail) {
        this.status = status;
        this.code = code;
        this.severity = severity;
        this.patient = patient;
        this.identifiedDateTime = identifiedDateTime;
        this.author = author;
        this.detail = detail;
        this.mitigation = null;
    }

    public DetectedIssue(){}


    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public void setIdentifiedDateTime(LocalDate identifiedDateTime) {
        this.identifiedDateTime = identifiedDateTime;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setMitigation(Mitigation[] mitigation) {
        this.mitigation = mitigation;
    }

    public String _getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getSeverity() {
        return severity;
    }

    public String getPatient() {
        return patient;
    }

    public LocalDate getIdentifiedDateTime() {
        return identifiedDateTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getDetail() {
        return detail;
    }

    public Mitigation[] getMitigation() {
        return mitigation;
    }
}
