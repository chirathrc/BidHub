package lk.codebridge.ee.core.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class ProductModel implements Serializable {

    private int id;
    private String name;
    private String SellerName;
    private String description;
    private double startingPrice;
    private String imageUrl;
    private LocalDateTime endDate;
    private int bidCount;
    private double currentHighestBid = 0;
    private UserModel highestBidder;

    public ProductModel(int id, String name, String sellerName, String description, double startingPrice, String imageUrl, LocalDateTime endDate, int bidCount) {
        this.id = id;
        this.name = name;
        SellerName = sellerName;
        this.description = description;
        this.startingPrice = startingPrice;
        this.imageUrl = imageUrl;
        this.endDate = endDate;
        this.bidCount = bidCount;
    }

    public ProductModel() {
    }

    public double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(double currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public UserModel getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(UserModel highestBidder) {
        this.highestBidder = highestBidder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getBidCount() {
        return bidCount;
    }

    public void setBidCount(int bidCount) {
        this.bidCount = bidCount;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", SellerName='" + SellerName + '\'' +
                ", description='" + description + '\'' +
                ", startingPrice=" + startingPrice +
                ", imageUrl='" + imageUrl + '\'' +
                ", endDate=" + endDate +
                ", bidCount=" + bidCount +
                ", currentHighestBid=" + currentHighestBid +
                ", highestBidder=" + highestBidder +
                '}';
    }
}

