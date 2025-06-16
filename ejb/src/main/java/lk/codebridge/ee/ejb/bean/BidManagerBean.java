package lk.codebridge.ee.ejb.bean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import lk.codebridge.ee.core.dto.AutoBidDataDTO;
import lk.codebridge.ee.core.model.BIDModel;
import lk.codebridge.ee.core.model.ProductModel;
import lk.codebridge.ee.core.model.UserModel;
import lk.codebridge.ee.ejb.remote.*;

import java.time.LocalDateTime;

@Singleton
public class BidManagerBean implements BidManagerService {

    private static final int BID_TYPE_AUTO = 1;
    private static final int BID_TYPE_MANUAL = 2;
    private static final long AUTO_BID_PAUSE_DELAY_MS = 2000;

    @EJB
    private BidHistoryService bidHistoryService;
    @EJB
    private AuctionItemService auctionItemService;
    @EJB
    private AutoBidManagerService autoBidManagerService;

    @EJB
    private BidBroadcastService bidBroadcastService;

    @Resource
    private ManagedExecutorService executor;

    @Override
    public boolean placeBid(String productIdStr, UserModel user, double bidAmount, int type) {

        int productId;

        //bid validation
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            return false;  // invalid product ID
        }

        ProductModel productModel = auctionItemService.getProductByID(productId);
        if (productModel == null) {
            return false;
        }

        synchronized (productModel) {
            if (bidAmount <= productModel.getCurrentHighestBid() ||
                    bidAmount < productModel.getStartingPrice() ||
                    LocalDateTime.now().isAfter(productModel.getEndDate())) {
                return false;
            }

            productModel.setCurrentHighestBid(bidAmount);
            productModel.setHighestBidder(user);
            productModel.setBidCount(productModel.getBidCount() + 1);
            auctionItemService.addToList(productModel.getId(), productModel);
        }

        BIDModel bidModel = new BIDModel(user, bidAmount, LocalDateTime.now());
        bidModel.setType(type == BID_TYPE_AUTO ? "AutoBid" : "ManualBid");
        bidHistoryService.addBid(productIdStr, bidModel);

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("productId", productModel.getId());
        jsonObject.addProperty("bidCount", productModel.getBidCount());
        jsonObject.add("bidHistory", gson.toJsonTree(bidHistoryService.getBidHistory(productIdStr)));
        jsonObject.add("bidModel", gson.toJsonTree(bidModel));

        AutoBidDataDTO autoBidDataDTO = autoBidManagerService.getAutoBidData(productIdStr, user);
        if (autoBidDataDTO != null) {
            jsonObject.add("isHaveAutoBid", gson.toJsonTree(autoBidDataDTO));
        }

        bidBroadcastService.sendBidUpdate(gson.toJson(jsonObject));

        if (type == BID_TYPE_MANUAL) {
            autoBidManagerService.pauseAutoBidTemporarily(productId, (int) AUTO_BID_PAUSE_DELAY_MS);
        } else {
            executor.execute(() -> autoBidManagerService.triggerAutoBid(productId));
        }

        return true;
    }

    @Override
    public double getCurrentHighestBid(String productIdStr) {
        try {
            int productId = Integer.parseInt(productIdStr);
            ProductModel productModel = auctionItemService.getProductByID(productId);
            return productModel != null ? productModel.getCurrentHighestBid() : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public UserModel getHighestBidder(String productIdStr) {
        try {
            int productId = Integer.parseInt(productIdStr);
            ProductModel productModel = auctionItemService.getProductByID(productId);
            return productModel != null ? productModel.getHighestBidder() : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
