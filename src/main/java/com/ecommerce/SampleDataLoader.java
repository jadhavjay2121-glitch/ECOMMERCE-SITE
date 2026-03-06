package com.ecommerce;

import com.ecommerce.model.Order;
import com.ecommerce.model.Payment;
import com.ecommerce.model.Category;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.PaymentRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class SampleDataLoader {

    @Bean
    CommandLineRunner loadSampleData(UserRepository userRepository, CategoryRepository categoryRepository,
            ProductRepository productRepository, OrderRepository orderRepository, PaymentRepository paymentRepository,
            CartRepository cartRepository) {
        return args -> {

            // Check if DB is already seeded
            if (userRepository.count() > 0)
                return;

            System.out.println("SEEDING START DATA FOR VIEWING ORDERS...");

            // 1. Create Mock Customer
            User customer1 = new User();
            customer1.setFirstName("John");
            customer1.setLastName("Doe");
            customer1.setPhone("555-0198");
            customer1.setEmail("john@example.com");
            userRepository.save(customer1);

            User customer2 = new User();
            customer2.setFirstName("Jane");
            customer2.setLastName("Smith");
            customer2.setPhone("555-0199");
            customer2.setEmail("jane@sample.com");
            userRepository.save(customer2);

            // 2. Mock Order #1
            Order o1 = new Order();
            o1.setCustomer(customer1);
            o1.setShippingAddress("123 Elm St, New York, NY");
            o1.setTotalAmount(new BigDecimal("199.99"));
            o1.setOrderStatus("Pending");
            o1.setStatus(true);
            orderRepository.save(o1);

            // 3. Mock Order #2
            Order o2 = new Order();
            o2.setCustomer(customer2);
            o2.setShippingAddress("456 Oak Avenue, Austin, TX");
            o2.setTotalAmount(new BigDecimal("950.50"));
            o2.setOrderStatus("Shipped");
            o2.setStatus(true);
            orderRepository.save(o2);

            // 4. Mock Order #3 (Cancelled)
            Order o3 = new Order();
            o3.setCustomer(customer1);
            o3.setShippingAddress("123 Elm St, New York, NY");
            o3.setTotalAmount(new BigDecimal("49.95"));
            o3.setOrderStatus("Cancelled");
            o3.setStatus(false);
            orderRepository.save(o3);

            // 5. Mock Associated Payments
            Payment p1 = new Payment();
            p1.setOrder(o1);
            p1.setAmount(new BigDecimal("199.99"));
            p1.setPaymentMethod("Credit Card");
            p1.setPaymentStatus("Paid");
            paymentRepository.save(p1);

            Payment p2 = new Payment();
            p2.setOrder(o2);
            p2.setAmount(new BigDecimal("950.50"));
            p2.setPaymentMethod("PayPal");
            p2.setPaymentStatus("Paid");
            paymentRepository.save(p2);

            Payment p3 = new Payment();
            p3.setOrder(o3);
            p3.setAmount(new BigDecimal("49.95"));
            p3.setPaymentMethod("Debit Card");
            p3.setPaymentStatus("Refunded");
            paymentRepository.save(p3);

            // 6. Provide mock Category and Product to seed Carts
            Category cat = new Category();
            if (categoryRepository.count() == 0) {
                cat.setName("Electronics");
                categoryRepository.save(cat);
            } else {
                cat = categoryRepository.findAll().get(0);
            }

            Product prod = new Product();
            if (productRepository.count() == 0) {
                prod.setName("Sample High-End Laptop");
                prod.setCategory(cat);
                prod.setPrice(new BigDecimal("1499.00"));
                prod.setInventoryCount(50);
                productRepository.save(prod);
            } else {
                prod = productRepository.findAll().get(0);
            }

            // 7. Simulated Cart items for Abandonment viewing
            Cart c1 = new Cart();
            c1.setCustomer(customer1);
            c1.setProduct(prod);
            c1.setQuantity(2);
            c1.setTotalPrice(prod.getPrice().multiply(new BigDecimal(2)));
            cartRepository.save(c1);

            System.out.println("Mock Generation Database Mapping Complete.");
        };
    }
}
