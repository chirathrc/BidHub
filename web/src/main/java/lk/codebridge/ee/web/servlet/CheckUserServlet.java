package lk.codebridge.ee.web.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.codebridge.ee.core.dto.ResponseDTO;
import lk.codebridge.ee.core.model.UserModel;

import java.io.IOException;

@WebServlet("/checkUser")
public class CheckUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        UserModel userModel = (UserModel) session.getAttribute("userModel");
        System.out.println(userModel);

        ResponseDTO responseDTO = new ResponseDTO();

        if (userModel == null) {

            responseDTO.setSuccess(false);

        } else {
            responseDTO.setSuccess(true);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(responseDTO));

    }
}
