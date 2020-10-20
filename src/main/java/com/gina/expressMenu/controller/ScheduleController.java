package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.Manager;
import com.gina.expressMenu.model.Product;
import com.gina.expressMenu.model.Restaurant;
import com.gina.expressMenu.model.Schedule;
import com.gina.expressMenu.repository.ProductRepository;
import com.gina.expressMenu.repository.RestaurantRepository;
import com.gina.expressMenu.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ScheduleController {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/displayAddFormSchedule/{idRestaurant}")
    public String displayAddFormSchedule(@PathVariable(value = "idRestaurant") Long idRestaurant, Model model) {
        Schedule schedule = new Schedule();
        final Optional<Restaurant> optional = restaurantRepository.findById(idRestaurant);
        if (optional.isPresent()) {
            final Restaurant restaurant = optional.get();
            model.addAttribute("restaurant", restaurant);
            schedule.setRestaurant(restaurant);
            model.addAttribute("schedule", schedule);


        } else {
            new IllegalArgumentException("Invalid restaurant Id" + idRestaurant);
        }

        return "add-schedule";
    }

    @PostMapping("/schedules/add")
    public String addSchedule(@Valid @ModelAttribute("schedule") Schedule schedule, Model model,
                             BindingResult result){
        if (result.hasErrors()) {
            return "add-schedule";
        }
        try{

            scheduleRepository.save(schedule);


        }catch(Exception e){
            e.printStackTrace();
        } finally {
            final Optional<Restaurant> optional = restaurantRepository.findById(schedule.getRestaurant().getIdRestaurant());
            Schedule newSchedule = new Schedule();
            if (optional.isPresent()){
                final Restaurant restaurant = optional.get();

                newSchedule.setRestaurant(restaurant);
                model.addAttribute("restaurant", restaurant);
                model.addAttribute("schedule", newSchedule);
            }
        }
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        return "add-schedule";
    }



    @GetMapping("/displayEditSchedule/{idSchedule}")
    public String displayUpdateForm(@PathVariable("idSchedule") Long idSchedule, Model model){
        final Optional<Schedule> optionalSchedule = scheduleRepository.findById(idSchedule);
        if (optionalSchedule.isPresent()) {
            final Schedule schedule = optionalSchedule.get();
            model.addAttribute("schedule", schedule);
            Manager manager = (Manager) httpSession.getAttribute("manager");
            model.addAttribute("manager", manager);
        } else {
            new IllegalArgumentException("Invalid schedule Id:" + idSchedule);
        }

        return "update-schedule";
    }

    @PostMapping("/schedules/update/{idSchedule}")
    public String updateProduct(@PathVariable("idSchedule") Long idSchedule,
                                @Valid @ModelAttribute("schedule")  Schedule schedule, BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "update-schedule";
        }
        try {

           scheduleRepository.save(schedule);
            Manager manager = (Manager) httpSession.getAttribute("manager");
            model.addAttribute("manager", manager);
        }catch(Exception e){
            e.printStackTrace();
        }

        return "manager-operation";
    }



    @GetMapping("/schedules/delete/{idSchedule}")
    public String deleteProduct(@PathVariable("idSchedule") Long idSchedule, Model model) {
        scheduleRepository.deleteById(idSchedule);
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        return "manager-operation";
    }

    @GetMapping("/schedules-restaurant/{idRestaurant}")
    public String getProductsByRestaurant(
            @PathVariable("idRestaurant") Long idRestaurant, Model model){
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        model.addAttribute("schedules", scheduleRepository.findAllByRestaurantId(idRestaurant));
        return "schedules-restaurant";
    }

}
