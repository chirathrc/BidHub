package lk.codebridge.ee.ejb.remote;

import jakarta.ejb.Remote;
import lk.codebridge.ee.core.model.ProductModel;

import java.util.List;

@Remote
public interface AuctionItemService {

    ProductModel getProductByID(int id);
    List<ProductModel> getProducts();
    boolean addToList(int pId, ProductModel productModel);

}
