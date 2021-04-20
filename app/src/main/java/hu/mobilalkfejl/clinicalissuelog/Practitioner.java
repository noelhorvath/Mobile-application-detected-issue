package hu.mobilalkfejl.clinicalissuelog;

import java.util.Date;

public class Practitioner {
    String id;
    String name;
    boolean active;
    String gender;
    Date birthDate;
    Qualification qualification;

    public Practitioner(String name, boolean active, String gender, Date birthDate, Qualification qualification) {
        this.name = name;
        this.active = active;
        this.gender = gender;
        this.birthDate = birthDate;
        this.qualification = qualification;
    }

    public Practitioner(){}

    public String _getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getName() {
        return name;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public void setName(String name) {
        this.name = name;
    }
}
