package model;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Objects;

public class Person {
    private final StringProperty firstname = new SimpleStringProperty(this, "firstname", "");
    private final StringProperty lastname = new SimpleStringProperty(this, "lastname", "");
    private final StringProperty notes = new SimpleStringProperty(this, "notes", "sample notes");
    private LocalDate dateOfBirth;
    private String gender;
    private String image;

    public Person() {
    }

    public Person(String firstname, String lastname, String notes, LocalDate dateOfBirth, String gender) {
        this.firstname.set(firstname);
        this.lastname.set(lastname);
        this.notes.set(notes);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        if(gender == "Male"){
            this.image = "resources/images/male-icon.png";
        } else {
            this.image = "resources/images/female-icon.png";
        }
    }

    public Person(String firstname, String lastname, String notes, LocalDate dateOfBirth, String gender, String image) {
        this.firstname.set(firstname);
        this.lastname.set(lastname);
        this.notes.set(notes);
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.image = image;
    }

    public String getFirstname() {
        return firstname.get();
    }
    public StringProperty firstnameProperty() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }
    public String getLastname() {
        return lastname.get();
    }
    public StringProperty lastnameProperty() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }
    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        if (gender.equals("Female") || gender.equals("Male") || gender.equals("Prefer not to mention")){
            this.gender = gender;
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return firstname.get() + " " + lastname.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(firstname, person.firstname) &&
                Objects.equals(lastname, person.lastname) &&
                Objects.equals(notes, person.notes) &&
                Objects.equals(dateOfBirth, person.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, notes, dateOfBirth);
    }

    public static Callback<Person, Observable[]> extractor =
            p-> new Observable[] {
                    p.lastnameProperty(), p.firstnameProperty()
            };


}

