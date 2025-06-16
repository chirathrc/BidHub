package lk.codebridge.ee.web.socket;

import jakarta.ejb.EJB;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lk.codebridge.ee.core.websocket.BidBroadcaster;

@ServerEndpoint("/ws/bids")
public class BidWebSocket {

    @OnOpen
    public void onOpen(Session session) {
        BidBroadcaster.register(session);
    }

    @OnClose
    public void onClose(Session session) {
        BidBroadcaster.unregister(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Optional: handle incoming messages
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        BidBroadcaster.unregister(session);
        throwable.printStackTrace();
    }
}
