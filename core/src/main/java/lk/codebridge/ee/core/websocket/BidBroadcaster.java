package lk.codebridge.ee.core.websocket;

import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

//Bid Broadcasting Class
public class BidBroadcaster {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    public static void register(Session session) {
        sessions.add(session);
    }

    public static void unregister(Session session) {
        sessions.remove(session);
    }

    public static void broadcast(String message) {
        for (Session session : sessions) {
            if (session != null && session.isOpen()) {
                try {
                    RemoteEndpoint.Basic remote = session.getBasicRemote();
                    if (remote != null) {
                        remote.sendText(message);
                    }
                } catch (IOException | IllegalStateException e) {
                    // This session probably closed during send, unregister it
                    unregister(session);
                    e.printStackTrace();
                } catch (Exception e) {
                    // Log unexpected exceptions but do not unregister automatically
                    e.printStackTrace();
                }
            } else {
                // Session is closed or null, unregister it to cleanup
                unregister(session);
            }
        }
    }
}
