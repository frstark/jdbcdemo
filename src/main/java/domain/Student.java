package domain;


import java.sql.Date;

import java.time.Instant;
import java.time.LocalDate;

public class Student extends BaseEntity {

    private String vorname;
    private String nachname;
    private Date birthDate;



    public Student(Long id, String vorname, String nachname, Date birthDate) {
        super(id);
        this.vorname = vorname;
        this.nachname = nachname;
        this.birthDate = birthDate;
    }

    public Student(String vorname, String nachname, Date birthDate) {
        super(null);
        this.vorname = vorname;
        this.nachname = nachname;
        this.birthDate = birthDate;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        if (vorname != null ) {
            this.vorname = vorname;
        } else {
            throw new InvalidValueException("Vorname darf nicht leer sein");
        }
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        if (nachname != null ) {
            this.nachname = nachname;
        } else {
            throw new InvalidValueException("Nachname darf nicht leer sein");
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        if (birthDate != null) {
            if (this.birthDate != null) {

                if (birthDate.before(Date.from(Instant.from(LocalDate.now())))) {
                    this.birthDate = birthDate;
                } else {
                    throw new InvalidValueException("Geburtsdatum kann nicht in der Zukunft liegen");
                }
            } else {
                this.birthDate = birthDate;
            }
        } else {
            throw new InvalidValueException("Geburtsdatum darf nicht null / leer sein!");
        }
    }
}
