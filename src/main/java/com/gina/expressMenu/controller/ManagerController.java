package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.*;
import com.gina.expressMenu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class ManagerController {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private OrderCustomerRepository orderCustomerRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private HttpSession httpSession;

    public ManagerController(ManagerRepository managerRepository,
                             RestaurantRepository restaurantRepository,
                             OrderCustomerRepository orderCustomerRepository,
                             OrderItemRepository orderItemRepository,
                             CustomerRepository customerRepository,
                             ProductRepository productRepository, ScheduleRepository scheduleRepository,
                             HttpSession httpSession) {
        this.managerRepository = managerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderCustomerRepository = orderCustomerRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.scheduleRepository = scheduleRepository;
        this.httpSession = httpSession;
    }

    @GetMapping("/displayAdminHome")
    public String displayAdminHome(Model model){
        return "admin-home";
    }
    @GetMapping("/displayAddAdmin")
    public String displayAddAdmin( Model model) {
        model.addAttribute("manager", new Manager());
        return "signUp-admin";
    }

    @PostMapping("/admin/add")
    public String addAdmin(@Valid @ModelAttribute("manager") Manager manager, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signUp-admin";
        }
    if(managerRepository.countByEmailAndPassword(manager.getEmail(), manager.getPassword())==0l){
         managerRepository.save(manager);
        httpSession.setAttribute("manager", manager);
        model.addAttribute("manager", manager);
        model.addAttribute("idManger", manager.getIdManager());

    return "manager-operation";
}

        return "logIn-admin";

    }

    @GetMapping("/displayLogInAdmin")
    public String displayLogInAdmin(Model model){

        return "logIn-admin";
    }

    @GetMapping("/authorization")
    public String authorization(@RequestParam(value="email",required=false) String email,
                                @RequestParam(value = "password", required = false) String password,
                                Model model){
        Manager manager = managerRepository.findByEmailAndPassword(email, password);

        if(manager != null){
            if(manager.getEmail() != null || manager.getPassword() != null){
                httpSession.setAttribute("manager", manager);
                model.addAttribute("manager", manager);
                model.addAttribute("restaurants", restaurantRepository.findAllByManagerId(manager.getIdManager()));
                return "manager-operation";
            }
            if(manager.getEmail() == null || manager.getPassword() == null){
                return "logIn-admin";
            }
        }
        model.addAttribute("manager", new Manager());
        return "signUp-admin";
    }

    @GetMapping("manager/logout")
    public String logOut(){
        Manager manager = (Manager) httpSession.getAttribute("manager");
        httpSession.removeAttribute("manager");
        httpSession.invalidate();

        return "redirect:/index";
    }

    @GetMapping("manager-operation")
    public String managerOperations(Model model){
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        model.addAttribute("restaurants", restaurantRepository.findAllByManagerId(manager.getIdManager()));

        return "manager-operation";
    }


    @GetMapping("/managers-list")
    public String displayManagerList(Model model) {
        model.addAttribute("managers",managerRepository.findAll());

        return "managers-list";
    }

    @GetMapping("/customers-list")
    public String displayCustomerList(Model model) {
        model.addAttribute("customers",customerRepository.findAll());

        return "customers-list";
    }

    @GetMapping("/customers/delete/{idCustomer}")
    public String deleteCustomer(@PathVariable("idCustomer") Long idCustomer, Model model){
        customerRepository.deleteById(idCustomer);
model.addAttribute("customers", customerRepository.findAll());
        return "customers-list";
    }

    @GetMapping("/orders/delete/{idOrderCustomer}")
    public String deleteOrders(@PathVariable("idOrderCustomer") Long idOrderCustomer,
                               Model model){
        Restaurant restaurant = orderCustomerRepository.findRestaurantByIdOrderCustomer(idOrderCustomer);
        String idRestaurant = restaurant.getIdRestaurant().toString();
        orderCustomerRepository.deleteById(idOrderCustomer);

        return "redirect:/orders-restaurant/"+idRestaurant;
    }



    @GetMapping("/displayEditManager/{idManager}")
    public String displayUpdateFormManger(@PathVariable("idManager") Long idManager, Model model){
        final Optional<Manager> optionalManager = managerRepository.findById(idManager);
        if (optionalManager.isPresent()) {
             final Manager manager = optionalManager.get();

            model.addAttribute("manager", manager);
        } else {
            new IllegalArgumentException("Invalid manager Id:" + idManager);
        }

        return "update-manager";
    }

    @PostMapping("/managers/update/{idManager}")
    public String updateManager(@PathVariable("idManager") Long idManager,
                                @Valid @ModelAttribute("manager") Manager manager, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-manager";
        }
        managerRepository.save(manager);
        httpSession.setAttribute("manager", manager);
        model.addAttribute("manager", manager);

        model.addAttribute("restaurants", restaurantRepository.findAllByManagerId(manager.getIdManager()));

        return "manager-operation";
    }

    @GetMapping("/managers/delete/{idManager}")
    public String deleteRestaurant(@PathVariable("idManager") Long idManager, Model model) {
        model.addAttribute("managers", managerRepository.findAll());
        Manager manager = managerRepository.findById(idManager).get();

        manager.displayManager();
        for(Restaurant restaurant: restaurantRepository.findAllByManagerId(idManager)){

            restaurant.displayRestaurant();
            for(Product product :productRepository.findAllByRestaurantId(restaurant.getIdRestaurant())) {
                product.displayProduct();
                for(OrderItem orderItem:orderItemRepository.findAllByIdProduct(product.getIdProduct())){
                    orderItemRepository.deleteById(orderItem.getIdOrderItem());
                }

                productRepository.deleteById(product.getIdProduct());
            }
            for(Schedule schedule:scheduleRepository.findAllByRestaurantId(restaurant.getIdRestaurant())){
                schedule.displaySchedule();
                scheduleRepository.deleteById(schedule.getIdSchedule());
                schedule.displaySchedule();
            }
            for(OrderCustomer orderCustomer :orderCustomerRepository.findAllByIdRestaurant(restaurant.getIdRestaurant())){
                orderCustomer.displayOrderCustomer();
                orderCustomerRepository.deleteById(orderCustomer.getIdOrderCustomer());
                orderCustomer.displayOrderCustomer();
            }
            restaurantRepository.deleteById(restaurant.getIdRestaurant());
        }
        managerRepository.deleteById(idManager);
        return "redirect:/index";
    }

    @GetMapping("/orders-restaurant/{idRestaurant}")
    public String getOrdersByRestaurant( Model model, @PathVariable("idRestaurant") Long idRestaurant){
        List<OrderCustomer>orderCustomers = orderCustomerRepository.findAllByIdRestaurant(idRestaurant);
       double total = 0;
       for(OrderCustomer orderCustomer: orderCustomers){
           total += orderCustomer.getTotal();

           model.addAttribute("orderCustomer", orderCustomer);
           model.addAttribute("restaurant", orderCustomer.getRestaurant());
       }
       List<OrderCustomer>orders = new ArrayList<>(orderCustomers);
        model.addAttribute("orders", orders);
        model.addAttribute("total", total);
        return "orders-restaurant";
    }

    @GetMapping("/orders-date/{idRestaurant}")
    public String getOrdersByRestaurantAndDate(@PathVariable(name = "idRestaurant") Long idRestaurant,
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam
                                                       (value = "date", required = false) LocalDate date,
                                                Model model){

        List<OrderCustomer>orderCustomers = orderCustomerRepository.findAllByIdRestaurantAndDate(idRestaurant, date);
        double total = 0;
        for(OrderCustomer order: orderCustomers){
            total += order.getTotal();
        }
        model.addAttribute("orders", orderCustomers);
        model.addAttribute("total", total);
        return "orders-date";
    }

    @GetMapping("/orders/restaurant/status/{idRestaurant}")
    public String getOrdersByRestaurantAndStatus(@PathVariable( "idRestaurant") Long idRestaurant,
                                                 @RequestParam(value = "status", required = false)Status status,
                                                 Model model){

        List<OrderCustomer>orderCustomers =
                orderCustomerRepository.findAllByIdRestaurantAndStatus(idRestaurant, status);

        double total = 0;
        for(OrderCustomer order: orderCustomers){
            total += order.getTotal();
        }

        model.addAttribute("orders", orderCustomers);
        model.addAttribute("total", total);

        return "orders-status";
    }

    @GetMapping("/orders-date-status/{idRestaurant}")
    public String getOrdersByRestaurantStatusAndDate(@PathVariable( "idRestaurant") Long idRestaurant,
                                                 @RequestParam(name = "status", required = false)Status status,
                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                 @RequestParam(value = "date", required = false) LocalDate date,
                                                 Model model){

        List<OrderCustomer>orderCustomers =
                orderCustomerRepository.findAllByIdRestaurantDateAndStatus(idRestaurant, date, status);
        double total = 0;
        for(OrderCustomer order: orderCustomers){
            total += order.getTotal();
        }

        model.addAttribute("orders", orderCustomers);
        model.addAttribute("total", total);

        return "orders-date-status";
    }

    @GetMapping("/displayEditOrderManager/{idOrderCustomer}")
    public String displayUpdateOrderManager(@PathVariable("idOrderCustomer") Long idOrderCustomer, Model model){
        final Optional<OrderCustomer> optionalOrderCustomer = orderCustomerRepository.findById(idOrderCustomer);
        if (optionalOrderCustomer.isPresent()){
            final OrderCustomer orderCustomer = optionalOrderCustomer.get();
            model.addAttribute("orderCustomer", orderCustomer);
            model.addAttribute("restaurant", orderCustomerRepository.findRestaurantByIdOrderCustomer(idOrderCustomer));
        }  else {
        new IllegalArgumentException("Invalid order Id:" + idOrderCustomer);
    }
        return "update-order-manager";
    }

    @PostMapping("/orderCustomers/update/{idOrderCustomer}")
    public String updateOrder(@PathVariable("idOrderCustomer") Long idOrderCustomer,
                              @Valid @ModelAttribute("orderCustomer")  OrderCustomer orderCustomer,
                              BindingResult result, Model model) {

        model.addAttribute("orderCustomer", orderCustomer);
        if (result.hasErrors()) {
            return "update-order-manager";
        }
        orderCustomerRepository.save(orderCustomer);
         Restaurant restaurant = orderCustomerRepository.findRestaurantByIdOrderCustomer(idOrderCustomer);
        String idRestaurant = restaurant.getIdRestaurant().toString();

        model.addAttribute("orders", orderCustomerRepository.findAllByIdRestaurant(restaurant.getIdRestaurant()));

        return "redirect:/orders-restaurant/"+idRestaurant;
    }
}
