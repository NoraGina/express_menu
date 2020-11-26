package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.*;
import com.gina.expressMenu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerOrderController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private OrderCustomerRepository orderCustomerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/productsRestaurant/{idRestaurant}")
    public String getProductsByRestaurant(@PathVariable("idRestaurant") Long idRestaurant,
                                          Model model) {
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderCustomer orderCustomer = new OrderCustomer();
        Customer customer = (Customer) httpSession.getAttribute("customer");
        for(Product product:productRepository.findAllByRestaurantId(idRestaurant)){
            OrderItem orderItem = new OrderItem(product, orderCustomer);
            orderItemList.add(orderItem);
            Restaurant restaurant = orderItem.getProduct().getRestaurant();
            orderCustomer.setRestaurant(restaurant);
        }

        orderCustomer.setOrderItemList(orderItemList);
        orderCustomer.setCustomer(customer);
        orderCustomer.setDate(LocalDate.now());
        orderCustomer.setStatus(Status.AFFECTED);
        orderCustomer.setTime(LocalTime.now());
        model.addAttribute("orderCustomer", orderCustomer);
        model.addAttribute("customer", customer);

        return "order-items";
    }



    @Transactional
    @PostMapping("orders/add")
    public String saveOrder(@Valid @ModelAttribute("orderCustomer")OrderCustomer orderCustomer,
                            Model model, BindingResult result){
        if (result.hasErrors()) {
            return "orders-item";
        }

        orderCustomer.getOrderItemList().stream().forEach(orderItem -> orderItem.setOrderCustomer(orderCustomer));
        List<OrderItem> nonZeroOi = new ArrayList<>();
        for(OrderItem orderItem :orderCustomer.getOrderItemList()) {
            if(orderItem.getQuantity() > 0) {
                nonZeroOi.add(orderItem);
            }
        }
        orderCustomer.setOrderItemList(nonZeroOi);
        double total = 0;
        for(OrderItem orderItem:nonZeroOi){
            total += orderItem.getQuantity()* orderItem.getProduct().getPrice();
        }
        orderCustomerRepository.save(orderCustomer);
        model.addAttribute("orderCustomer", orderCustomer);
        orderCustomer.setStatus(Status.AFFECTED);
        model.addAttribute("restaurantName", orderCustomer.getRestaurant().getRestaurantName());
        Long id = orderCustomer.getCustomer().getIdCustomer();
        Customer customer = customerRepository.findById(id).get();
        Restaurant restaurant = restaurantRepository.findById(orderCustomer.getRestaurant().getIdRestaurant()).get();
        model.addAttribute("customer",customer);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("total", total);


        return "cart";
    }


    @GetMapping("/cart")
    public String displayOrder(@ModelAttribute("orderCustomer") OrderCustomer orderCustomer, Model model){


        return "cart";
    }

    @GetMapping("/orderCustomer/delete/{idOrderCustomer}")
    public String deleteOrder(@PathVariable("idOrderCustomer") Long idOrderCustomer)
                               {
       orderCustomerRepository.deleteById(idOrderCustomer);


        return "customer-home";
    }

    @GetMapping("/displayEditOrder/{idOrderCustomer}")
    public String displayUpdateOrder(@PathVariable("idOrderCustomer") Long idOrderCustomer, Model model){
        final Optional<OrderCustomer> optionalOrderCustomer = orderCustomerRepository.findById(idOrderCustomer);
        if (optionalOrderCustomer.isPresent()) {
            final OrderCustomer orderCustomer = optionalOrderCustomer.get();
            model.addAttribute("orderCustomer", orderCustomer);
            List<OrderItem>orderItemList = orderItemRepository.findAllByOrderCustomerId(idOrderCustomer);
            Customer customer = orderCustomer.getCustomer();
            Restaurant restaurant = orderCustomer.getRestaurant();
            model.addAttribute("orderItemList", orderItemList);
            model.addAttribute("customer", customer);
            model.addAttribute("restaurant", restaurant);
        } else {
            new IllegalArgumentException("Invalid order Id:" + idOrderCustomer);
        }

        return "update-order";
    }

    @PostMapping("/orderCustomer/update/{idOrderCustomer}")
    public String updateOrder(@PathVariable("idOrderCustomer") Long idOrderCustomer,
                                   @Valid @ModelAttribute("orderCustomer")  OrderCustomer orderCustomer,
                                   BindingResult result, Model model) {

        model.addAttribute("orderCustomer", orderCustomer);
        if (result.hasErrors()) {
            return "update-order";
        }
        try{

            List<OrderItem> nonZeroOi = new ArrayList<>();
            for(OrderItem orderItem :orderCustomer.getOrderItemList()) {
                if(orderItem.getQuantity() > 0) {
                    nonZeroOi.add(orderItem);
                }
            }
            orderCustomer.setOrderItemList(nonZeroOi);
            double total = 0;
            for(OrderItem orderItem:nonZeroOi){
                total += orderItem.getQuantity()* orderItem.getProduct().getPrice();
            }

            orderCustomerRepository.save(orderCustomer);
            model.addAttribute("orderCustomer", orderCustomer);

            model.addAttribute("restaurantName", orderCustomer.getRestaurant().getRestaurantName());
            Long id = orderCustomer.getCustomer().getIdCustomer();
            Customer customer = customerRepository.findById(id).get();
            Restaurant restaurant = restaurantRepository.findById(orderCustomer.getRestaurant().getIdRestaurant()).get();
            model.addAttribute("customer",customer);
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("total", total);


        }catch(Exception e){
            e.printStackTrace();
        }

        return "cart";
    }



}
