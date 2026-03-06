package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.ReviewRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewRepository.findAllByOrderByCreatedAtDesc());
        return "admin/reviews";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("review", new Review());
        model.addAttribute("customers", userRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("products", productRepository.findAllByOrderByCreatedAtDesc());
        return "admin/add_review";
    }

    @PostMapping("/add")
    public String addReview(@ModelAttribute("review") Review reviewInput, RedirectAttributes redirectAttributes) {
        if (reviewInput.getProduct() == null || reviewInput.getProduct().getId() == null ||
                reviewInput.getCustomer() == null || reviewInput.getCustomer().getId() == null ||
                reviewInput.getRating() == null || reviewInput.getRating() < 1 || reviewInput.getRating() > 5) {
            redirectAttributes.addFlashAttribute("error_msg", "Please fill valid customer, product, and rating (1-5).");
            return "redirect:/admin/reviews/add";
        }

        Product product = productRepository.findById(reviewInput.getProduct().getId()).orElse(null);
        User customer = userRepository.findById(reviewInput.getCustomer().getId()).orElse(null);

        if (product != null && customer != null) {
            reviewInput.setCustomer(customer);
            reviewInput.setProduct(product);
            // new reviews auto default status as false (unapproved) in entity config, but
            // let us be explicit
            reviewInput.setStatus(reviewInput.getStatus() != null ? reviewInput.getStatus() : false);
            reviewRepository.save(reviewInput);
            redirectAttributes.addFlashAttribute("success_msg",
                    "Review recorded successfully and is pending moderation.");
        }
        return "redirect:/admin/reviews";
    }

    @PostMapping("/moderate/{id}")
    public String moderateReview(@PathVariable("id") Long id, @RequestParam("status") Boolean status,
            RedirectAttributes redirectAttributes) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            review.setStatus(status);
            reviewRepository.save(review);
            if (status) {
                redirectAttributes.addFlashAttribute("success_msg", "Review successfully Approved for public display.");
            } else {
                redirectAttributes.addFlashAttribute("success_msg", "Review Rejected (Hidden from public).");
            }
        }
        return "redirect:/admin/reviews";
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        reviewRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success_msg", "Inappropriate review permanently deleted.");
        return "redirect:/admin/reviews";
    }
}
