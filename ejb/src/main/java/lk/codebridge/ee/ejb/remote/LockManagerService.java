package lk.codebridge.ee.ejb.remote;

import jakarta.ejb.Remote;

import java.util.concurrent.locks.ReentrantLock;

@Remote
public interface LockManagerService {

    ReentrantLock getLockForProduct(int productId);
}
