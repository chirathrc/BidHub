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
import lk.codebridge.ee.core.model.ProductModel;
import lk.codebridge.ee.ejb.remote.AuctionItemService;
import lk.codebridge.ee.ejb.remote.BidHistoryService;

import java.io.IOException;
import java.util.List;

@WebServlet("/loadAuctionItems")
public class LoadAuctionProductServlet extends HttpServlet {

    @EJB
    private AuctionItemService auctionItemService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        ResponseDTO  responseDTO = new ResponseDTO();
        List<ProductModel> productModels = auctionItemService.getProducts();

        JsonObject obj = new JsonObject();
        obj.add("auctionProducts", gson.toJsonTree(productModels));

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(obj));
    }
}
