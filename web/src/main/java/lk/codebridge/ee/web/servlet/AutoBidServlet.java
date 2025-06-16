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
import lk.codebridge.ee.ejb.remote.AutoBidManagerService;

import java.io.IOException;

@WebServlet("/autoBid")
public class AutoBidServlet extends HttpServlet {

    @EJB
    private AutoBidManagerService autoBidManagerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        UserModel userModel = (UserModel) session.getAttribute("userModel");

        if (userModel == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
        }

        ResponseDTO responseDTO = new ResponseDTO();

        double price = Double.parseDouble(req.getParameter("maxPrice"));
        String productId = req.getParameter("productId");

        responseDTO.setSuccess(false);
        responseDTO.setMessage("Something went wrong");

        if (autoBidManagerService.registerAutoBid(Integer.parseInt(productId), userModel, price)) {
            responseDTO.setSuccess(true);
            responseDTO.setMessage("AutoBid success.");
        }

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(responseDTO));


    }
}
