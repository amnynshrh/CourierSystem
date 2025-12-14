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
                    case 4: trackParcelById (customer); break;
                    case 5: makePayment(customer); break;
                    case 6: customer.displayCustomerInfo(); break;
                    case 7: 
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
    MenuView.showSectionHeader("TRACK ALL MY PARCELS");
    
    ArrayList<Parcel> customerParcels = new ArrayList<>();
    int count = 1;
    
    System.out.println("Parcels sent by you OR addressed to you:");
    System.out.println("----------------------------------------");
    
    for (Parcel parcel : parcels) {
        if (parcel.getSender().getUserId().equals(customer.getUserId()) ||
            parcel.getReceiver().getUserId().equals(customer.getUserId())) {
            
            String role = parcel.getSender().getUserId().equals(customer.getUserId()) ? 
                         "📤 Sending" : "📥 Receiving";
            
            System.out.println(count + ". " + parcel.getParcelId() + 
                             " - " + parcel.getDescription() + 
                             " (" + role + ", " + parcel.getStatus() + ")");
            customerParcels.add(parcel);
            count++;
        }
    }
    
    if (customerParcels.isEmpty()) {
        System.out.println("\n📭 You have no parcels to track.");
        System.out.println("Create a new parcel or ask someone to send you one!");
        return;
    }
    
    System.out.print("\nSelect parcel to view details (1-" + customerParcels.size() + "): ");
    int choice = scanner.nextInt();
    scanner.nextLine();
    
    if (choice < 1 || choice > customerParcels.size()) {
        MenuView.showError("Invalid selection!");
        return;
    }
    
    Parcel selectedParcel = customerParcels.get(choice - 1);
    
    // Display detailed information
    CustomerView.showParcelSummary(selectedParcel);
    
    // Show delivery info if exists
    for (Delivery delivery : deliveries) {
        if (delivery.getParcel().getParcelId().equals(selectedParcel.getParcelId())) {
            System.out.println("\n--- DELIVERY INFORMATION ---");
            delivery.displayDeliveryInfo();
        }
    }
}
    
    private void trackParcelById(Customer customer) {
    MenuView.showSectionHeader("TRACK PARCEL BY ID");
    
    System.out.println("\nYou can track any parcel addressed to you.");
    System.out.print("Enter Parcel ID (e.g., P001): ");
    String parcelId = scanner.nextLine();
    
    // Find the parcel
    Parcel parcel = null;
    for (Parcel p : parcels) {
        if (p.getParcelId().equals(parcelId)) {
            parcel = p;
            break;
        }
    }
    
    if (parcel == null) {
        MenuView.showError("Parcel not found!");
        return;
    }
    
    // SECURITY: Check if customer is sender OR receiver
    boolean isSender = parcel.getSender().getUserId().equals(customer.getUserId());
    boolean isReceiver = parcel.getReceiver().getUserId().equals(customer.getUserId());
    
    if (!isSender && !isReceiver) {
        MenuView.showError("❌ ACCESS DENIED: This parcel is not addressed to you.");
        System.out.println("You can only track parcels you sent or are receiving.");
        return;
    }
    
    // Display parcel information
    System.out.println("\n" + "=".repeat(50));
    System.out.println("        PARCEL TRACKING INFORMATION");
    System.out.println("=".repeat(50));
    
    String role = isSender ? "📤 SENDER" : "📥 RECIPIENT";
    System.out.println("Your Role: " + role);
    System.out.println();
    
    System.out.printf("%-20s: %s\n", "Parcel ID", parcel.getParcelId());
    System.out.printf("%-20s: %s\n", "Description", parcel.getDescription());
    System.out.printf("%-20s: %s\n", "Type", parcel.getClass().getSimpleName());
    System.out.printf("%-20s: %.2f kg\n", "Weight", parcel.getWeight());
    System.out.printf("%-20s: %s\n", "Dimensions", parcel.getDimensions());
    System.out.printf("%-20s: RM%.2f\n", "Price", parcel.getPrice());
    System.out.printf("%-20s: %s\n", "Status", parcel.getStatus());
    System.out.printf("%-20s: %s\n", "Created Date", parcel.getCreatedDate());
    
    System.out.println("\n--- PARTIES INVOLVED ---");
    System.out.printf("%-20s: %s (%s)\n", "Sender", 
                     parcel.getSender().getName(), parcel.getSender().getUserId());
    System.out.printf("%-20s: %s (%s)\n", "Receiver", 
                     parcel.getReceiver().getName(), parcel.getReceiver().getUserId());
    
    // Find delivery information if exists
    boolean hasDelivery = false;
    for (Delivery delivery : deliveries) {
        if (delivery.getParcel().getParcelId().equals(parcelId)) {
            System.out.println("\n--- DELIVERY INFORMATION ---");
            System.out.printf("%-20s: %s\n", "Delivery ID", delivery.getDeliveryId());
            System.out.printf("%-20s: %s\n", "Delivery Status", delivery.getStatus());
            System.out.printf("%-20s: %s\n", "Route", delivery.getRoute());
            System.out.printf("%-20s: %s\n", "Est. Delivery", delivery.getEstimatedTime());
            
            if (delivery.getDeliveryPerson() != null) {
                System.out.printf("%-20s: %s\n", "Delivery Staff", 
                                delivery.getDeliveryPerson().getName());
            }
            
            if (delivery.getAssignedVehicle() != null) {
                System.out.printf("%-20s: %s (%s)\n", "Vehicle", 
                                delivery.getAssignedVehicle().getVehicleId(),
                                delivery.getAssignedVehicle().getVehicleType());
            }
            hasDelivery = true;
            break;
        }
    }
    
    if (!hasDelivery) {
        System.out.println("\n⚠️  Delivery not yet assigned. Please check back later.");
    }
    
    System.out.println("=".repeat(50));
    
    // Show delivery progress
    if (hasDelivery) {
        for (Delivery delivery : deliveries) {
            if (delivery.getParcel().getParcelId().equals(parcelId)) {
                views.DeliveryView.displayDeliveryProgress(delivery.getStatus());
                break;
            }
        }
    }
}
    
    private void makePayment(Customer customer) {
        CustomerView.showPaymentHeader();
        
        // Find unp1aid parcels
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