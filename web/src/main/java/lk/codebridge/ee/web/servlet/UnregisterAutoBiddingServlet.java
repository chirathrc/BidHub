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
import lk.codebridge.ee.ejb.bean.AutoBidManagerBean;
import lk.codebridge.ee.ejb.remote.AutoBidManagerService;

import java.io.IOException;

@WebServlet("/unregisterAuto")
public class UnregisterAutoBiddingServlet extends HttpServlet {

    @EJB
    private AutoBidManagerService autoBidManagerBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        UserModel userModel = (UserModel) session.getAttribute("userModel");

        if (userModel == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
        }

        ResponseDTO responseDTO = new ResponseDTO();

        String productId = req.getParameter("productId");

        if (autoBidManagerBean.unregisterAutoBid(Integer.parseInt(productId), userModel)) {
            responseDTO.setSuccess(true);
        }else  {
            responseDTO.setSuccess(false);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(responseDTO));

    }
}
