package controllers;

import models.*;
import views.*;
import java.util.*;
import java.time.LocalDate;

public class AdminController {
    private Scanner scanner;
    private ArrayList<User> users;
    private ArrayList<Parcel> parcels;
    private ArrayList<Delivery> deliveries;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Payment> payments;
    
    public AdminController(Scanner scanner, ArrayList<User> users, 
                          ArrayList<Parcel> parcels, ArrayList<Delivery> deliveries,
                          ArrayList<Vehicle> vehicles, ArrayList<Payment> payments) {
        this.scanner = scanner;
        this.users = users;
        this.parcels = parcels;
        this.deliveries = deliveries;
        this.vehicles = vehicles;
        this.payments = payments;
    }
    
    public void showMenu() {
        boolean logout = false;
        
        while (!logout) {
            MenuView.showSectionHeader("ADMINISTRATOR DASHBOARD");
            System.out.println("1. View System Statistics & Reports");
            System.out.println("2. View All Users");
            System.out.println("3. View All Parcels");
            System.out.println("4. Manage Deliveries");
            System.out.println("5. Manage Vehicles");
            System.out.println("6. View Payment Reports");
            System.out.println("7. Add New Staff");
            System.out.println("8. Add New Vehicle");
            System.out.println("9. Generate System Report");
            System.out.println("10. Logout");
            System.out.print("\nSelect option (1-10): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1: showStatistics(); break;
                    case 2: viewAllUsers(); break;
                    case 3: viewAllParcels(); break;
                    case 4: manageDeliveries(); break;
                    case 5: manageVehicles(); break;
                    case 6: viewPaymentReports(); break;
                    case 7: addNewStaff(); break;
                    case 8: addNewVehicle(); break;
                    case 9: generateSystemReport(); break;
                    case 10: 
                        logout = true;
                        MenuView.showSuccess("Admin logged out successfully!");
                        break;
                    default: 
                        MenuView.showError("Invalid option! Choose 1-10");
                }
            } catch (InputMismatchException e) {
                MenuView.showError("Please enter a number!");
                scanner.nextLine();
            }
        }
    }
    
    // 1. SYSTEM STATISTICS & REPORTS (Matches: "analytical reports", "KPIs")
    private void showStatistics() {
        MenuView.showSectionHeader("SYSTEM STATISTICS & PERFORMANCE REPORTS");
        
        // User Statistics
        int customerCount = 0, staffCount = 0;
        for (User user : users) {
            if (user instanceof Customer) customerCount++;
            else if (user instanceof Staff && !user.getUserId().equals("ADMIN")) staffCount++;
        }
        
        System.out.println("\n📊 USER STATISTICS:");
        System.out.println("   Total Users: " + users.size());
        System.out.println("   Customers: " + customerCount);
        System.out.println("   Staff Members: " + staffCount);
        
        // Parcel Statistics
        int standardCount = 0, expressCount = 0, internationalCount = 0;
        int created = 0, processing = 0, delivered = 0;
        
        for (Parcel parcel : parcels) {
            if (parcel instanceof StandardParcel) standardCount++;
            else if (parcel instanceof ExpressParcel) expressCount++;
            else if (parcel instanceof InternationalParcel) internationalCount++;
            
            String status = parcel.getStatus();
            if (status.contains("Created")) created++;
            else if (status.contains("Processing")) processing++;
            else if (status.contains("Delivered")) delivered++;
        }
        
        System.out.println("\n📦 PARCEL STATISTICS (KPIs):");
        System.out.println("   Total Parcels: " + parcels.size());
        System.out.println("   Standard: " + standardCount);
        System.out.println("   Express: " + expressCount);
        System.out.println("   International: " + internationalCount);
        System.out.println("   Delivery Success Rate: " + 
                         (parcels.size() > 0 ? (double)delivered/parcels.size()*100 : 0) + "%");
        
        // Delivery Statistics
        int scheduled = 0, inTransit = 0, completed = 0;
        for (Delivery delivery : deliveries) {
            String status = delivery.getStatus();
            if (status.contains("Scheduled")) scheduled++;
            else if (status.contains("Transit")) inTransit++;
            else if (status.contains("Delivered")) completed++;
        }
        
        System.out.println("\n🚚 DELIVERY PERFORMANCE:");
        System.out.println("   Total Deliveries: " + deliveries.size());
        System.out.println("   Scheduled: " + scheduled);
        System.out.println("   In Transit: " + inTransit);
        System.out.println("   Completed: " + completed);
        System.out.println("   Completion Rate: " + 
                         (deliveries.size() > 0 ? (double)completed/deliveries.size()*100 : 0) + "%");
        
        // Vehicle Utilization (KPI)
        int availableVehicles = 0;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable()) availableVehicles++;
        }
        
        System.out.println("\n🚛 VEHICLE UTILIZATION:");
        System.out.println("   Total Vehicles: " + vehicles.size());
        System.out.println("   Available: " + availableVehicles);
        System.out.println("   In Use: " + (vehicles.size() - availableVehicles));
        System.out.println("   Utilization Rate: " + 
                         (vehicles.size() > 0 ? 
                         (double)(vehicles.size() - availableVehicles)/vehicles.size()*100 : 0) + "%");
        
        MenuView.showDivider();
        System.out.println("Report Generated: " + new Date());
    }
    
    // 2. VIEW ALL USERS (Matches: "data management")
    private void viewAllUsers() {
        MenuView.showSectionHeader("USER MANAGEMENT");
        
        System.out.println("\n👥 CUSTOMERS:");
        for (User user : users) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                System.out.println("  " + customer.getUserId() + " - " + customer.getName());
                System.out.println("    Email: " + customer.getEmail() + " | Phone: " + customer.getPhone());
            }
        }
        
        System.out.println("\n👔 STAFF MEMBERS:");
        for (User user : users) {
            if (user instanceof Staff && !user.getUserId().equals("ADMIN")) {
                Staff staff = (Staff) user;
                System.out.println("  " + staff.getUserId() + " - " + staff.getName());
                System.out.println("    Role: " + staff.getRole() + " | Salary: RM" + staff.getSalary());
            }
        }
    }
    
    // 3. VIEW ALL PARCELS (Matches: "system operations")
    private void viewAllParcels() {
        MenuView.showSectionHeader("PARCEL MANAGEMENT");
        
        if (parcels.isEmpty()) {
            System.out.println("No parcels in system.");
            return;
        }
        
        System.out.println("\n📦 ALL PARCELS:");
        for (Parcel parcel : parcels) {
            System.out.println("  " + parcel.getParcelId() + " - " + parcel.getDescription());
            System.out.println("    Type: " + parcel.getClass().getSimpleName() + 
                             " | Status: " + parcel.getStatus() + 
                             " | Price: RM" + parcel.getPrice());
            System.out.println("    Sender: " + parcel.getSender().getName() + 
                             " → Recipient: " + parcel.getReceiver().getName());
            System.out.println();
        }
    }
    
    // 4. MANAGE DELIVERIES (Matches: "performance monitoring")
    private void manageDeliveries() {
        MenuView.showSectionHeader("DELIVERY MANAGEMENT");
        
        System.out.println("\n1. View All Deliveries");
        System.out.println("2. Assign Staff to Delivery");
        System.out.println("3. Assign Vehicle to Delivery");
        System.out.println("4. Update Delivery Status");
        System.out.println("5. Back to Admin Menu");
        System.out.print("\nSelect option (1-5): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: viewAllDeliveries(); break;
            case 2: assignStaffToDelivery(); break;
            case 3: assignVehicleToDelivery(); break;
            case 4: updateDeliveryStatus(); break;
            case 5: return;
            default: MenuView.showError("Invalid option!");
        }
    }
    
    private void viewAllDeliveries() {
        System.out.println("\n🚚 ALL DELIVERIES:");
        for (Delivery delivery : deliveries) {
            System.out.println("  " + delivery.getDeliveryId() + 
                             " - Parcel: " + delivery.getParcel().getDescription());
            System.out.println("    Status: " + delivery.getStatus() + 
                             " | Staff: " + (delivery.getDeliveryPerson() != null ? 
                             delivery.getDeliveryPerson().getName() : "Unassigned"));
            System.out.println("    Vehicle: " + (delivery.getAssignedVehicle() != null ? 
                             delivery.getAssignedVehicle().getVehicleId() : "None"));
            System.out.println();
        }
    }
    
    private void assignStaffToDelivery() {
        System.out.print("\nEnter Delivery ID: ");
        String deliveryId = scanner.nextLine();
        
        Delivery foundDelivery = null;
        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryId().equals(deliveryId)) {
                foundDelivery = delivery;
                break;
            }
        }
        
        if (foundDelivery == null) {
            MenuView.showError("Delivery not found!");
            return;
        }
        
        System.out.println("\nAvailable Staff:");
        ArrayList<Staff> availableStaff = new ArrayList<>();
        int count = 1;
        
        for (User user : users) {
            if (user instanceof Staff && !user.getUserId().equals("ADMIN")) {
                Staff staff = (Staff) user;
                if (staff.isAvailable()) {
                    System.out.println(count + ". " + staff.getName() + " (" + staff.getRole() + ")");
                    availableStaff.add(staff);
                    count++;
                }
            }
        }
        
        if (availableStaff.isEmpty()) {
            MenuView.showError("No staff available!");
            return;
        }
        
        System.out.print("\nSelect staff (1-" + availableStaff.size() + "): ");
        int staffChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (staffChoice < 1 || staffChoice > availableStaff.size()) {
            MenuView.showError("Invalid selection!");
            return;
        }
        
        Staff selectedStaff = availableStaff.get(staffChoice - 1);
        foundDelivery.setDeliveryPerson(selectedStaff);
        foundDelivery.updateDeliveryStatus("Assigned");
        
        MenuView.showSuccess("Staff " + selectedStaff.getName() + " assigned to delivery!");
    }
    
    private void assignVehicleToDelivery() {
        System.out.print("\nEnter Delivery ID: ");
        String deliveryId = scanner.nextLine();
        
        Delivery foundDelivery = null;
        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryId().equals(deliveryId)) {
                foundDelivery = delivery;
                break;
            }
        }
        
        if (foundDelivery == null) {
            MenuView.showError("Delivery not found!");
            return;
        }
        
        System.out.println("\nAvailable Vehicles:");
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
        int count = 1;
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable() && vehicle.canCarry(foundDelivery.getParcel().getWeight())) {
                System.out.println(count + ". " + vehicle.getVehicleId() + 
                                 " (" + vehicle.getVehicleType() + ", " + 
                                 vehicle.getCapacity() + "kg)");
                availableVehicles.add(vehicle);
                count++;
            }
        }
        
        if (availableVehicles.isEmpty()) {
            MenuView.showError("No suitable vehicles available!");
            return;
        }
        
        System.out.print("\nSelect vehicle (1-" + availableVehicles.size() + "): ");
        int vehicleChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (vehicleChoice < 1 || vehicleChoice > availableVehicles.size()) {
            MenuView.showError("Invalid selection!");
            return;
        }
        
        Vehicle selectedVehicle = availableVehicles.get(vehicleChoice - 1);
        selectedVehicle.assignVehicle(deliveryId);
        foundDelivery.setAssignedVehicle(selectedVehicle);
        
        MenuView.showSuccess("Vehicle " + selectedVehicle.getVehicleId() + " assigned to delivery!");
    }
    
    private void updateDeliveryStatus() {
        System.out.print("\nEnter Delivery ID: ");
        String deliveryId = scanner.nextLine();
        
        Delivery foundDelivery = null;
        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryId().equals(deliveryId)) {
                foundDelivery = delivery;
                break;
            }
        }
        
        if (foundDelivery == null) {
            MenuView.showError("Delivery not found!");
            return;
        }
        
        System.out.println("\nCurrent Status: " + foundDelivery.getStatus());
        System.out.println("\nSelect new status:");
        System.out.println("1. Scheduled");
        System.out.println("2. Loading");
        System.out.println("3. In Transit");
        System.out.println("4. Out for Delivery");
        System.out.println("5. Delivered");
        System.out.println("6. Returned");
        System.out.print("\nChoice (1-6): ");
        
        int statusChoice = scanner.nextInt();
        scanner.nextLine();
        
        String newStatus = "";
        switch (statusChoice) {
            case 1: newStatus = "Scheduled"; break;
            case 2: newStatus = "Loading"; break;
            case 3: newStatus = "In Transit"; break;
            case 4: newStatus = "Out for Delivery"; break;
            case 5: newStatus = "Delivered"; break;
            case 6: newStatus = "Returned"; break;
            default: 
                MenuView.showError("Invalid choice!");
                return;
        }
        
        foundDelivery.updateDeliveryStatus(newStatus);
        foundDelivery.getParcel().updateStatus(newStatus);
        
        MenuView.showSuccess("Delivery status updated to: " + newStatus);
    }
    
    // 5. MANAGE VEHICLES (Matches: "system management")
    private void manageVehicles() {
        MenuView.showSectionHeader("VEHICLE MANAGEMENT");
        
        System.out.println("\n1. View All Vehicles");
        System.out.println("2. Add New Vehicle");
        System.out.println("3. Back to Admin Menu");
        System.out.print("\nSelect option (1-3): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1: viewAllVehicles(); break;
            case 2: addNewVehicle(); break;
            case 3: return;
            default: MenuView.showError("Invalid option!");
        }
    }
    
    private void viewAllVehicles() {
        System.out.println("\n🚛 VEHICLE FLEET:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("  " + vehicle.getVehicleId() + 
                             " - " + vehicle.getVehicleType() + 
                             " (" + vehicle.getPlateNumber() + ")");
            System.out.println("    Capacity: " + vehicle.getCapacity() + "kg | " +
                             "Status: " + (vehicle.isAvailable() ? "✅ Available" : "⛔ In Use"));
            if (!vehicle.isAvailable()) {
                System.out.println("    Assigned to: " + vehicle.getCurrentDeliveryId());
            }
            System.out.println();
        }
    }
    
    private void addNewVehicle() {
        MenuView.showSectionHeader("ADD NEW VEHICLE");
        
        System.out.print("Enter Vehicle ID (e.g., V003): ");
        String vehicleId = scanner.nextLine();
        
        // Check if ID exists
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                MenuView.showError("Vehicle ID already exists!");
                return;
            }
        }
        
        System.out.print("Enter Vehicle Type (Van/Motorcycle/Truck): ");
        String vehicleType = scanner.nextLine();
        
        System.out.print("Enter License Plate: ");
        String plateNumber = scanner.nextLine();
        
        System.out.print("Enter Capacity (kg): ");
        double capacity = scanner.nextDouble();
        scanner.nextLine();
        
        if (capacity <= 0) {
            MenuView.showError("Capacity must be positive!");
            return;
        }
        
        Vehicle newVehicle = new Vehicle(vehicleId, vehicleType, plateNumber, capacity);
        vehicles.add(newVehicle);
        
        MenuView.showSuccess("Vehicle " + vehicleId + " added to fleet!");
    }
    
    // 6. VIEW PAYMENT REPORTS (Matches: "financial reports")
    private void viewPaymentReports() {
        MenuView.showSectionHeader("PAYMENT REPORTS & FINANCIAL ANALYSIS");
        
        if (payments.isEmpty()) {
            System.out.println("No payment records.");
            return;
        }
        
        double totalRevenue = 0;
        double pendingAmount = 0;
        int completedCount = 0;
        int pendingCount = 0;
        
        for (Payment payment : payments) {
            if (payment.getStatus().equals("Completed")) {
                totalRevenue += payment.getAmount();
                completedCount++;
            } else {
                pendingAmount += payment.getAmount();
                pendingCount++;
            }
        }
        
        System.out.println("\n💰 FINANCIAL SUMMARY:");
        System.out.println("   Total Revenue: RM" + String.format("%.2f", totalRevenue));
        System.out.println("   Pending Payments: RM" + String.format("%.2f", pendingAmount));
        System.out.println("   Completed Payments: " + completedCount);
        System.out.println("   Pending Payments: " + pendingCount);
        System.out.println("   Total Transactions: " + payments.size());
        
        System.out.println("\n📈 RECENT PAYMENTS:");
        int count = 0;
        for (Payment payment : payments) {
            if (count >= 5) break; // Show last 5 payments
            System.out.println("  " + payment.getPaymentId() + 
                             " - RM" + payment.getAmount() + 
                             " (" + payment.getStatus() + ")");
            count++;
        }
    }
    
    // 7. ADD NEW STAFF (Matches: "system management")
    private void addNewStaff() {
        MenuView.showSectionHeader("ADD NEW STAFF MEMBER");
        
        System.out.print("Enter Staff ID (e.g., S003): ");
        String staffId = scanner.nextLine();
        
        // Check if ID exists
        for (User user : users) {
            if (user.getUserId().equals(staffId)) {
                MenuView.showError("Staff ID already exists!");
                return;
            }
        }
        
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Role: ");
        String role = scanner.nextLine();
        
        System.out.print("Enter Monthly Salary (RM): ");
        double salary = scanner.nextDouble();
        scanner.nextLine();
        
        if (salary <= 0) {
            MenuView.showError("Salary must be positive!");
            return;
        }
        
        Staff newStaff = new Staff(staffId, name, "", "pass123", "", role, salary);
        users.add(newStaff);
        
        MenuView.showSuccess("Staff member " + name + " added successfully!");
    }
    
    // 8. ADD NEW VEHICLE (Already implemented above)
    
    // 9. GENERATE SYSTEM REPORT (Matches: "generate reports")
    private void generateSystemReport() {
        MenuView.showSectionHeader("SYSTEM REPORT GENERATION");
        
        System.out.println("\n📄 REPORT SUMMARY");
        System.out.println("Generated: " + new Date());
        System.out.println("Generated By: Administrator");
        System.out.println("\n--- SYSTEM OVERVIEW ---");
        
        // Show key statistics
        showStatistics(); // Reuse statistics method
        
        System.out.println("\n--- RECOMMENDATIONS ---");
        System.out.println("1. Monitor delivery completion rates");
        System.out.println("2. Ensure vehicle utilization remains optimal");
        System.out.println("3. Track customer satisfaction through parcel feedback");
        
        System.out.println("\n✅ Report generated successfully!");
        System.out.println("(For detailed reports, implement in Phase III with database)");
    }
}