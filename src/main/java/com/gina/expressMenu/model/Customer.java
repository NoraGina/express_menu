package com.gina.expressMenu.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Long idCustomer;

    private String customerName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "email")
    private String email;

    private String password;
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<OrderCustomer> orderCustomerSet = new HashSet<>();

    public Customer() {
    }

    public Customer(Long idCustomer, String customerName, String address, String email, String password,
                    String phone, Set<OrderCustomer> orderCustomerSet) {
        this.idCustomer = idCustomer;
        this.customerName = customerName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.orderCustomerSet = orderCustomerSet;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<OrderCustomer> getOrderCustomerSet() {
        return orderCustomerSet;
    }

    public void setOrderCustomerSet(Set<OrderCustomer> orderCustomerSet) {
        this.orderCustomerSet = orderCustomerSet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCustomer, customerName, email, phone, password);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "idCustomer=" + idCustomer +
                ", customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", orderCustomerSet=" + orderCustomerSet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return idCustomer.equals(customer.idCustomer) &&
                email.equals(customer.email) &&
                password.equals(customer.password);
    }

    public void displayCustomer(){
        System.out.println("Customer Id: "+ idCustomer);
        System.out.println("Customer name: "+ customerName);
        System.out.println("Email: "+ email);
        System.out.println("Password: "+ password);
        System.out.println("Phone: "+ phone);
    }

}
