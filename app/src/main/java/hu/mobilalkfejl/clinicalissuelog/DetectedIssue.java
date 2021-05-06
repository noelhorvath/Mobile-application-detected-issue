package hu.mobilalkfejl.clinicalissuelog;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.time.LocalDateTime;

public class DetectedIssue {
    private String id;
    private String status;
    private String code;
    private String severity;
    private String patient;
    private Timestamp identifiedDateTime;
    private String author;
    private String detail;

    public DetectedIssue(String status, String code, String severity, String patient, Timestamp identifiedDateTime, String author, String detail) {
        this.status = status;
        this.code = code;
        this.severity = severity;
        this.patient = patient;
        this.identifiedDateTime = identifiedDateTime;
        this.author = author;
        this.detail = detail;
    }

    public DetectedIssue(){
    }

    public void setId(String id) { this.id = id; }

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

    public void setIdentifiedDateTime(Timestamp identifiedDateTime) {
        this.identifiedDateTime = identifiedDateTime;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String _getId() { return id; }

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

    public Timestamp getIdentifiedDateTime() {
        return identifiedDateTime;
    }

    public String getAuthor() {
        return author;
    }

    public String getDetail() {
        return detail;
    }

}
