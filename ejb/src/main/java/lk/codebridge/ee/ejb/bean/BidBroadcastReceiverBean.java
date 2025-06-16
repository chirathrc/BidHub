package lk.codebridge.ee.ejb.bean;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import lk.codebridge.ee.core.websocket.BidBroadcaster;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/BidTopic")
        }
)

public class BidBroadcastReceiverBean implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String json = textMessage.getText();
                BidBroadcaster.broadcast(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}