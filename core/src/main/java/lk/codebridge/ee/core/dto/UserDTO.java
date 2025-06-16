package lk.codebridge.ee.core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserDTO implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserDTO(int id, String firstName, String lastName, LocalDateTime registrationTime, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
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

}
