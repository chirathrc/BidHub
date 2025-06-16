package lk.codebridge.ee.ejb.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import lk.codebridge.ee.core.dto.UserDTO;
import lk.codebridge.ee.core.model.UserModel;
import lk.codebridge.ee.ejb.remote.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Stateless
public class UserSessionBean implements User {


    private static final ConcurrentHashMap<String, UserModel> usersByEmail = new ConcurrentHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    static {
        UserModel defaultUserOne = new UserModel();
        defaultUserOne.setFirstName("Gayan");
        defaultUserOne.setLastName("Rodrigo");
        defaultUserOne.setEmail("gayan@gmail.com");
        defaultUserOne.setPassword("1234"); // In production, hash passwords!

        usersByEmail.put(defaultUserOne.getEmail(), defaultUserOne);

        UserModel defaultUserTwo = new UserModel();
        defaultUserTwo.setFirstName("Chirath");
        defaultUserTwo.setLastName("Rothila");
        defaultUserTwo.setEmail("chirathrc@gmail.com");
        defaultUserTwo.setPassword("1234"); // In production, hash passwords!

        usersByEmail.put(defaultUserTwo.getEmail(), defaultUserTwo);

        UserModel defaultUserThree = new UserModel();
        defaultUserThree.setFirstName("Kamal");
        defaultUserThree.setLastName("Perera");
        defaultUserThree.setEmail("kamal@gmail.com");
        defaultUserThree.setPassword("1234"); // In production, hash passwords!

        usersByEmail.put(defaultUserThree.getEmail(), defaultUserThree);
    }

    @Override
    public Boolean register(UserDTO user) {

        if (usersByEmail.containsKey(user.getEmail())) {
            System.out.println("User already exists");
            return false;
        }

        UserModel userModel = new UserModel();
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setEmail(user.getEmail());
        userModel.setPassword(user.getPassword());

        usersByEmail.put(user.getEmail(),userModel);
        System.out.println("Successfully registered user " + userModel);
        return true;

    }

    @Override
    public UserModel login(String email, String password) {

//        System.out.println("Attempting to login user " + email+" "+password);

        if (usersByEmail.containsKey(email)) {
            UserModel userModel = usersByEmail.get(email);

            if (userModel.getPassword().equals(password)) {
                return userModel;

            }else {
                return null;
            }

        }else {
            return null;
        }

    }

}
