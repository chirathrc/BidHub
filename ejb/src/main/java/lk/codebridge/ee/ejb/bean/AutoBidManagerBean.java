package lk.codebridge.ee.ejb.bean;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import lk.codebridge.ee.core.dto.AutoBidDataDTO;
import lk.codebridge.ee.core.model.ProductModel;
import lk.codebridge.ee.core.model.UserModel;
import lk.codebridge.ee.ejb.remote.AuctionItemService;
import lk.codebridge.ee.ejb.remote.AutoBidManagerService;
import lk.codebridge.ee.ejb.remote.BidManagerService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class AutoBidManagerBean implements AutoBidManagerService {

    @EJB
    private BidManagerService bidManagerService;

    @EJB
    private AuctionItemService auctionItemService;

    @Resource
    private ManagedExecutorService executor;

    private final Map<Integer, List<AutoBidEntry>> autoBidMap = new ConcurrentHashMap<>();
    private final Map<Integer, Boolean> pausedProducts = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> lastBidderIndexMap = new ConcurrentHashMap<>();

    // Track last auto-bidder index per product for round-robin
//    private final Map<Integer, Integer> lastBidderIndexMap = new ConcurrentHashMap<>();


    @Override
    public boolean registerAutoBid(int productId, UserModel user, double maxBid) {
        if (productId == 0 || user == null) return false;

        List<AutoBidEntry> entries = autoBidMap.computeIfAbsent(productId, k -> new ArrayList<>());

        synchronized (entries) {
            entries.removeIf(entry -> entry.user.getEmail().equals(user.getEmail()));
            entries.add(new AutoBidEntry(user, maxBid));
        }

        ProductModel productModel = auctionItemService.getProductByID(productId);
        if (productModel.getCurrentHighestBid() != 0 &&
                productModel.getCurrentHighestBid() + 20 < maxBid &&
                !productModel.getHighestBidder().getEmail().equals(user.getEmail())) {
            bidManagerService.placeBid(String.valueOf(productId), user, productModel.getCurrentHighestBid() + 10.00, 1);
        } else if (productModel.getCurrentHighestBid() == 0) {
            bidManagerService.placeBid(String.valueOf(productId), user, productModel.getStartingPrice(), 1);
        }

        return true;
    }

    // Give Chance for All Bidders Equally
    //AutoBid
    @Override
    public void triggerAutoBid(int productId) {
        if (pausedProducts.getOrDefault(productId, false)) {
            System.out.println("Auto-bid paused for product: " + productId);
            return;
        }

        List<AutoBidEntry> originalEntries = autoBidMap.get(productId);
        if (originalEntries == null || originalEntries.isEmpty()) return;

        List<AutoBidEntry> entriesSnapshot;
        synchronized (originalEntries) {
            entriesSnapshot = new ArrayList<>(originalEntries);
        }

        ProductModel product = auctionItemService.getProductByID(productId);
        double currentBid = product.getCurrentHighestBid();
        double minNextBid = currentBid + 10;

        int totalBidders = entriesSnapshot.size();
        if (totalBidders == 0) return;

        int lastIndex = lastBidderIndexMap.getOrDefault(productId, -1);
        int startIndex = (lastIndex + 1) % totalBidders;

        for (int i = 0; i < totalBidders; i++) {
            int idx = (startIndex + i) % totalBidders;
            AutoBidEntry entry = entriesSnapshot.get(idx);

            boolean isHighest = entry.user.getEmail().equals(product.getHighestBidder().getEmail());
            if (!isHighest && entry.maxBid >= minNextBid) {
                executor.execute(() -> {
                    try {
                        long remainingSeconds = java.time.Duration.between(java.time.LocalDateTime.now(), product.getEndDate()).getSeconds();
                        if (remainingSeconds > 300) {
                            Thread.sleep(10000); // 10 seconds delay if auction end > 5 mins
                        }
                    } catch (InterruptedException ignored) {
                    }

                    boolean placed = bidManagerService.placeBid(String.valueOf(productId), entry.user, minNextBid, 1);
                    if (placed) {
                        lastBidderIndexMap.put(productId, idx);
                    }
                });
                break;
            } else if (entry.maxBid < minNextBid) {
                synchronized (originalEntries) {
                    originalEntries.remove(entry);
                }
            }
        }
    }

    @Override
    public List<AutoBidEntry> getAutoBidEntries(String productId) {
        return autoBidMap.getOrDefault(Integer.parseInt(productId), Collections.emptyList());
    }

    @Override
    public AutoBidDataDTO getAutoBidData(String productId, UserModel user) {
        if (productId == null || user == null) return null;

        List<AutoBidEntry> entries = autoBidMap.get(Integer.parseInt(productId));
        if (entries == null || entries.isEmpty()) return null;

        synchronized (entries) {
            for (AutoBidEntry entry : entries) {
                if (entry.user.getEmail().equals(user.getEmail())) {
                    AutoBidDataDTO dto = new AutoBidDataDTO();
                    dto.setAutoValue(entry.maxBid);
                    dto.setUser(entry.user);
                    dto.setProductModel(auctionItemService.getProductByID(Integer.parseInt(productId)));
                    return dto;
                }
            }
        }
        return null;
    }

    @Override
    public Boolean unregisterAutoBid(int productId, UserModel user) {
        if (productId == 0 || user == null) return false;

        List<AutoBidEntry> entries = autoBidMap.get(productId);
        if (entries == null || entries.isEmpty()) return false;

        synchronized (entries) {
            entries.removeIf(entry -> entry.user.getEmail().equals(user.getEmail()));
        }
        return true;
    }

    @Override
    public void pauseAutoBidTemporarily(int productId, int delayMillis) {
        pausedProducts.put(productId, true);
        executor.execute(() -> {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException ignored) {
            }
            pausedProducts.put(productId, false);
            triggerAutoBid(productId);
        });
    }

    public static class AutoBidEntry {
        UserModel user;
        double maxBid;

        public AutoBidEntry(UserModel user, double maxBid) {
            this.user = user;
            this.maxBid = maxBid;
        }
    }
}


//Get Bidder Randomly
//    @Override
//    public void triggerAutoBid(int productId) {
//        if (pausedProducts.getOrDefault(productId, false)) {
//            System.out.println("Auto-bid paused for product: " + productId);
//            return;
//        }
//
//        List<AutoBidEntry> originalEntries = autoBidMap.get(productId);
//        if (originalEntries == null || originalEntries.isEmpty()) return;
//
//        List<AutoBidEntry> entriesSnapshot;
//        synchronized (originalEntries) {
//            entriesSnapshot = new ArrayList<>(originalEntries);
//        }
//
//        ProductModel product = auctionItemService.getProductByID(productId);
//        double currentBid = product.getCurrentHighestBid();
//        double minNextBid = currentBid + 10;
//
//        AutoBidEntry bestCandidate = null;
//        double highestMaxBid = 0;
//
//        for (AutoBidEntry entry : entriesSnapshot) {
//            boolean isHighest = entry.user.getEmail().equals(product.getHighestBidder().getEmail());
//
//            if (!isHighest && entry.maxBid >= minNextBid && entry.maxBid > highestMaxBid) {
//                bestCandidate = entry;
//                highestMaxBid = entry.maxBid;
//            } else if (entry.maxBid < minNextBid) {
//                synchronized (originalEntries) {
//                    originalEntries.remove(entry);
//                }
//            }
//        }
//
//        if (bestCandidate != null) {
//            bidManagerService.placeBid(String.valueOf(productId), bestCandidate.user, minNextBid, 1);
//        }
//    }


//Give Chance for All bidders equaly
//    @Override
//    public void triggerAutoBid(int productId) {
//        if (pausedProducts.getOrDefault(productId, false)) {
//            System.out.println("Auto-bid paused for product: " + productId);
//            return;
//        }
//
//        List<AutoBidEntry> originalEntries = autoBidMap.get(productId);
//        if (originalEntries == null || originalEntries.isEmpty()) return;
//
//        List<AutoBidEntry> entriesSnapshot;
//        synchronized (originalEntries) {
//            entriesSnapshot = new ArrayList<>(originalEntries);
//        }
//
//        ProductModel product = auctionItemService.getProductByID(productId);
//        double currentBid = product.getCurrentHighestBid();
//        double minNextBid = currentBid + 10;
//
//        int totalBidders = entriesSnapshot.size();
//        if (totalBidders == 0) return;
//
//        int lastIndex = lastBidderIndexMap.getOrDefault(productId, -1);
//        int startIndex = (lastIndex + 1) % totalBidders;
//
//        // Try bidders in round-robin order starting from startIndex
//        for (int i = 0; i < totalBidders; i++) {
//            int idx = (startIndex + i) % totalBidders;
//            AutoBidEntry entry = entriesSnapshot.get(idx);
//
//            boolean isHighest = entry.user.getEmail().equals(product.getHighestBidder().getEmail());
//            if (!isHighest && entry.maxBid >= minNextBid) {
//                boolean placed = bidManagerService.placeBid(String.valueOf(productId), entry.user, minNextBid, 1);
//                if (placed) {
//                    lastBidderIndexMap.put(productId, idx); // Update last bidder index
//                    break; // One bid per trigger
//                }
//            } else if (entry.maxBid < minNextBid) {
//                synchronized (originalEntries) {
//                    originalEntries.remove(entry);
//                }
//            }
//        }
//    }

