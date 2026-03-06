package com.ecommerce.controller;

import com.ecommerce.model.Payment;
import com.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentRepository.findAllByOrderByCreatedAtDesc());
        return "admin/payments";
    }

    @PostMapping("/refund/{id}")
    public String refundPayment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Payment payment = paymentRepository.findById(id).orElse(null);
        if (payment != null) {
            if ("Refunded".equalsIgnoreCase(payment.getPaymentStatus())) {
                redirectAttributes.addFlashAttribute("error_msg", "This payment is already in a Refunded state.");
            } else {
                payment.setPaymentStatus("Refunded");
                paymentRepository.save(payment);
                redirectAttributes.addFlashAttribute("success_msg", "Successful Refund triggered for Payment #" + id);
            }
        } else {
            redirectAttributes.addFlashAttribute("error_msg", "Payment transaction explicitly failed or not found.");
        }
        return "redirect:/admin/payments";
    }
}
