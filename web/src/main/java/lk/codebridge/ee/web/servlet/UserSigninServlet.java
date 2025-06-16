package lk.codebridge.ee.web.servlet;

import com.google.gson.Gson;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.codebridge.ee.core.dto.ResponseDTO;
import lk.codebridge.ee.core.model.UserModel;
import lk.codebridge.ee.ejb.remote.User;

import java.io.IOException;

@WebServlet("/signIn")
public class UserSigninServlet extends HttpServlet {


    @EJB
    private User user;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResponseDTO responseDTO = new ResponseDTO();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        System.out.println("email: " + email);

        UserModel userModel = user.login(email, password);

        if (userModel != null) {

            responseDTO.setMessage(userModel.getFirstName()+" "+userModel.getLastName());
            responseDTO.setSuccess(true);

            HttpSession session = req.getSession(true);
            session.setAttribute("userModel", userModel);

        }else {
            responseDTO.setMessage("Invalid Email or Password");
            responseDTO.setSuccess(false);

        }

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(responseDTO));

    }
}
