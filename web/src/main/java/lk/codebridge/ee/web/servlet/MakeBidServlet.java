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
import lk.codebridge.ee.core.model.UserModel;
import lk.codebridge.ee.ejb.remote.AuctionItemService;
import lk.codebridge.ee.ejb.remote.BidHistoryService;
import lk.codebridge.ee.ejb.remote.BidManagerService;
import lk.codebridge.ee.ejb.remote.User;

import java.io.IOException;

@WebServlet("/makeBid")
public class MakeBidServlet extends HttpServlet {

    @EJB
    private User userService;

    @EJB
    private BidManagerService bidManagerService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (req.getSession().getAttribute("userModel") == null) {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 status
            resp.setContentType("application/json");
            return;
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        String productId = jsonObject.get("productId").getAsString();
        UserModel user = (UserModel) req.getSession().getAttribute("userModel");
        System.out.println(user);
//        UserModel user = userService.login("user@example.com", "1234");

        ResponseDTO responseDTO = new ResponseDTO();

        double bidAmount = jsonObject.get("bidAmount").getAsDouble();

        if (bidManagerService.placeBid(productId, user, bidAmount,2)) {

            responseDTO.setSuccess(true);
            responseDTO.setMessage("Success");
        } else {
            responseDTO.setSuccess(false);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));

//        System.out.println("Highest Bid for Product--------"+bidManagerService.getCurrentHighestBid(productId));
//        System.out.println("Highest biddest user--------"+bidManagerService.getHighestBidder(productId));


    }
}
