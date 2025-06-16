package lk.codebridge.ee.core.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserModel implements Serializable {

    private String firstName;
    private String lastName;
    private LocalDateTime registrationTime;
    private String email;
    private String password;

    public UserModel(int id, String firstName, String lastName, LocalDateTime registrationTime, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationTime = registrationTime;
        this.email = email;
        this.password = password;
    }

    public UserModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", registrationTime=" + registrationTime +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
