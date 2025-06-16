package lk.codebridge.ee.web.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.codebridge.ee.core.dto.ResponseDTO;

import java.io.IOException;

@WebServlet("/logOut")
public class logOut extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        if (session.getAttribute("userModel") != null) {
            System.out.println("Logout Successful");
            session.removeAttribute("userModel");
        }

        ResponseDTO responseDTO  = new ResponseDTO();
        responseDTO.setSuccess(true);

        resp.getWriter().write(new Gson().toJson(responseDTO));
    }
}
