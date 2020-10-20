package com.gina.expressMenu.controller;

import com.gina.expressMenu.model.Manager;
import com.gina.expressMenu.model.Product;
import com.gina.expressMenu.model.Restaurant;
import com.gina.expressMenu.repository.ProductRepository;
import com.gina.expressMenu.repository.RestaurantRepository;
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
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/displayAddFormProduct/{idRestaurant}")
    public String displayAddFormProduct(@PathVariable(value = "idRestaurant") Long idRestaurant, Model model) {
        Product product = new Product();
        final Optional<Restaurant> optional = restaurantRepository.findById(idRestaurant);
        if (optional.isPresent()) {
            final Restaurant restaurant = optional.get();
            System.out.println("Restaurant Id: " + restaurant.getIdRestaurant());
            model.addAttribute("restaurant", restaurant);
            product.setRestaurant(restaurant);
            model.addAttribute("product", product);
            System.out.println(product.getRestaurant().getRestaurantName());
            System.out.println(product.getRestaurant().getIdRestaurant());

        } else {
            new IllegalArgumentException("Invalid restaurant Id" + idRestaurant);
        }

        return "add-product";
    }

    @PostMapping("/products/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product, Model model,
                             BindingResult result,
                             @RequestParam("imageFile") MultipartFile file){
        if (result.hasErrors()) {
            return "add-product";
        }
        try{
            byte[] byteObjects = convertToBytes(file);
            product.setImage(byteObjects);
            productRepository.save(product);


        }catch(Exception e){
            e.printStackTrace();
        } finally {
            final Optional<Restaurant> optional = restaurantRepository.findById(product.getRestaurant().getIdRestaurant());
            Product newProduct = new Product();
            if (optional.isPresent()){
                final Restaurant restaurant = optional.get();

                newProduct.setRestaurant(restaurant);
                model.addAttribute("restaurant", restaurant);
                model.addAttribute("product", newProduct);
            }
        }
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        return "add-product";
    }


    private byte[] convertToBytes(MultipartFile file) throws IOException {
        byte[] byteObjects = new byte[file.getBytes().length];
        int i = 0;
        for (byte b : file.getBytes()) {
            byteObjects[i++] = b;
        }
        return byteObjects;
    }



    @GetMapping("/displayEditProduct/{idProduct}")
    public String displayUpdateForm(@PathVariable("idProduct") Long idProduct, Model model){
        final Optional<Product> optionalProduct = productRepository.findById(idProduct);
        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            model.addAttribute("product", product);
            Manager manager = (Manager) httpSession.getAttribute("manager");
            model.addAttribute("manager", manager);
        } else {
            new IllegalArgumentException("Invalid restaurant Id:" + idProduct);
        }

        return "update-product";
    }

    @PostMapping("/products/update/{idProduct}")
    public String updateProduct(@PathVariable("idProduct") Long idProduct,
                                @Valid @ModelAttribute("product")  Product product, BindingResult result,
                                Model model, @RequestParam("imageFile") MultipartFile file) {
        if (result.hasErrors()) {
            return "update-product";
        }
        try {
            byte[] byteObjects = convertToBytes(file);
            product.setImage(byteObjects);
            productRepository.save(product);
            Manager manager = (Manager) httpSession.getAttribute("manager");
            model.addAttribute("manager", manager);
        }catch(Exception e){
            e.printStackTrace();
        }

        return "manager-operation";
    }

    /*@PostMapping("/products/update/{productName}")
    public String updateRestaurant(@PathVariable("productName") Long idProduct, @Valid @ModelAttribute("restaurant")  RestaurantDTO restaurantDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-product";
        }
        restaurantService.save(restaurantDTO);
        return "redirect:/products-list";
    }*/

    @GetMapping("/products/delete/{idProduct}")
    public String deleteProduct(@PathVariable("idProduct") Long idProduct, Model model) {
        productRepository.deleteById(idProduct);
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        return "manager-operation";
    }

    @GetMapping("/products-restaurant/{idRestaurant}")
    public String getProductsByRestaurant(
            @PathVariable("idRestaurant") Long idRestaurant, Model model){
        Manager manager = (Manager) httpSession.getAttribute("manager");
        model.addAttribute("manager", manager);
        model.addAttribute("products", productRepository.findAllByRestaurantId(idRestaurant));
        return "products-restaurant";
    }

}
