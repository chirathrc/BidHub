package lk.codebridge.ee.ejb.bean;

import jakarta.ejb.Singleton;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
public class LockManager {

    private final ConcurrentHashMap<Integer, ReentrantLock> productLocks = new ConcurrentHashMap<>();

    public ReentrantLock getLockForProduct(int productId) {
        return productLocks.computeIfAbsent(productId, k -> new ReentrantLock());
    }
}
