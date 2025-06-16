package lk.codebridge.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.codebridge.ee.core.dto.AutoBidDataDTO;
import lk.codebridge.ee.core.model.UserModel;
import lk.codebridge.ee.ejb.bean.AutoBidManagerBean;

import java.util.List;

@Remote
public interface AutoBidManagerService {
    boolean registerAutoBid(int productId, UserModel user, double maxBid);
    void triggerAutoBid(int productId);
    List<AutoBidManagerBean.AutoBidEntry> getAutoBidEntries(String productId);
    AutoBidDataDTO getAutoBidData(String productId, UserModel user);
    Boolean unregisterAutoBid(int productId, UserModel user);

    void pauseAutoBidTemporarily(int productId, int delayMillis); // ✅ Added method
}
