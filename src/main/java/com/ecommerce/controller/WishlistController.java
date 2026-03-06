package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.model.Wishlist;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/wishlists")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listWishlists(Model model) {
        model.addAttribute("wishlists", wishlistRepository.findAllByOrderByCreatedAtDesc());
        return "admin/wishlists";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("wishlist", new Wishlist());
        model.addAttribute("customers", userRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("products", productRepository.findAllByOrderByCreatedAtDesc());
        return "admin/add_wishlist";
    }

    @PostMapping("/add")
    public String addToWishlist(@ModelAttribute("wishlist") Wishlist wishlistInput,
            RedirectAttributes redirectAttributes) {
        if (wishlistInput.getProduct() == null || wishlistInput.getProduct().getId() == null ||
                wishlistInput.getCustomer() == null || wishlistInput.getCustomer().getId() == null) {
            redirectAttributes.addFlashAttribute("error_msg", "Please fill valid customer and product.");
            return "redirect:/admin/wishlists/add";
        }

        Product product = productRepository.findById(wishlistInput.getProduct().getId()).orElse(null);
        User customer = userRepository.findById(wishlistInput.getCustomer().getId()).orElse(null);

        if (product != null && customer != null) {
            wishlistInput.setCustomer(customer);
            wishlistInput.setProduct(product);
            wishlistRepository.save(wishlistInput);
            redirectAttributes.addFlashAttribute("success_msg",
                    "Product successfully bookmarked to the Customer's Wishlist.");
        }
        return "redirect:/admin/wishlists";
    }

    @PostMapping("/remove/{id}")
    public String removeWishlist(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        wishlistRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success_msg", "Product removed from Wishlist.");
        return "redirect:/admin/wishlists";
    }

    @PostMapping("/move-to-cart/{id}")
    public String moveToCart(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Wishlist wishlist = wishlistRepository.findById(id).orElse(null);
        if (wishlist != null) {
            Product product = wishlist.getProduct();
            if (product.getInventoryCount() > 0 && product.getStatus()) {
                // Create Cart Item
                Cart cartItem = new Cart();
                cartItem.setCustomer(wishlist.getCustomer());
                cartItem.setProduct(product);
                cartItem.setQuantity(1); // Default to 1
                cartItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(1)));
                cartRepository.save(cartItem);

                // Remove from Wishlist
                wishlistRepository.deleteById(id);

                redirectAttributes.addFlashAttribute("success_msg", "Item successfully moved from Wishlist to Cart!");
            } else {
                redirectAttributes.addFlashAttribute("error_msg",
                        "Cannot move to cart. Item is currently out of stock or inactive.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error_msg", "Wishlist item not found.");
        }
        return "redirect:/admin/wishlists";
    }
}
