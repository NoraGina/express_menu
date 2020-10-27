package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.Customer;
import com.gina.expressMenu.model.Restaurant;
import com.gina.expressMenu.model.Schedule;
import com.gina.expressMenu.model.Status;
import com.gina.expressMenu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
@Controller
public class CustomerHomeController {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @GetMapping("/displayCustomerHome")
    public String displayClientHome(Model model){
        List<Restaurant>restaurants = restaurantRepository.findAll();

       /* for(Restaurant restaurant: restaurants){
            Long idRestaurant = restaurant.getIdRestaurant();
           Schedule schedule = scheduleRepository.findByDayAndIdRestaurant(getDayOfWeek(LocalDate.now()), idRestaurant);
            model.addAttribute("schedule", schedule);
           // model.addAttribute("openTime", schedule.getOpenTime());
        }*/
        model.addAttribute("restaurants", restaurants);

        return "customer-home";
    }

    @GetMapping("/displayCustomerOrder")
    public String displayCustomerOrder(Model model){
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "customer-order";
    }
    
  @GetMapping("displayRestaurants/{idRestaurant}")
    public String displayRestaurants(@PathVariable("idRestaurant") Long idRestaurant,
                                     Model model){
        model.addAttribute("products",productRepository.findAllByRestaurantId(idRestaurant));

    return "view-products";
  }

    public  int getDayOfWeek(LocalDate date) {

        DayOfWeek day = date.getDayOfWeek();
        return day.getValue();
    }

    @GetMapping("/displaySignUpCustomer")
    public String displaySignUpCustomer(Model model){
        model.addAttribute("customer", new Customer());

        return "signUp-customer";
    }

    @PostMapping("/customer/add")
    public String addCustomer(@Valid @ModelAttribute("customer") Customer customer,
                              BindingResult result,Model model,
                              RedirectAttributes ra ) {
        if (result.hasErrors()) {
            return "signUp-customer";
        }
        customerRepository.save(customer);
        httpSession.setAttribute("customer", customer);
        model.addAttribute("customer", customer);
        model.addAttribute("idCustomer", customer.getIdCustomer());
        System.out.println("Customer id: "+ customer.getIdCustomer());
        model.addAttribute("userName", customer.getCustomerName());
        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("localDate", LocalDate.now());
        model.addAttribute("timestamp", Instant.now());
        model.addAttribute("status", Status.AFFECTED );
        ra.addFlashAttribute("message", "The customer has been saved successfully.");

        return "customer-order";
    }

    @GetMapping("/displayLogInCustomer")
    public String displayLogInCustomer(Model model){

        return "logIn-customer";
    }

    @GetMapping("/authorizationCustomer")
    public String authorization(@RequestParam(value="email",required=false) String email, @RequestParam(value = "password", required = false) String password,Model model){
        Customer customer = customerRepository.findByEmailAndPassword(email, password);
        model.addAttribute("restaurants", restaurantRepository.findAll());
        if(customer != null){
            httpSession.setAttribute("customer", customer);
            model.addAttribute("customer", customer);
            return "customer-order";
        }
        model.addAttribute("customer", new Customer());
        return "/signUp-customer";
    }
}
