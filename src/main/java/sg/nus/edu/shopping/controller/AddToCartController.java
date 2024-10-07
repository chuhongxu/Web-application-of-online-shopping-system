package sg.nus.edu.shopping.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddToCartController {

    @Autowired
    private ProductService productService; // 产品服务接口，获取产品信息

    @Autowired
    private ShoppingCartService shoppingCartService; // 购物车服务接口，管理购物车

    // 获取所有产品的 API
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 添加产品到购物车的 API
    @PostMapping("/cart/add")
    public String addProductToCart(@RequestBody AddToCartRequest request) {
        shoppingCartService.addProductToCart(request.getProductId(), request.getQuantity());
        return "Product added to cart successfully.";
    }

    // Data Transfer Object 类 接收从客户端传递的数据
    public static class AddToCartRequest {
        private int productId;
        private int quantity;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}