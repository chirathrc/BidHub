package lk.codebridge.ee.core.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BIDModel implements Serializable {

    private UserModel bidder;
    private double amount;
    private LocalDateTime time;
    private String type;

    public BIDModel(UserModel bidder, double amount, LocalDateTime time) {
        this.bidder = bidder;
        this.amount = amount;
        this.time = time;
    }

    public BIDModel() {
    }

    public BIDModel(UserModel user, double bidAmount, LocalDateTime now, String s) {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserModel getBidder() {
        return bidder;
    }

    public void setBidder(UserModel bidder) {
        this.bidder = bidder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
