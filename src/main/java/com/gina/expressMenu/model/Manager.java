package com.gina.expressMenu.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "mangers")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_manager")
    private Long idManager;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Restaurant> restaurantSet;

    public Manager() {
    }

    public Manager(Long idManager, String userName, String email, String phone,
                   Set<Restaurant> restaurantSet, String password) {
        this.idManager = idManager;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.restaurantSet = restaurantSet;
        this.password = password;
    }

    public Long getIdManager() {
        return idManager;
    }

    public void setIdManager(Long idManager) {
        this.idManager = idManager;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Restaurant> getRestaurantSet() {
        return restaurantSet;
    }

    public void setRestaurantSet(Set<Restaurant> restaurantSet) {
        this.restaurantSet = restaurantSet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager)) return false;
        Manager manager = (Manager) o;
        return idManager.equals(manager.idManager) &&
                userName.equals(manager.userName) &&
                email.equals(manager.email) &&
                phone.equals(manager.phone) &&
                password.equals(manager.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idManager, userName, email, phone, password);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "idManager=" + idManager +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", restaurantSet=" + restaurantSet +
                '}';
    }

    public void displayManager() {
        System.out.println("Manager Id: " + idManager);
        System.out.println("Manager name: " + userName);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Phone: " + phone);
    }
}
