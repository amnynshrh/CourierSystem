package controllers;

import models.*;
import utils.Validator;
import java.util.*;

public class AuthController {
    private Scanner scanner;
    private ArrayList<User> users;
    
    public AuthController(Scanner scanner, ArrayList<User> users) {
        this.scanner = scanner;
        this.users = users;
    }
    
    public User login() {
        views.MenuView.showLoginMenu();
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: return customerLogin();
                case 2: return staffLogin();
                case 3: return adminLogin();
                case 4: return null;
                default: 
                    views.MenuView.showError("Invalid choice!");
                    return null;
            }
        } catch (InputMismatchException e) {
            views.MenuView.showError("Please enter a number!");
            scanner.nextLine();
            return null;
        }
    }
    
    private Customer customerLogin() {
        try {
            views.MenuView.showSubSection("CUSTOMER LOGIN");
            System.out.print("Enter Customer ID (e.g., C001): ");
            String id = scanner.nextLine();
            
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            
            // Search through users array
            for (User user : users) {
                if (user instanceof Customer && 
                    user.getUserId().equals(id) && 
                    user.validateLogin(password)) {
                    views.MenuView.showSuccess("Login successful! Welcome " + user.getName());
                    return (Customer) user;
                }
            }
            views.MenuView.showError("Invalid customer ID or password!");
            return null;
        } catch (Exception e) {
            views.MenuView.showError("Login error occurred!");
            return null;
        }
    }
    
    private Staff staffLogin() {
        try {
            views.MenuView.showSubSection("STAFF LOGIN");
            System.out.print("Enter Staff ID (e.g., S001): ");
            String id = scanner.nextLine();
            
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            
            // Search through users array
            for (User user : users) {
                if (user instanceof Staff && 
                    user.getUserId().equals(id) && 
                    user.validateLogin(password)) {
                    Staff staff = (Staff) user;
                    views.MenuView.showSuccess("Login successful! Welcome " + staff.getName());
                    staff.login(); // Polymorphic method call
                    return staff;
                }
            }
            views.MenuView.showError("Invalid staff ID or password!");
            return null;
        } catch (Exception e) {
            views.MenuView.showError("Login error occurred!");
            return null;
        }
    }
    
    private User adminLogin() {
        try {
            views.MenuView.showSubSection("ADMIN LOGIN");
            System.out.print("Enter Admin Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Enter Admin Password: ");
            String password = scanner.nextLine();
            
            if (username.equals("admin") && password.equals("admin123")) {
                // Create admin user (Staff with admin role)
                Staff admin = new Staff("ADMIN", "System Administrator", 
                                      "admin@courier.com", "admin123", 
                                      "000-0000000", "System Admin", 0);
                views.MenuView.showSuccess("Admin login successful!");
                return admin;
            }
            views.MenuView.showError("Invalid admin credentials!");
            return null;
        } catch (Exception e) {
            views.MenuView.showError("Login error occurred!");
            return null;
        }
    }
    
    public Customer registerCustomer() {
        try {
            views.MenuView.showSectionHeader("REGISTER NEW CUSTOMER");
            
            System.out.print("Enter Customer ID (e.g., C004): ");
            String id = scanner.nextLine().trim();
            
            // Validate ID format
            if (!id.matches("C\\d{3}")) {
                views.MenuView.showError("Invalid ID format! Must be C followed by 3 digits (e.g., C004)");
                return null;
            }
            
            // Check if ID already exists
            for (User user : users) {
                if (user.getUserId().equals(id)) {
                    views.MenuView.showError("Customer ID already exists!");
                    return null;
                }
            }
            
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine().trim();
            
            if (name.isEmpty() || name.length() < 2) {
                views.MenuView.showError("Name must be at least 2 characters!");
                return null;
            }
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            
            if (!Validator.isValidEmail(email)) {
                views.MenuView.showError("Invalid email format! Must contain @ and .");
                return null;
            }
            
            System.out.print("Enter Password (min 6 characters): ");
            String password = scanner.nextLine();
            
            if (password.length() < 6) {
                views.MenuView.showError("Password must be at least 6 characters!");
                return null;
            }
            
            System.out.print("Enter Phone Number: ");
            String phone = scanner.nextLine().trim();
            
            if (!Validator.isValidPhone(phone)) {
                views.MenuView.showError("Invalid phone number! Must be 10-11 digits with valid area code");
                return null;
            }
            
            System.out.print("Enter Address: ");
            String address = scanner.nextLine().trim();
            
            if (address.length() < 10) {
                views.MenuView.showError("Address must be at least 10 characters!");
                return null;
            }
            
            // Create new customer
            Customer newCustomer = new Customer(id, name, email, password, phone, address);
            users.add(newCustomer);
            
            views.MenuView.showSuccess("Registration successful! Customer " + name + " added.");
            return newCustomer;
            
        } catch (Exception e) {
            views.MenuView.showError("Registration error: " + e.getMessage());
            return null;
        }
    }
}