package lk.codebridge.ee.ejb.remote;

import jakarta.ejb.Remote;

@Remote
public interface BidBroadcastService {
    void sendBidUpdate(String json);
}
