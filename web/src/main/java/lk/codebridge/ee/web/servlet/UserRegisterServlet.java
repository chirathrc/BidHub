package lk.codebridge.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.codebridge.ee.core.dto.ResponseDTO;
import lk.codebridge.ee.core.dto.UserDTO;
import lk.codebridge.ee.ejb.remote.User;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/register")
public class UserRegisterServlet extends HttpServlet {

    @EJB
    private User user;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        ResponseDTO responseDTO = new ResponseDTO();

        UserDTO userDTO = gson.fromJson(req.getReader(), UserDTO.class);

        if (user.register(userDTO)) {

            responseDTO.setMessage("Register successful");
            responseDTO.setSuccess(true);

        } else {
            responseDTO.setMessage("Already Registered User");
            responseDTO.setSuccess(false);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));

    }
}
