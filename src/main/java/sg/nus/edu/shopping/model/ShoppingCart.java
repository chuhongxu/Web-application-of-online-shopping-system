package sg.nus.edu.shopping.model;

import jakarta.persistence.*;

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @ManyToOne @JoinColumn (name = "productId")
    private Product product;

    @ManyToOne @JoinColumn(name = "customerId")
    private Customer customer;

    private int productQty;

    public ShoppingCart() {
    }
    public ShoppingCart(Product product, Customer customer, int ProductQty) {
        this.product = product;
        this.setCustomer(customer);
        this.productQty = ProductQty;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
