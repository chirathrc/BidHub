package lk.codebridge.ee.core.dto;

import lk.codebridge.ee.core.model.ProductModel;
import lk.codebridge.ee.core.model.UserModel;

import java.io.Serializable;

public class AutoBidDataDTO implements Serializable {

    private UserModel user;
    private double AutoValue;
    private ProductModel productModel;

    public AutoBidDataDTO(UserModel user, double autoValue, ProductModel productModel) {
        this.user = user;
        AutoValue = autoValue;
        this.productModel = productModel;
    }

    public AutoBidDataDTO() {
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public double getAutoValue() {
        return AutoValue;
    }

    public void setAutoValue(double autoValue) {
        AutoValue = autoValue;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }
}
