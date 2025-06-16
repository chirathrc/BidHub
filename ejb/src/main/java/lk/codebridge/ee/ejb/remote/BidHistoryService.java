package lk.codebridge.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.codebridge.ee.core.model.BIDModel;

import java.util.List;

@Remote
public interface BidHistoryService {

    void addBid(String productId, BIDModel bid);
    List<BIDModel> getBidHistory(String productId);
}
