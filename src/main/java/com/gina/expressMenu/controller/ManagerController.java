package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.Manager;
import com.gina.expressMenu.repository.ManagerRepository;
import com.gina.expressMenu.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ManagerController {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/displayAdminHome")
    public String displayAdminHome(Model model){
       // model.addAttribute("managers", managerRepository.findAll());
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

        managerRepository.save(manager);
        httpSession.setAttribute("manager", manager);
        model.addAttribute("manager", manager);
        model.addAttribute("idManger", manager.getIdManager());
        for(Manager managers: managerRepository.findAll()){
            managers.displayManager();
        }
        return "manager-operation";


    }


    @GetMapping("/managers-list")
    public String displayManagerList(Model model) {
        model.addAttribute("managers",managerRepository.findAll());

        return "managers-list";
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
            httpSession.setAttribute("manager", manager);
            model.addAttribute("manager", manager);
            return "manager-operation";
        }
        model.addAttribute("manager", new Manager());
        return "signUp-admin";
    }

    @GetMapping("manager-operation")
    public String managerOperations(Model model){
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        model.addAttribute("restaurants", restaurantRepository.findAllByManagerId(manager.getIdManager()));
        return "manager-operation";
    }

    @GetMapping("/displayEditManager/{idManager}")
    public String displayUpdateForm(@PathVariable("idManager") Long idManager, Model model){
        final Optional<Manager> optionalManager = managerRepository.findById(idManager);
        if (optionalManager.isPresent()) {
            final Manager manager = optionalManager.get();
            model.addAttribute("manager", manager);
        } else {
            new IllegalArgumentException("Invalid restaurant Id:" + idManager);
        }

        return "update-manager";
    }

    @PostMapping("/managers/update/{idManager}")
    public String updateProduct(@PathVariable("idManager") Long idManager,
                                @Valid @ModelAttribute("manager") Manager manager, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-manager";
        }
        managerRepository.save(manager);
        return "redirect:managers-list";
    }

    @GetMapping("/managers/delete/{idManager}")
    public String deleteRestaurant(@PathVariable("idManager") Long idManager) {
        managerRepository.deleteById(idManager);
        return "redirect:managers-list";
    }

}
