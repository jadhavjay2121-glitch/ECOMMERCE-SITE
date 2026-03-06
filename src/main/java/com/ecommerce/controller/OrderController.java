package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String listOrders(@RequestParam(value = "statusFilter", required = false) String statusFilter, Model model) {
        if (statusFilter == null || statusFilter.equalsIgnoreCase("All")) {
            model.addAttribute("orders", orderRepository.findAllByOrderByCreatedAtDesc());
        } else {
            model.addAttribute("orders", orderRepository.findByOrderStatusFilter(statusFilter));
        }
        model.addAttribute("currentFilter", statusFilter);
        return "admin/orders";
    }

    @PostMapping("/update-status/{id}")
    public String updateOrderStatus(@PathVariable("id") Long id, @RequestParam("newStatus") String newStatus,
            RedirectAttributes redirectAttributes) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setOrderStatus(newStatus);
            // Re-activate softly if they switch it back from Cancelled
            if (!newStatus.equalsIgnoreCase("Cancelled")) {
                order.setStatus(true);
            }
            orderRepository.save(order);
            redirectAttributes.addFlashAttribute("success_msg", "Order #" + id + " status updated to " + newStatus);
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {

            // Constraint logic constraint rule
            if (order.getOrderStatus().equalsIgnoreCase("Shipped")
                    || order.getOrderStatus().equalsIgnoreCase("Delivered")) {
                redirectAttributes.addFlashAttribute("error_msg",
                        "Cannot cancel an order that has already been shipped or delivered.");
                return "redirect:/admin/orders";
            }

            // Soft Delete configuration
            order.setOrderStatus("Cancelled");
            order.setStatus(false);
            orderRepository.save(order);
            redirectAttributes.addFlashAttribute("success_msg", "Order #" + id + " was successfully cancelled.");
        }
        return "redirect:/admin/orders";
    }
}
