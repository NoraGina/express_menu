package com.gina.expressMenu.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long idProduct;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(length = 100000, name = "image")
    private byte[] image;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurant")
    @NotFound(action = NotFoundAction.IGNORE)
    private Restaurant restaurant;



    public Product() {
    }

    public Product(Long idProduct, String productName, String description, byte[] image, Double price,
                   Restaurant restaurant) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.price = price;
        this.restaurant = restaurant;

    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageAsString(){
        String base64EncodedImage = Base64.getEncoder().encodeToString(image);
        return base64EncodedImage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        if(idProduct != null){
            return idProduct.equals(product.idProduct) &&
                    productName.equals(product.productName) &&
                    restaurant.equals(product.restaurant);
        }else{
            return  productName.equals(product.productName) &&
                    restaurant.equals(product.restaurant);
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduct, productName, restaurant);
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", image=" + Arrays.toString(image) +
                ", price=" + price +
                ", restaurant=" + restaurant +
                '}';
    }

    public void displayProduct(){
        System.out.println("Product id: "+ idProduct);
        System.out.println("Product name: "+ productName);
        System.out.println("Desription: "+ description);
        System.out.println("Price: "+ price);
        System.out.println("Restaurant: "+ restaurant.getRestaurantName());
    }
}

