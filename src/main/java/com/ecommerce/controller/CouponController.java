package com.ecommerce.controller;

import com.ecommerce.model.Coupon;
import com.ecommerce.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/coupons")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @GetMapping
    public String listCoupons(Model model) {
        model.addAttribute("coupons", couponRepository.findAllByOrderByCreatedAtDesc());
        return "admin/coupons";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        return "admin/add_coupon";
    }

    @PostMapping("/add")
    public String addCoupon(@ModelAttribute("coupon") Coupon coupon, RedirectAttributes redirectAttributes) {
        // Enforce upper case codes
        coupon.setCouponCode(coupon.getCouponCode().toUpperCase());

        // Prevent duplicates
        Coupon existing = couponRepository.findByCouponCode(coupon.getCouponCode());
        if (existing != null) {
            redirectAttributes.addFlashAttribute("error_msg", "A coupon with this code already exists.");
            return "redirect:/admin/coupons/add";
        }

        couponRepository.save(coupon);
        redirectAttributes.addFlashAttribute("success_msg",
                "Discount Code '" + coupon.getCouponCode() + "' successfully generated.");
        return "redirect:/admin/coupons";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Coupon coupon = couponRepository.findById(id).orElse(null);
        if (coupon == null) {
            redirectAttributes.addFlashAttribute("error_msg", "Coupon not found.");
            return "redirect:/admin/coupons";
        }
        model.addAttribute("coupon", coupon);
        return "admin/edit_coupon";
    }

    @PostMapping("/edit/{id}")
    public String updateCoupon(@PathVariable("id") Long id, @ModelAttribute("coupon") Coupon updatedCoupon,
            RedirectAttributes redirectAttributes) {
        Coupon existing = couponRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setDiscountType(updatedCoupon.getDiscountType());
            existing.setDiscountValue(updatedCoupon.getDiscountValue());
            existing.setValidFrom(updatedCoupon.getValidFrom());
            existing.setValidTo(updatedCoupon.getValidTo());
            existing.setUsageLimit(updatedCoupon.getUsageLimit());

            couponRepository.save(existing);
            redirectAttributes.addFlashAttribute("success_msg", "Coupon configurations updated successfully.");
        }
        return "redirect:/admin/coupons";
    }

    @PostMapping("/toggle/{id}")
    public String toggleCouponStatus(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Coupon coupon = couponRepository.findById(id).orElse(null);
        if (coupon != null) {
            coupon.setStatus(!coupon.getStatus()); // Flip the boolean for soft activation/deactivation
            couponRepository.save(coupon);
            String statusVerb = coupon.getStatus() ? "Activated" : "Deactivated";
            redirectAttributes.addFlashAttribute("success_msg", "Coupon " + statusVerb + " successfully.");
        }
        return "redirect:/admin/coupons";
    }
}
