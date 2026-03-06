package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/customers")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", userRepository.findAllByOrderByCreatedAtDesc());
        return "admin/customers";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new User());
        return "admin/add_customer";
    }

    @PostMapping("/add")
    public String saveCustomer(@ModelAttribute("customer") User customer, RedirectAttributes redirectAttributes) {
        // Enforce the schema
        if (customer.getFirstName() == null || customer.getLastName() == null || customer.getEmail() == null) {
            redirectAttributes.addFlashAttribute("error_msg", "Please fill out all required fields.");
            return "redirect:/admin/customers/add";
        }
        userRepository.save(customer);
        redirectAttributes.addFlashAttribute("success_msg", "Customer added successfully.");
        return "redirect:/admin/customers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        User customer = userRepository.findById(id).orElse(null);
        if (customer == null) {
            redirectAttributes.addFlashAttribute("error_msg", "Customer not found.");
            return "redirect:/admin/customers";
        }
        model.addAttribute("customer", customer);
        return "admin/edit_customer";
    }

    @PostMapping("/edit/{id}")
    public String updateCustomer(@PathVariable("id") Long id, @ModelAttribute("customer") User updatedCustomer,
            RedirectAttributes redirectAttributes) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setFirstName(updatedCustomer.getFirstName());
            existing.setLastName(updatedCustomer.getLastName());
            existing.setEmail(updatedCustomer.getEmail());
            existing.setPhone(updatedCustomer.getPhone());
            existing.setStatus(updatedCustomer.getStatus());
            userRepository.save(existing);
            redirectAttributes.addFlashAttribute("success_msg", "Customer updated successfully.");
        }
        return "redirect:/admin/customers";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        User customer = userRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setStatus(false);
            userRepository.save(customer);
            redirectAttributes.addFlashAttribute("success_msg", "Customer deactivated successfully.");
        }
        return "redirect:/admin/customers";
    }
}
