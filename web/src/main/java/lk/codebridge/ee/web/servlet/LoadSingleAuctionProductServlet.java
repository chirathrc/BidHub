package lk.codebridge.ee.web.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lk.codebridge.ee.core.dto.AutoBidDataDTO;
import lk.codebridge.ee.core.dto.ResponseDTO;
import lk.codebridge.ee.core.model.BIDModel;
import lk.codebridge.ee.core.model.ProductModel;
import lk.codebridge.ee.core.model.UserModel;
import lk.codebridge.ee.ejb.remote.AuctionItemService;
import lk.codebridge.ee.ejb.remote.AutoBidManagerService;
import lk.codebridge.ee.ejb.remote.BidHistoryService;

import java.io.IOException;
import java.util.List;

@WebServlet("/singleAuction")
public class LoadSingleAuctionProductServlet extends HttpServlet {

    @EJB
    private AuctionItemService auctionItemService;

    @EJB
    private BidHistoryService bidHistoryService;

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
        Gson gson = new Gson();

//        if (req.getSession().getAttribute("user") == null) {
//
//            responseDTO.setSuccess(Boolean.FALSE);
//            resp.setContentType("application/json");
//            resp.getWriter().write(gson.toJson(responseDTO));
//        }

        int auctionId = Integer.parseInt(req.getParameter("Id"));


        ProductModel productModel = auctionItemService.getProductByID(auctionId);

        if (productModel != null) {

            List<BIDModel> bidHistory = bidHistoryService.getBidHistory(String.valueOf(productModel.getId()));
            JsonObject responseObject = new JsonObject();

            AutoBidDataDTO autoBidDataDTO = autoBidManagerService.getAutoBidData(String.valueOf(productModel.getId()), userModel);

            if (autoBidDataDTO != null) {
                responseObject.add("autoBidData", gson.toJsonTree(autoBidDataDTO));
            }else {
                System.out.println("AutoBidDataDTO is Null");
            }

            responseObject.add("bidHistory", gson.toJsonTree(bidHistory));
            responseObject.add("product", gson.toJsonTree(productModel));
            responseObject.add("userModel", gson.toJsonTree(userModel));

            responseDTO.setMessage(responseObject);
            responseDTO.setSuccess(true);

        } else {
            responseDTO.setMessage("Product not found");
            responseDTO.setSuccess(false);

        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));

    }
}
