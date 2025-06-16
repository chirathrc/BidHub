package lk.codebridge.ee.ejb.remote;


import jakarta.ejb.Remote;
import lk.codebridge.ee.core.model.UserModel;

@Remote
public interface BidManagerService {

    boolean placeBid(String productId, UserModel user, double bidAmount,int type);

    double getCurrentHighestBid(String productId);

    UserModel getHighestBidder(String productId);

}
