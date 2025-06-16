package lk.codebridge.ee.ejb.bean;

import jakarta.ejb.Singleton;
import lk.codebridge.ee.core.model.BIDModel;
import lk.codebridge.ee.ejb.remote.BidHistoryService;

import java.util.*;

@Singleton
public class BidHistorySessionBean implements BidHistoryService {

    private final Map<String, List<BIDModel>> productBids = new HashMap<>();

    @Override
    public synchronized void addBid(String productId, BIDModel bid) {

        productBids.computeIfAbsent(productId, k -> new ArrayList<>()).add(bid);

    }

    @Override
    public List<BIDModel> getBidHistory(String productId) {

        //        Collections.reverse(bidHistory);

        return productBids.getOrDefault(productId, Collections.emptyList());

    }
}
