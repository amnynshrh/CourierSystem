package controllers;

import models.*;
import services.*;
import utils.*;
import views.*;
import java.util.*;

public class CustomerController {
    private Scanner scanner;
    private ParcelService parcelService;
    private PaymentService paymentService;
    private ArrayList<Parcel> parcels;
    private ArrayList<Delivery> deliveries;
    private ArrayList<Payment> payments;
    private ArrayList<User> users;
    private ArrayList<Vehicle> vehicles;
    
    public CustomerController(Scanner scanner, ParcelService parcelService, 
                            PaymentService paymentService, ArrayList<Parcel> parcels,
                            ArrayList<Delivery> deliveries, ArrayList<Payment> payments,
                            ArrayList<User> users, ArrayList<Vehicle> vehicles) {
        this.scanner = scanner;
        this.parcelService = parcelService;
        this.paymentService = paymentService;
        this.parcels = parcels;
        this.deliveries = deliveries;
        this.payments = payments;
        this.users = users;
        this.vehicles = vehicles;
    }
    
    public void showMenu(Customer customer) {
        boolean logout = false;
        
        while (!logout) {
            CustomerView.showDashboard(customer);
            CustomerView.showCustomerMenu();
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1: createParcel(customer); break;
                    case 2: viewMyParcels(customer); break;
                    case 3: trackParcel(customer); break;
                    case 4: makePayment(customer); break;
                    case 5: customer.displayCustomerInfo(); break;
                    case 6: 
                        logout = true;
                        customer.logout();
                        MenuView.showSuccess("Logged out successfully!");
                        break;
                    default: 
                        MenuView.showError("Invalid option! Choose 1-6");
                }
            } catch (InputMismatchException e) {
                MenuView.showError("Please enter a number (1-6)!");
                scanner.nextLine();
            }
        }
    }
    
   private void createParcel(Customer sender) {
    CustomerView.showParcelCreationHeader();
    CustomerView.showParcelTypes();
    
    System.out.print("\nEnter parcel type (1=STANDARD, 2=EXPRESS, 3=INTERNATIONAL): ");
    int typeChoice = scanner.nextInt();
    scanner.nextLine();
    
    String parcelType = "";
    switch (typeChoice) {
        case 1: parcelType = "STANDARD"; break;
        case 2: parcelType = "EXPRESS"; break;
        case 3: parcelType = "INTERNATIONAL"; break;
        default: 
            MenuView.showError("Invalid type! Using STANDARD.");
            parcelType = "STANDARD";
    }
    
    // ✅ FIXED: Generate sequential IDs (P003, P004, etc.)
    String parcelId = generateParcelId(parcels);
    
    System.out.print("Enter parcel description: ");
    String description = scanner.nextLine();
    
    System.out.print("Enter weight (kg): ");
    double weight = scanner.nextDouble();
    scanner.nextLine();
    
    if (!Validator.isValidWeight(weight)) {
        MenuView.showError("Invalid weight! Must be between 0.1 and 100 kg");
        return;
    }
    
    System.out.print("Enter dimensions (e.g., 30x20x15): ");
    String dimensions = scanner.nextLine();
    
    if (!Validator.isValidDimensions(dimensions)) {
        MenuView.showError("Invalid dimensions! Use format: LxWxH");
        return;
    }
    
    // Select recipient
    System.out.println("\n--- SELECT RECIPIENT ---");
    System.out.print("Enter recipient Customer ID (e.g., C002): ");
    String recipientId = scanner.nextLine();
    
    Customer recipient = findCustomerById(recipientId);
    if (recipient == null) {
        MenuView.showError("Recipient not found! Please register the recipient first.");
        return;
    }
    
    String additionalInfo = "";
    if (parcelType.equals("INTERNATIONAL")) {
        System.out.print("Enter destination country: ");
        additionalInfo = scanner.nextLine();
    }
    
    // Use FACTORY PATTERN to create parcel
    MenuView.showLoading("Creating parcel");
    Parcel newParcel = ParcelFactory.createParcel(
        parcelType, parcelId, sender, recipient, 
        weight, dimensions, description, additionalInfo
    );
    
    // Add to system
    parcelService.addParcel(newParcel);
    
    // Create delivery assignment
    assignDelivery(newParcel);
    
    // Add loyalty points
    sender.addLoyaltyPoints(10);
    
    // Show success
    CustomerView.showParcelSummary(newParcel);
    MenuView.showSuccess("Parcel created successfully! +10 loyalty points added.");
}

// ✅ NEW METHOD: Generate sequential parcel IDs
private String generateParcelId(ArrayList<Parcel> parcels) {
    if (parcels.isEmpty()) {
        return "P001"; // Start from P001
    }
    
    // Find highest existing parcel ID
    int maxId = 0;
    for (Parcel parcel : parcels) {
        String id = parcel.getParcelId();
        if (id.startsWith("P")) {
            try {
                int num = Integer.parseInt(id.substring(1));
                if (num > maxId) {
                    maxId = num;
                }
            } catch (NumberFormatException e) {
                // Ignore non-numeric IDs
            }
        }
    }
    
    // Generate next ID with leading zeros
    int nextId = maxId + 1;
    return String.format("P%03d", nextId); // P001, P002, ..., P010, P011, etc.
}
    
    private Customer findCustomerById(String id) {
        for (User user : users) {
            if (user instanceof Customer && user.getUserId().equals(id)) {
                return (Customer) user;
            }
        }
        return null;
    }
    
    private void assignDelivery(Parcel parcel) {
    // Find available staff
    Staff availableStaff = null;
    for (User user : users) {
        if (user instanceof Staff) {
            Staff staff = (Staff) user;
            if (staff.isAvailable()) {
                availableStaff = staff;
                break;
            }
        }
    }
    
    if (availableStaff != null) {
        // ✅ FIXED: Generate sequential delivery ID (D001, D002, D003...)
        String deliveryId = generateDeliveryId(deliveries);
        
        Delivery newDelivery = new Delivery(deliveryId, parcel, availableStaff);
        deliveries.add(newDelivery);
        
        // Find available vehicle
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable() && vehicle.canCarry(parcel.getWeight())) {
                vehicle.assignVehicle(deliveryId);
                newDelivery.setAssignedVehicle(vehicle);
                break;
            }
        }
        
        MenuView.showSuccess("Delivery assigned to: " + availableStaff.getName());
    } else {
        MenuView.showError("No staff available. Delivery will be assigned later.");
    }
}
    
    private void viewMyParcels(Customer customer) {
        MenuView.showSectionHeader("MY PARCELS");
        
        boolean hasParcels = false;
        int count = 1;
        
        for (Parcel parcel : parcels) {
            if (parcel.getSender().getUserId().equals(customer.getUserId())) {
                System.out.println("\n" + count + ". " + parcel.getParcelId());
                System.out.println("   Description: " + parcel.getDescription());
                System.out.println("   Type: " + parcel.getClass().getSimpleName());
                System.out.println("   Status: " + parcel.getStatus());
                System.out.println("   Price: RM" + parcel.getPrice());
                System.out.println("   Recipient: " + parcel.getReceiver().getName());
                System.out.println("   Created: " + parcel.getCreatedDate());
                hasParcels = true;
                count++;
            }
        }
        
        if (!hasParcels) {
            CustomerView.showNoParcelsMessage();
        }
    }
    
    private void trackParcel(Customer customer) {
        MenuView.showSectionHeader("TRACK PARCEL");
        
        // Show customer's parcels
        ArrayList<Parcel> customerParcels = new ArrayList<>();
        int count = 1;
        
        System.out.println("Your parcels:");
        for (Parcel parcel : parcels) {
            if (parcel.getSender().getUserId().equals(customer.getUserId())) {
                System.out.println(count + ". " + parcel.getParcelId() + 
                                 " - " + parcel.getDescription() + 
                                 " (" + parcel.getStatus() + ")");
                customerParcels.add(parcel);
                count++;
            }
        }
        
        if (customerParcels.isEmpty()) {
            CustomerView.showNoParcelsMessage();
            return;
        }
        
        System.out.print("\nSelect parcel to track (1-" + customerParcels.size() + "): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice < 1 || choice > customerParcels.size()) {
            MenuView.showError("Invalid selection!");
            return;
        }
        
        Parcel selectedParcel = customerParcels.get(choice - 1);
        CustomerView.showParcelSummary(selectedParcel);
        
        // Show delivery info if exists
        for (Delivery delivery : deliveries) {
            if (delivery.getParcel().getParcelId().equals(selectedParcel.getParcelId())) {
                delivery.displayDeliveryInfo();
            }
        }
    }
    
    private void makePayment(Customer customer) {
        CustomerView.showPaymentHeader();
        
        // Find unpaid parcels
        ArrayList<Parcel> unpaidParcels = new ArrayList<>();
        int count = 1;
        
        System.out.println("Your unpaid parcels:");
        for (Parcel parcel : parcels) {
            if (parcel.getSender().getUserId().equals(customer.getUserId()) &&
                parcel.getStatus().equals("Created")) {
                System.out.println(count + ". " + parcel.getParcelId());
                System.out.println("   Description: " + parcel.getDescription());
                System.out.println("   Amount: RM" + parcel.getPrice());
                System.out.println("   Type: " + parcel.getClass().getSimpleName());
                unpaidParcels.add(parcel);
                count++;
            }
        }
        
        if (unpaidParcels.isEmpty()) {
            System.out.println("\n✅ All your parcels are paid!");
            return;
        }
        
        System.out.print("\nSelect parcel to pay (1-" + unpaidParcels.size() + "): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice < 1 || choice > unpaidParcels.size()) {
            MenuView.showError("Invalid selection!");
            return;
        }
        
        Parcel selectedParcel = unpaidParcels.get(choice - 1);
        double amount = selectedParcel.getPrice();
        
        CustomerView.showPaymentMethods();
        int methodChoice = scanner.nextInt();
        scanner.nextLine();
        
        String paymentMethod = "";
        switch (methodChoice) {
            case 1: paymentMethod = "Credit Card"; break;
            case 2: paymentMethod = "Online Banking"; break;
            case 3: paymentMethod = "Cash on Delivery"; break;
            default: paymentMethod = "Unknown";
        }
        
        // Create payment
        Payment payment = paymentService.createPayment(amount, paymentMethod);
        paymentService.processPayment(payment.getPaymentId());
        payments.add(payment);
        
        // Update parcel status
        selectedParcel.updateStatus("Paid - Processing");
        
        // Add loyalty points
        int points = (int)(amount / 10);
        if (points < 5) points = 5;
        customer.addLoyaltyPoints(points);
        
        // Show receipt
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        PAYMENT RECEIPT");
        System.out.println("=".repeat(50));
        System.out.println("Payment ID: " + payment.getPaymentId());
        System.out.println("Date: " + new Date());
        System.out.println("Parcel: " + selectedParcel.getParcelId());
        System.out.println("Amount: RM" + amount);
        System.out.println("Method: " + paymentMethod);
        System.out.println("Status: ✅ PAID");
        System.out.println("Loyalty Points: +" + points);
        System.out.println("=".repeat(50));
        
        MenuView.showSuccess("Payment successful! Parcel is now being processed.");
    }
    
    private String generateDeliveryId(ArrayList<Delivery> deliveries) {
    if (deliveries.isEmpty()) {
        return "D001";
    }
    
    // Find highest existing delivery ID
    int maxId = 0;
    for (Delivery delivery : deliveries) {
        String id = delivery.getDeliveryId();
        if (id.startsWith("D")) {
            try {
                int num = Integer.parseInt(id.substring(1));
                if (num > maxId) {
                    maxId = num;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }
    }
    
    // Generate next ID with 3 digits
    int nextId = maxId + 1;
    return String.format("D%03d", nextId); // D001, D002, ..., D010, D011
}
}