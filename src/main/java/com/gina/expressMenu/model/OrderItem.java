package com.gina.expressMenu.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Table(name = "orders_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_item")
    private Long idOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_order_customer")
    @NotFound(action = NotFoundAction.IGNORE)
    private OrderCustomer orderCustomer;

    public OrderItem() {
    }

    public OrderItem(Long idOrderItem, Product product, int quantity, OrderCustomer orderCustomer) {
        this.idOrderItem = idOrderItem;
        this.product = product;
        this.quantity = quantity;
        this.orderCustomer = orderCustomer;
    }

    public OrderItem(Product product, OrderCustomer orderCustomer1) {
        this.product = product;
        this.orderCustomer = orderCustomer1;
    }

    public Long getIdOrderItem() {
        return idOrderItem;
    }

    public void setIdOrderItem(Long idOrderItem) {
        this.idOrderItem = idOrderItem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderCustomer getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(OrderCustomer orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public double getValue(){
        return product.getPrice() * quantity;
    }
}
