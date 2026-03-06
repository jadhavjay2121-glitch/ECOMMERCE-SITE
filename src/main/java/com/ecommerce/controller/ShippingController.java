package com.ecommerce.controller;

import com.ecommerce.model.Shipping;
import com.ecommerce.repository.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/shipping")
public class ShippingController {

    @Autowired
    private ShippingRepository shippingRepository;

    @GetMapping
    public String listShipping(Model model) {
        model.addAttribute("shippings", shippingRepository.findAllByOrderByCreatedAtDesc());
        return "admin/shipping";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Shipping shipping = shippingRepository.findById(id).orElse(null);
        if (shipping == null) {
            redirectAttributes.addFlashAttribute("error_msg", "Shipping entry not found.");
            return "redirect:/admin/shipping";
        }
        model.addAttribute("shipping", shipping);
        return "admin/edit_shipping";
    }

    @PostMapping("/edit/{id}")
    public String updateShipping(@PathVariable("id") Long id, @ModelAttribute("shipping") Shipping updatedShipping,
            RedirectAttributes redirectAttributes) {
        Shipping existing = shippingRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setCourierService(updatedShipping.getCourierService());
            existing.setTrackingNumber(updatedShipping.getTrackingNumber());
            existing.setShippingStatus(updatedShipping.getShippingStatus());
            existing.setShippingCost(updatedShipping.getShippingCost());

            shippingRepository.save(existing);
            redirectAttributes.addFlashAttribute("success_msg", "Shipping entry updated successfully.");
        }
        return "redirect:/admin/shipping";
    }
}
