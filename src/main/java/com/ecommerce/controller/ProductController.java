package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAllByOrderByCreatedAtDesc());
        return "admin/products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findByStatusOrderByCreatedAtDesc("active"));
        return "admin/add_product";
    }

    @PostMapping("/add")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result,
            RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findByStatusOrderByCreatedAtDesc("active"));
            return "admin/add_product";
        }
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success_msg", "Product added successfully.");
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error_msg", "Product not found.");
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findByStatusOrderByCreatedAtDesc("active"));
        return "admin/edit_product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute("product") Product updatedProduct,
            BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findByStatusOrderByCreatedAtDesc("active"));
            return "admin/edit_product";
        }
        Product existing = productRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(updatedProduct.getName());
            existing.setDescription(updatedProduct.getDescription());
            existing.setPrice(updatedProduct.getPrice());
            existing.setSku(updatedProduct.getSku());
            existing.setCategory(updatedProduct.getCategory());
            existing.setInventoryCount(updatedProduct.getInventoryCount());
            productRepository.save(existing);
            redirectAttributes.addFlashAttribute("success_msg", "Product updated successfully.");
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setStatus(false);
            productRepository.save(product);
            redirectAttributes.addFlashAttribute("success_msg", "Product deactivated successfully.");
        }
        return "redirect:/admin/products";
    }
}
