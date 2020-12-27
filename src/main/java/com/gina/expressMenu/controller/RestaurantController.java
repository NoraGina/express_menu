package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.*;
import com.gina.expressMenu.repository.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Controller
public class RestaurantController {

    @Autowired
    HttpSession httpSession;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private OrderCustomerRepository orderCustomerRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public byte[] convertToBytes(MultipartFile file) throws IOException {
        byte[] byteObjects = new byte[file.getBytes().length];
        int i = 0;
        for (byte b : file.getBytes()) {
            byteObjects[i++] = b;
        }
        return byteObjects;
    }


    @GetMapping("/displayAddFormRestaurant")
    public String displayAddForm( Model model) {

        Restaurant restaurant = new Restaurant();
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        restaurant.setManager(manager);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurants", restaurantRepository.findAllByManagerId(manager.getIdManager()));
        return "add-restaurant";
    }



    @PostMapping("/restaurants/add")
    public String addRestaurant(@Valid @ModelAttribute("restaurant") Restaurant restaurant,
                                BindingResult result,Model model,
                                @RequestParam("imageFile") MultipartFile file ) {
        if (result.hasErrors()) {
            return "add-restaurant";
        }
        try{
            byte[] byteObjects = convertToBytes(file);
            restaurant.setImage(byteObjects);
            restaurantRepository.save(restaurant);

        }catch(Exception e){
            e.printStackTrace();
        }
        model.addAttribute("restaurant",restaurant);
      Long id = restaurant.getManager().getIdManager();
        Manager manager = managerRepository.getOne(id);
        model.addAttribute("manager", manager);
        model.addAttribute("restaurants", restaurantRepository.findAllByManagerId(manager.getIdManager()));

        return "manager-operation";

    }

    @GetMapping("/manager-restaurants/{idManager}")
    public String displayRestaurantsManager( @PathVariable("idManager")Long idManager, Model model){
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("restaurants", restaurantRepository.findAllByManagerId(idManager));

        return "manager-restaurants";
    }


    @GetMapping("/restaurants-list")
    public String displayRestaurantsList(Model model) {
        model.addAttribute("restaurants", restaurantRepository.findAll());

        return "restaurants-list";
    }



    @GetMapping("/displayEditRestaurant/{idRestaurant}")
    public String displayUpdateRestaurant(@PathVariable("idRestaurant") Long idRestaurant, Model model){
        final Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(idRestaurant);
        if (optionalRestaurant.isPresent()) {
            final Restaurant restaurant = optionalRestaurant.get();
            model.addAttribute("restaurant", restaurant);
        } else {
            new IllegalArgumentException("Invalid restaurant Id:" + idRestaurant);
        }

        return "update-restaurant";
    }

    @PostMapping("/restaurants/update/{idRestaurant}")
    public String updateRestaurant(@PathVariable("idRestaurant") Long idRestaurant,
                                   @Valid @ModelAttribute("restaurant")  Restaurant restaurant,
                                   @RequestParam("imageFile") MultipartFile file ,
                                   BindingResult result, Model model) {
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        if (result.hasErrors()) {
            return "update-restaurant";
        }
        try{
            byte[] byteObjects = convertToBytes(file);
            restaurant.setImage(byteObjects);
            restaurantRepository.save(restaurant);

        }catch(Exception e){
            e.printStackTrace();
        }
        String idManager = manager.getIdManager().toString();
        List<Restaurant>restaurantList = restaurantRepository.findAllByManagerId(manager.getIdManager());
        model.addAttribute("restaurants", restaurantList);
        //return "manager-operation";
        return "redirect:/manager-restaurants/"+idManager;
    }


    @Transactional
    @GetMapping("/restaurants/delete/{idRestaurant}")
    public String deleteRestaurant(@PathVariable("idRestaurant") Long idRestaurant, Model model) {
        List<Product>productList = productRepository.findAllByRestaurantId(idRestaurant);
      ;
        for(Product product:productList){
           List<OrderItem> orderItemList = orderItemRepository.findAllByIdProduct(product.getIdProduct());
            for(OrderItem orderItem:orderItemList){
                orderItemRepository.deleteById(orderItem.getIdOrderItem());

            }

            productRepository.deleteById(product.getIdProduct());

        }

        for(Schedule schedule:scheduleRepository.findAllByRestaurantId(idRestaurant)){
            scheduleRepository.deleteById(schedule.getIdSchedule());
        }

        for(OrderCustomer orderCustomer:orderCustomerRepository.findAllByIdRestaurant(idRestaurant)){
            orderCustomerRepository.deleteById(orderCustomer.getIdOrderCustomer());
        }

        restaurantRepository.deleteById(idRestaurant);

        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        String idManager = manager.getIdManager().toString();
        List<Restaurant>restaurantList = restaurantRepository.findAllByManagerId(manager.getIdManager());
        model.addAttribute("restaurants", restaurantList);

        return "redirect:/manager-restaurants/"+idManager;
    }

}
