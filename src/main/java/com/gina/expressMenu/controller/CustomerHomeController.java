package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.*;
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
    public String displayClientHome(Model model,
                                    @ModelAttribute("message") String message,
                                    @ModelAttribute("message1") String message1){
        List<Restaurant>restaurants = restaurantRepository.findAll();
        Customer customer = (Customer) httpSession.getAttribute("customer");
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("customer", customer);
        model.addAttribute("message", message);
        model.addAttribute("message1", message1);
        return "customer-home";

        }

    
  @GetMapping("displayRestaurants/{idRestaurant}")
    public String displayRestaurants(@PathVariable("idRestaurant") Long idRestaurant,
                                     Model model){
        model.addAttribute("products",productRepository.findAllByRestaurantId(idRestaurant));

    return "view-products";
  }


    @GetMapping("/displaySignUpCustomer")
    public String displaySignUpCustomer(Model model){
        model.addAttribute("customer", new Customer());

        return "signUp-customer";
    }

    @PostMapping("/customer/add")
    public String addCustomer(@Valid @ModelAttribute("customer") Customer customer,
                              BindingResult result,Model model,
                              RedirectAttributes attributes ) {
        if (result.hasErrors()) {

            return "signUp-customer";
        }
        if(customerRepository.countByEmailAndPassword(customer.getEmail(), customer.getPassword()) == 1l) {
            attributes.addFlashAttribute("message", customer.getCustomerName() + " " + "Aveti deja cont");
            System.out.println("Count by email and password"+customerRepository.countByEmailAndPassword(customer.getEmail(), customer.getPassword()));
            return "redirect:/displayLogInCustomer";
        }else if(customerRepository.countByEmail(customer.getEmail()) == 1l){
            attributes.addFlashAttribute("message1", "exista un cont deja la adresa:! "+customer.getEmail()+" "+" Ai uitat parola?");
            System.out.println("Count by email"+customerRepository.countByEmail(customer.getEmail()));
            return "redirect:/displayLogInCustomer";

        }else if(customerRepository.countByEmail(customer.getEmail()) == 0l ||
                customerRepository.countByEmailAndPassword(customer.getEmail(), customer.getPassword()) == 0l){
            customerRepository.save(customer);
            httpSession.setAttribute("customer", customer);
            model.addAttribute("customer", customer);
            model.addAttribute("idCustomer", customer.getIdCustomer());
            model.addAttribute("userName", customer.getCustomerName());
            model.addAttribute("restaurants", restaurantRepository.findAll());

            attributes.addFlashAttribute("message", customer.getCustomerName().toUpperCase()+" "+"Sunte-ti logat!");

            return "redirect:/displayCustomerHome";
        }
        return "customer-home";
    }

    @GetMapping("/displayLogInCustomer")
    public String displayLogInCustomer(Model model){

        return "logIn-customer";
    }

    @GetMapping("/authorizationCustomer")
    public String authorization(@RequestParam(value="email",required=false) String email,
                                @RequestParam(value = "password", required = false) String password,
                                 Model model, RedirectAttributes attributes ){
        Customer customer = customerRepository.findByEmailAndPassword(email, password);

        if(customer != null){
                httpSession.setAttribute("customer", customer);
                model.addAttribute("customer", customer);
                model.addAttribute("restaurants", restaurantRepository.findAll());
                attributes.addFlashAttribute("message", customer.getCustomerName().toUpperCase()+" "+"Sunte-ti logat!");

                return "redirect:/displayCustomerHome";
            }
            attributes.addFlashAttribute("message", "Poate ai uitat parola sau nu ai cont?");
            //model.addAttribute("customer", new Customer());
            return "redirect:/displaySignUpCustomer";

    }


    @GetMapping("/update-customer")
    public String displayCustomerUpdate(@RequestParam("email") String email,Model model){
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null){
            model.addAttribute("customer", new Customer());
            return "/signUp-customer";

        }
        model.addAttribute("customer", customer);

        return "update-customer";
    }

    @PostMapping("customers/update/{idCustomer}")
    public String updateCustomer(@PathVariable("idCustomer")Long idCustomer, Model model,
                                 @Valid @ModelAttribute("customer") Customer customer, BindingResult result,
                                 RedirectAttributes attributes){
        if(result.hasErrors()){
            return "update-customer";
        }

        customerRepository.save(customer);
        model.addAttribute("customer", customer);
        httpSession.setAttribute("customer", customer);
        attributes.addFlashAttribute("message", customer.getCustomerName().toUpperCase()+" "+"Sunte-ti logat!");
        model.addAttribute("restaurants", restaurantRepository.findAll());

        return "redirect:/displayCustomerHome";
    }

    @GetMapping("customer/logout")
    public String logOutCustomer(){
        Customer customer = (Customer) httpSession.getAttribute("customer");
        httpSession.removeAttribute("customer");
        httpSession.invalidate();

        return "redirect:/index";
    }
}
