package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    // Record for simple DTO representing category with its calculated product count
    public record CategoryWithCount(Category category, long productCount) {
    }

    @GetMapping
    public String listCategories(Model model) {
        List<Category> activeCategories = categoryRepository.findByStatusOrderByCreatedAtDesc("active");
        List<CategoryWithCount> categoriesWithCount = new ArrayList<>();

        for (Category cat : activeCategories) {
            long count = productRepository.countByCategoryIdAndStatus(cat.getId(), true);
            categoriesWithCount.add(new CategoryWithCount(cat, count));
        }

        model.addAttribute("categories", categoriesWithCount);
        return "admin/categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/add_category";
    }

    @PostMapping("/add")
    public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/add_category";
        }
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success_msg", "Category added successfully.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null || "deleted".equals(category.getStatus())) {
            redirectAttributes.addFlashAttribute("error_msg", "Category not found.");
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        return "admin/edit_category";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable("id") Long id,
            @Valid @ModelAttribute("category") Category updatedCategory, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/edit_category";
        }
        Category existing = categoryRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(updatedCategory.getName());
            existing.setDescription(updatedCategory.getDescription());
            categoryRepository.save(existing);
            redirectAttributes.addFlashAttribute("success_msg", "Category updated successfully.");
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        long productCount = productRepository.countByCategoryIdAndStatus(id, true);
        if (productCount > 0) {
            redirectAttributes.addFlashAttribute("error_msg", "Warning: Cannot deactivate category. Please assign its "
                    + productCount + " product(s) to a new category first.");
            return "redirect:/admin/categories";
        }

        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            category.setStatus("deleted");
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("success_msg", "Category formally deactivated.");
        }
        return "redirect:/admin/categories";
    }
}
