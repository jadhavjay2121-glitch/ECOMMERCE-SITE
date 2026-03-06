package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/carts")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listCarts(Model model) {
        model.addAttribute("carts", cartRepository.findAllByOrderByCreatedAtDesc());
        return "admin/carts";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cart", new Cart());
        model.addAttribute("customers", userRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("products", productRepository.findAllByOrderByCreatedAtDesc());
        return "admin/add_cart";
    }

    @PostMapping("/add")
    public String addToCart(@ModelAttribute("cart") Cart cartInput, RedirectAttributes redirectAttributes) {
        if (cartInput.getProduct() == null || cartInput.getProduct().getId() == null ||
                cartInput.getCustomer() == null || cartInput.getCustomer().getId() == null ||
                cartInput.getQuantity() == null || cartInput.getQuantity() <= 0) {
            redirectAttributes.addFlashAttribute("error_msg",
                    "Please fill valid customer, product, and positive quantity.");
            return "redirect:/admin/carts/add";
        }

        Product product = productRepository.findById(cartInput.getProduct().getId()).orElse(null);
        User customer = userRepository.findById(cartInput.getCustomer().getId()).orElse(null);

        if (product != null && customer != null) {
            // Check inventory stock
            if (cartInput.getQuantity() > product.getInventoryCount()) {
                redirectAttributes.addFlashAttribute("error_msg",
                        "Cannot add to cart. Only " + product.getInventoryCount() + " items currently in stock.");
                return "redirect:/admin/carts/add";
            }

            cartInput.setCustomer(customer);
            cartInput.setProduct(product);
            cartInput.setTotalPrice(product.getPrice().multiply(new BigDecimal(cartInput.getQuantity())));
            cartRepository.save(cartInput);
            redirectAttributes.addFlashAttribute("success_msg", "Item successfully added to the Customer's Cart.");
        }
        return "redirect:/admin/carts";
    }

    @PostMapping("/update/{id}")
    public String updateCart(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity,
            RedirectAttributes redirectAttributes) {
        if (quantity == null || quantity <= 0) {
            redirectAttributes.addFlashAttribute("error_msg", "Quantity must be greater than 0.");
            return "redirect:/admin/carts";
        }

        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart != null) {
            if (quantity > cart.getProduct().getInventoryCount()) {
                redirectAttributes.addFlashAttribute("error_msg",
                        "Cannot update: Insufficient stock. Only " + cart.getProduct().getInventoryCount() + " left.");
            } else {
                cart.setQuantity(quantity);
                cart.setTotalPrice(cart.getProduct().getPrice().multiply(new BigDecimal(quantity)));
                cartRepository.save(cart);
                redirectAttributes.addFlashAttribute("success_msg",
                        "Cart quantity & pricing recalculated successfully.");
            }
        }
        return "redirect:/admin/carts";
    }

    @PostMapping("/remove/{id}")
    public String removeCart(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        cartRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success_msg", "Item removed from cart layout.");
        return "redirect:/admin/carts";
    }
}
