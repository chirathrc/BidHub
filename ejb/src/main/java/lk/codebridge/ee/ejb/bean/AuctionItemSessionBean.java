package lk.codebridge.ee.ejb.bean;

import jakarta.ejb.Stateless;
import lk.codebridge.ee.core.model.ProductModel;
import lk.codebridge.ee.ejb.remote.AuctionItemService;

import java.nio.channels.Pipe;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AuctionItemSessionBean implements AuctionItemService {

    private static final List<ProductModel> auctionProducts =  java.util.Collections.synchronizedList(new ArrayList<>());

    static {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            auctionProducts.add(new ProductModel(1, "Apple iPhone 16 Pro Max", "Chirath Rothila", "Latest Apple flagship smartphone", 15000.00, "https://www.gnextstore.lk/uploads/product_image_upload/Product_1602_1737968084.webp", LocalDateTime.parse("2025-12-01 00:00", formatter), 0));
            auctionProducts.add(new ProductModel(2, "Samsung Galaxy Z Fold 6", "Nila Jayasooriya", "Foldable phone with large screen", 2200.00, "https://celltronics.lk/wp-content/uploads/2024/07/Samsung-Galaxy-Z-Fold-6-12GB-RAM-256GB-1.jpg", LocalDateTime.parse("2025-12-02 20:30", formatter), 0));
            auctionProducts.add(new ProductModel(3, "Sony PlayStation 5", "Ruwan Perera", "Next-gen gaming console with 8K support", 900.00, "https://luxuryx.lk/product/149.webp", LocalDateTime.parse("2025-12-03 22:00", formatter), 0));
            auctionProducts.add(new ProductModel(4, "Apple MacBook Pro M4", "Amara Fernando", "16-inch MacBook with M4 chip", 2900.00, "https://ishop.lk/asset/img/product/item_img_2927.jpg", LocalDateTime.parse("2025-12-04 17:00", formatter), 0));
            auctionProducts.add(new ProductModel(5, "DJI Mavic 4 Pro Drone", "Sanjaya Gunasekara", "Advanced camera drone with 4K HDR", 1600.00, "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/17637/1747122454_1886937.jpg", LocalDateTime.parse("2025-12-05 19:45", formatter), 0));
            auctionProducts.add(new ProductModel(6, "ASUS ROG Gaming Laptop", "Harsha Wickramasinghe", "i9, RTX 5090, 32GB RAM, 1TB SSD", 3200.00, "https://www.asus.com/media/Odin/Websites/global/ProductLine/20200824120546.jpg?webp", LocalDateTime.parse("2025-12-06 15:30", formatter), 0));
            auctionProducts.add(new ProductModel(7, "Canon EOS R6 II Camera", "Dilani Jayawardene", "Mirrorless camera with 8K video", 2500.00, "https://s7d1.scene7.com/is/image/canon/5666C002_eos_r6_mark_ii_body_primary?fmt=webp-alpha&wid=800&hei=800", LocalDateTime.parse("2025-12-07 21:00", formatter), 0));
            auctionProducts.add(new ProductModel(8, "Bose QuietComfort Ultra", "Suresh Pathirana", "Noise-cancelling wireless headphones", 400.00, "https://assets.bosecreative.com/transform/775c3e9a-fcd1-489f-a2f7-a57ac66464e1/SF_QCUH_deepplum_gallery_1_816x612_x2?quality=90&io=width:816,height:667,transform:fit", LocalDateTime.parse("2025-12-08 18:30", formatter), 0));
            auctionProducts.add(new ProductModel(9, "Apple Vision Pro", "Minoli Rathnayake", "Mixed reality headset with eye-tracking", 3500.00, "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Apple_Vision_Pro_on_display.jpg/250px-Apple_Vision_Pro_on_display.jpg", LocalDateTime.parse("2025-12-09 16:00", formatter), 0));
            auctionProducts.add(new ProductModel(10, "Samsung Neo QLED 8K TV", "Tharindu Jayalath", "85-inch 8K UHD Smart TV", 6000.00, "https://images.samsung.com/is/image/samsung/p6pim/us/qn98qn990ffxza/gallery/us-qled-qn990f-545129-qn98qn990ffxza-547225276?$PD_GALLERY_PNG$?$product-card-small-jpg$", LocalDateTime.parse("2025-12-10 20:15", formatter), 0));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ProductModel getProductByID(int id) {

        for (ProductModel productModel : auctionProducts) {
            if (productModel.getId() == id) {
                System.out.println(productModel);
                return productModel;
            }
        }

        return null;
    }

    @Override
    public List<ProductModel> getProducts() {

        List<ProductModel> productsFilter = new ArrayList<ProductModel>();

        for (ProductModel productModel : auctionProducts) {
            if (productModel.getEndDate().isAfter(LocalDateTime.now())) {
                productsFilter.add(productModel);
            }
        }

        return productsFilter;
    }

    @Override
    public boolean addToList(int pId, ProductModel productModel) {

        synchronized (auctionProducts) {  // lock on static shared list
            for (int i = 0; i < auctionProducts.size(); i++) {
                ProductModel p = auctionProducts.get(i);
                if (p.getId() == pId) {
                    auctionProducts.set(i, productModel);
                    return true;
                }
            }
        }
        return false;
    }
}
