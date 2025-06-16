package lk.codebridge.ee.ejb.bean;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;
import lk.codebridge.ee.ejb.remote.BidBroadcastService;

@Stateless
public class BidBroadcastBean implements BidBroadcastService {

    @Resource(lookup = "jms/BidTopic")
    private Topic bidTopic;

    @Resource(lookup = "jms/__defaultConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Override
    public void sendBidUpdate(String json) {
        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(bidTopic, json);
        }
    }
}
