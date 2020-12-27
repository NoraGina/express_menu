package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.*;
import com.gina.expressMenu.repository.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                          Model model, RedirectAttributes redirAttrs) {
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderCustomer orderCustomer = new OrderCustomer();
        Customer customer = (Customer) httpSession.getAttribute("customer");
        if(customer!= null){
            OrderItem orderItem = null;
            for (Product product : productRepository.findAllByRestaurantId(idRestaurant)) {
                orderItem = new OrderItem(product, orderCustomer);
                orderItemList.add(orderItem);
                Restaurant restaurant = orderItem.getProduct().getRestaurant();
                orderCustomer.setRestaurant(restaurant);
                model.addAttribute("restaurant", restaurant);
            }

            orderCustomer.setOrderItemList(orderItemList);
            orderCustomer.setCustomer(customer);
            orderCustomer.setDate(LocalDate.now());
            orderCustomer.setStatus(Status.AFFECTED);
            orderCustomer.setTime(LocalTime.now());
            model.addAttribute("orderCustomer", orderCustomer);
            model.addAttribute("customer", customer);
            model.addAttribute("orderItem", orderItem);

            return "order-items";
        }
        model.addAttribute("customer", new Customer());

        return "/logIn-customer";

    }


    @Transactional
    @PostMapping("orders/add")
    public String saveOrder(@Valid @ModelAttribute("orderCustomer") OrderCustomer orderCustomer,
                            Model model, BindingResult result) {

        if (result.hasErrors()) {
            return "orders-item";
        }
        orderCustomer.getOrderItemList().stream().forEach(item -> item.setOrderCustomer(orderCustomer));
        List<OrderItem> nonZeroOi = new ArrayList<>();
        double total = 0;
        for (OrderItem orderItem : orderCustomer.getOrderItemList()) {
            if (orderItem.getQuantity() > 0) {
                nonZeroOi.add(orderItem);
                total += orderItem.getQuantity() * orderItem.getProduct().getPrice();
            }
        }
        orderCustomer.setOrderItemList(nonZeroOi);

        orderCustomerRepository.save(orderCustomer);
        model.addAttribute("orderCustomer", orderCustomer);
        orderCustomer.setStatus(Status.AFFECTED);

        Long id = orderCustomer.getCustomer().getIdCustomer();
        Customer customer = customerRepository.findById(id).get();
        Restaurant restaurant = restaurantRepository.findById(orderCustomer.getRestaurant().getIdRestaurant()).get();
        model.addAttribute("customer", customer);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("total", total);


        return "cart";
    }


    @GetMapping("/cart")
    public String displayOrder(@ModelAttribute("orderCustomer") OrderCustomer orderCustomer,
                               Model model) {

        model.addAttribute("orderCustomer", orderCustomer);
        model.addAttribute("restaurants", restaurantRepository.findAll());
        Customer customer = (Customer) httpSession.getAttribute("customer");
        model.addAttribute("customer", customer);
        return "cart";
    }

    @GetMapping("/orderCustomer/delete/{idOrderCustomer}")
    public String deleteOrder(@PathVariable("idOrderCustomer") Long idOrderCustomer, Model model,
                              RedirectAttributes attributes) {

        Customer customer = orderCustomerRepository.findById(idOrderCustomer).get().getCustomer();
        httpSession.setAttribute("customer", customer);
        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("customer", customer);
        attributes.addFlashAttribute("message", customer.getCustomerName()+ ", Doriti sa faceti alta comanda? Sau sa iesiti din aplicatie!");
        orderCustomerRepository.deleteById(idOrderCustomer);

       return "redirect:/displayCustomerHome";
        //return "customer-home";
    }

    @GetMapping("/displayEditOrder/{idOrderCustomer}")
    public String displayUpdateOrder(@PathVariable("idOrderCustomer") Long idOrderCustomer, Model model) {
        final Optional<OrderCustomer> optionalOrderCustomer = orderCustomerRepository.findById(idOrderCustomer);
        if (optionalOrderCustomer.isPresent()) {
            final OrderCustomer orderCustomer = optionalOrderCustomer.get();
            model.addAttribute("orderCustomer", orderCustomer);
            Customer customer = orderCustomer.getCustomer();
            Restaurant restaurant = orderCustomer.getRestaurant();
            for (OrderItem orderItem : orderCustomer.getOrderItemList()) {
                model.addAttribute("orderItem", orderItem);
            }
            model.addAttribute("customer", customer);
            model.addAttribute("restaurant", restaurant);

        } else {
            new IllegalArgumentException("Invalid order Id:" + idOrderCustomer);
        }

        return "update-order";
    }



    @Transactional
    @GetMapping("/orderItemCustomer/delete/{idOrderItem}")
    public String deleteOrderItem(@PathVariable("idOrderItem") Long idOrderItem,
                                  Model model) {
        OrderCustomer orderCustomer = orderItemRepository.findOrderCustomerByIdOrderItem(idOrderItem);

        orderItemRepository.deleteById(idOrderItem);
        model.addAttribute("orderCustomer", orderCustomer);
        model.addAttribute("customer", orderCustomer.getCustomer());
        String idOrderCustomer = orderCustomer.getIdOrderCustomer().toString();
       return "redirect:/displayEditOrder/"+idOrderCustomer;

    }


    @PostMapping("/orderCustomer/update/{idOrderCustomer}")
    public String updateOrder(@PathVariable("idOrderCustomer") Long idOrderCustomer,
                              @Valid @ModelAttribute("orderCustomer") OrderCustomer orderCustomer,
                              BindingResult result, RedirectAttributes attributes,Model model) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("error", "Verificati sunt campuri necompletate");
           /* for (ObjectError error : result.getAllErrors()) { // 1.
                String fieldErrors = ((FieldError) error).getField(); // 2.
                System.out.println(fieldErrors);
            }*/
            return "update-order";
        }

        double total = 0;
        for(OrderItem orderItem:orderCustomer.getOrderItemList()){
            orderItem.setOrderCustomer(orderCustomer);
            total += orderItem.getProduct().getPrice()*orderItem.getQuantity();
        }
        model.addAttribute("orderCustomer", orderCustomer);
        model.addAttribute("customer", orderCustomer.getCustomer());
        model.addAttribute("total", total);
        attributes.addFlashAttribute("success", "Comanda a fost editata cu succes!");
        return "cart";

    }



}
