package lk.codebridge.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.codebridge.ee.core.dto.UserDTO;
import lk.codebridge.ee.core.model.UserModel;

@Remote
public interface User {

    Boolean register(UserDTO user);
    UserModel login(String email, String password);

}
