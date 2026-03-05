# Internship Assignment Submission

**Module Name:** Category Management Module
**Candidate Name:** Jay
**Date:** March 2026

---

## 1. Assignment Links

*Please replace the placeholders below with the actual links after you push the code and deploy the server!*

- **GitHub Repository Link:** [https://github.com/Jay/ecommerce-project](https://github.com/Jay/ecommerce-project)
- **Live Server Deployment URL:** [https://ecommerce-project-demo.onrender.com/admin/categories](https://ecommerce-project-demo.onrender.com/admin/categories) *(Example Placeholder)*

---

## 2. End-User Documentation: Category Management

### 2.1 Overview
The **Category Management Module** enables administrators to systematically organize products into distinct categories within the e-commerce system. Categories allow customers to navigate and find products efficiently, ensuring an improved and reliable shopping experience.

### 2.2 Accessing the Dashboard
1. Log into your administrator account.
2. Navigate to the left-hand sidebar menu.
3. Click on the **Categories** tab (represented by a list icon).
4. You will be redirected to the **Manage Categories** Dashboard (`/admin/categories`), which is the centralized hub for all category operations.

### 2.3 Key Functionalities

#### A. Viewing Categories (Dashboard)
- Upon accessing the dashboard, you will see a table displaying all currently active categories.
- The table outlines the `ID`, `Name`, a brief `Description`, the number of `Products` currently associated with each category, and the `Created On` date.
- **Note:** The "Products" column provides a real-time count of active items linked to that specific category.

#### B. Creating a New Category
1. On the Category Dashboard, click the blue **"Add New Category"** button located at the top right.
2. You will be taken to the "Add Category" form.
3. Provide a **Category Name** (Required) and a **Brief Description** (Optional).
4. Click the **"Save Category"** button.
5. You will be redirected back to the Dashboard, and a green success notification will appear to confirm the addition.

#### C. Updating an Existing Category
1. On the Category Dashboard, locate the category you wish to modify.
2. Click the yellow **"Edit"** button in the *Actions* column next to the targeted category.
3. The "Edit Category" form will open, pre-filled with the existing details.
4. Modify the `Name` or `Description` as required.
5. Click **"Update Category"** to save your changes.

#### D. Deactivate / Delete Category (Soft Delete)
To maintain data integrity and sales history, the system performs a **"Soft Delete"**. Deactivated categories are hidden from the active catalog but remain preserved within the system's database.
1. Locate the category you wish to deactivate on the Dashboard.
2. Click the red **"Deactivate"** button.
3. A browser warning message will prompt you to confirm your action. 

> **Important Constraint & Safety Warning:** 
> The system will actively **prevent** you from deactivating a category if there are still products assigned to it. If you attempt to deactivate such a category, the action will be blocked, and a red error banner will appear stating: *"Cannot deactivate category. Please assign its X product(s) to a new category first."*
> To proceed, you must first reassign those products.

---

### Additional Notes for Evaluator
- **Backend Infrastructure:** Built with Java 17 and Spring Boot 3 MVC.
- **Frontend Template:** Rendered via Thymeleaf using Bootstrap 5 for responsiveness and styling.
- **Database Architecture:** Currently operates using an embedded H2 relation database for immediate standalone evaluation but contains full application property configurations to instantly swap to MySQL.
