package couriersystem;

import models.*;
import services.*;
import utils.*;
import java.util.*;

public class NewMain {
    private static Scanner scanner = new Scanner(System.in);
    
    // Collections using new class structure
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Parcel> parcels = new ArrayList<>();
    private static ArrayList<Delivery> deliveries = new ArrayList<>();
    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static ArrayList<Payment> payments = new ArrayList<>();
    
    // Current user (polymorphic - can be Customer or Staff)
    private static User currentUser = null;
    private static boolean isAdmin = false;
    
    // Services
    private static ParcelService parcelService = new ParcelService();
    private static PaymentService paymentService = new PaymentService();
    private static ArrayDemo arrayDemo = new ArrayDemo();
    
    public static void main(String[] args) {
        System.out.println("=== COURIER PARCEL MANAGEMENT SYSTEM ===");
        System.out.println("Phase 2 - Complete OOP Implementation");
        System.out.println("=======================================\n");
        
        initializeSampleData();
        loginMenu();
    }
    
    // ==================== INITIALIZE SAMPLE DATA ====================
    private static void initializeSampleData() {
        System.out.println("Initializing system with sample data...\n");
        
        // 1. Create customers
        Customer customer1 = new Customer("C001", "Ali", "ali@email.com", 
                                         "pass123", "012-3456789", "123 Main Street, KL");
        Customer customer2 = new Customer("C002", "Siti", "siti@email.com", 
                                         "pass456", "013-4567890", "456 Oak Avenue, PJ");
        Customer customer3 = new Customer("C003", "Ahmad", "ahmad@email.com", 
                                         "pass789", "014-5678901", "789 Pine Road, Penang");
        
        users.add(customer1);
        users.add(customer2);
        users.add(customer3);
        
        // 2. Create staff
        Staff staff1 = new Staff("S001", "Raju", "raju@courier.com", 
                                "staff123", "011-2223333", "Delivery Driver", 2500.00);
        Staff staff2 = new Staff("S002", "Mei Ling", "mei@courier.com", 
                                "staff456", "011-4445555", "Warehouse Manager", 3200.00);
        Staff staff3 = new Staff("S003", "Kumar", "kumar@courier.com", 
                                "staff789", "011-6667777", "Customer Service", 2800.00);
        
        users.add(staff1);
        users.add(staff2);
        users.add(staff3);
        
        // 3. Create parcels using FACTORY PATTERN
        System.out.println("Creating sample parcels using Factory Pattern...");
        
        // Standard Parcel
        Parcel parcel1 = ParcelFactory.createParcel(
            "STANDARD", "P001", customer1, customer2, 
            2.5, "30x20x15", "Small Box", ""
        );
        parcels.add(parcel1);
        arrayDemo.addToArchive(parcel1);
        parcelService.addParcel(parcel1);
        
        // Express Parcel
        Parcel parcel2 = ParcelFactory.createParcel(
            "EXPRESS", "P002", customer2, customer3,
            1.2, "25x15x10", "Documents", ""
        );
        parcels.add(parcel2);
        arrayDemo.addToArchive(parcel2);
        parcelService.addParcel(parcel2);
        
        // International Parcel
        Parcel parcel3 = ParcelFactory.createParcel(
            "INTERNATIONAL", "P003", customer3, customer1,
            5.8, "40x30x25", "Electronics", "Singapore"
        );
        parcels.add(parcel3);
        arrayDemo.addToArchive(parcel3);
        parcelService.addParcel(parcel3);
        
        // 4. Create vehicles
        vehicles.add(new Vehicle("V001", "Van", "ABC1234", 500.0));
        vehicles.add(new Vehicle("V002", "Motorcycle", "DEF5678", 50.0));
        vehicles.add(new Vehicle("V003", "Truck", "GHI9012", 2000.0));
        
        // 5. Create deliveries
        deliveries.add(new Delivery("D001", parcel1, staff1));
        deliveries.add(new Delivery("D002", parcel2, staff2));
        deliveries.add(new Delivery("D003", parcel3, staff3));
        
        // 6.Assign a vehicle to show it works
        vehicles.get(0).assignVehicle("D001");  // Assign van to D001
        deliveries.get(0).setAssignedVehicle(vehicles.get(0));
        deliveries.get(0).assignRoute("Main City Route - Van ABC1234");
        
        // 7. Create payments
        payments.add(new Payment("PAY001", parcel1.getPrice(), "Credit Card"));
        payments.add(new Payment("PAY002", parcel2.getPrice(), "Online Banking"));
        payments.get(0).processPayment();
        payments.get(1).processPayment();
        
        System.out.println("✓ Sample data initialized successfully!");
        System.out.println("  - " + users.size() + " users (customers & staff)");
        System.out.println("  - " + parcels.size() + " parcels (using Factory Pattern)");
        System.out.println("  - " + vehicles.size() + " vehicles");
        System.out.println("  - " + deliveries.size() + " deliveries");
        System.out.println("  - " + payments.size() + " payments\n");
    }
    
    // ==================== LOGIN MENU ====================
    private static void loginMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("=== COURIER SYSTEM LOGIN ===");
            System.out.println("1. Customer Login");
            System.out.println("2. Staff Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Register New Customer");
            System.out.println("5. Demonstrate Arrays (For Rubric)");
            System.out.println("6. Exit System");
            System.out.print("Select option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        customerLogin();
                        break;
                    case 2:
                        staffLogin();
                        break;
                    case 3:
                        adminLogin();
                        break;
                    case 4:
                        registerNewCustomer();
                        break;
                    case 5:
                        demonstrateArrays();
                        break;
                    case 6:
                        exit = true;
                        System.out.println("Thank you for using Courier System!");
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number (1-6)!");
                scanner.nextLine();
            }
        }
    }
    
    private static void customerLogin() {
        System.out.print("\nEnter Customer ID (e.g., C001): ");
        String id = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        for (User user : users) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                if (customer.getUserId().equals(id) && customer.validateLogin(password)) {
                    currentUser = customer;
                    customer.login();
                    customerDashboard();
                    return;
                }
            }
        }
        System.out.println("Invalid customer credentials!");
    }
    
    private static void staffLogin() {
        System.out.print("\nEnter Staff ID (e.g., S001): ");
        String id = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        for (User user : users) {
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                if (staff.getUserId().equals(id) && staff.validateLogin(password)) {
                    currentUser = staff;
                    staff.login();
                    staffDashboard();
                    return;
                }
            }
        }
        System.out.println("Invalid staff credentials!");
    }
    
    private static void adminLogin() {
        System.out.print("\nEnter Admin Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        
        if (username.equals("admin") && password.equals("admin123")) {
            isAdmin = true;
            System.out.println("Welcome, Administrator!");
            adminDashboard();
        } else {
            System.out.println("Invalid admin credentials!");
        }
    }
    
    private static void registerNewCustomer() {
        System.out.println("\n=== REGISTER NEW CUSTOMER ===");
        
        System.out.print("Enter Customer ID (e.g., C004): ");
        String id = scanner.nextLine();
        
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        
        if (!Validator.isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }
        
        if (!Validator.isValidPhone(phone)) {
            System.out.println("Invalid phone number!");
            return;
        }
        
        // Check if ID already exists
        for (User user : users) {
            if (user.getUserId().equals(id)) {
                System.out.println("Customer ID already exists!");
                return;
            }
        }
        
        Customer newCustomer = new Customer(id, name, email, password, phone, address);
        users.add(newCustomer);
        
        System.out.println("✅ Customer registered successfully!");
        System.out.println("Please login with your new credentials.");
    }
    
    private static void demonstrateArrays() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ARRAY DEMONSTRATION (For Rubric Requirements)");
        System.out.println("=".repeat(50));
        
        // 1. Demonstrate primitive and string arrays
        ArrayDemo.demonstrateArrays();
        
        // 2. Demonstrate object array from ParcelService
        parcelService.demonstrateArrayUsage();
        
        // 3. Show our archive array
        System.out.println("\n=== System Parcel Archive (Object Array) ===");
        arrayDemo.displayArchive();
        
        // 4. Show vehicle array
        System.out.println("\n=== Vehicle Array ===");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.println((i + 1) + ". " + vehicles.get(i).getVehicleId() + 
                             " - " + vehicles.get(i).getVehicleType());
        }
        
        System.out.println("\n✓ All array requirements demonstrated!");
        System.out.println("  - Primitive arrays ✓");
        System.out.println("  - String arrays ✓");
        System.out.println("  - Object arrays ✓");
        System.out.println("  - Array operations ✓");
    }
    
    // ==================== CUSTOMER DASHBOARD ====================
    private static void customerDashboard() {
        if (!(currentUser instanceof Customer)) {
            System.out.println("Access denied! Not a customer.");
            return;
        }
        
        Customer customer = (Customer) currentUser;
        boolean logout = false;
        
        while (!logout) {
            System.out.println("\n=== CUSTOMER DASHBOARD ===");
            System.out.println("Welcome, " + customer.getName() + "!");
            System.out.println("Customer ID: " + customer.getUserId());
            System.out.println("Loyalty Points: " + customer.getLoyaltyPoints());
            System.out.println("=".repeat(30));
            
            System.out.println("1. Send New Parcel (Factory Pattern)");
            System.out.println("2. View My Parcels");
            System.out.println("3. Track Parcel");
            System.out.println("4. Make Payment");
            System.out.println("5. View My Profile");
            System.out.println("6. View All Parcel Types");
            System.out.println("7. Logout");
            System.out.print("Select option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        createParcelForCustomer(customer);
                        break;
                    case 2:
                        viewCustomerParcels(customer);
                        break;
                    case 3:
                        trackCustomerParcel(customer);
                        break;
                    case 4:
                        makePaymentForCustomer(customer);
                        break;
                    case 5:
                        customer.displayCustomerInfo();
                        break;
                    case 6:
                        ParcelFactory.displayParcelTypes();
                        break;
                    case 7:
                        customer.logout();
                        currentUser = null;
                        logout = true;
                        System.out.println("Logged out successfully!");
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number (1-7)!");
                scanner.nextLine();
            }
        }
    }
    
    private static void createParcelForCustomer(Customer sender) {
        System.out.println("\n=== CREATE NEW PARCEL ===");
        System.out.println("Sender: " + sender.getName());
        
        // Show parcel types
        ParcelFactory.displayParcelTypes();
        
        System.out.print("\nEnter parcel type (STANDARD/EXPRESS/INTERNATIONAL): ");
        String parcelType = scanner.nextLine().toUpperCase();
        
        if (!parcelType.equals("STANDARD") && !parcelType.equals("EXPRESS") && 
            !parcelType.equals("INTERNATIONAL")) {
            System.out.println("Invalid parcel type!");
            return;
        }
        
        // Auto-generate parcel ID
        String parcelId = "P" + (parcels.size() + 1001);
        
        System.out.print("Enter parcel description: ");
        String description = scanner.nextLine();
        
        System.out.print("Enter weight (kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();
        
        if (!Validator.isValidWeight(weight)) {
            System.out.println("Invalid weight! Must be 0.1-100 kg");
            return;
        }
        
        System.out.print("Enter dimensions (LxWxH in cm): ");
        String dimensions = scanner.nextLine();
        
        // Get recipient
        System.out.print("Enter recipient Customer ID: ");
        String recipientId = scanner.nextLine();
        
        Customer recipient = null;
        for (User user : users) {
            if (user instanceof Customer && user.getUserId().equals(recipientId)) {
                recipient = (Customer) user;
                break;
            }
        }
        
        if (recipient == null) {
            System.out.println("Recipient not found!");
            return;
        }
        
        // Additional info for international parcels
        String additionalInfo = "";
        if (parcelType.equals("INTERNATIONAL")) {
            System.out.print("Enter destination country: ");
            additionalInfo = scanner.nextLine();
        }
        
        // USE FACTORY PATTERN HERE
        System.out.println("\nCreating parcel using Factory Pattern...");
        Parcel newParcel = ParcelFactory.createParcel(
            parcelType,
            parcelId,
            sender,
            recipient,
            weight,
            dimensions,
            description,
            additionalInfo
        );
        
        parcels.add(newParcel);
        arrayDemo.addToArchive(newParcel);
        parcelService.addParcel(newParcel);
        
        System.out.println("\n📦 Creating delivery assignment for new parcel...");
    
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
        String deliveryId = "D" + (deliveries.size() + 1001);
        Delivery newDelivery = new Delivery(deliveryId, newParcel, availableStaff);
        deliveries.add(newDelivery);
        
        System.out.println("✅ Delivery created: " + deliveryId);
        System.out.println("   Assigned to: " + availableStaff.getName());
        System.out.println("   Status: Scheduled");
    } else {
        System.out.println("⚠️  No staff available. Delivery will be assigned later.");
    }
        
        System.out.println("\n✅ PARCEL CREATED SUCCESSFULLY!");
        System.out.println("Factory Pattern used: ParcelFactory.createParcel()");
        newParcel.displayParcelInfo();
        
        // Add loyalty points
        sender.addLoyaltyPoints(10);
        System.out.println("+10 Loyalty Points added to your account!");
    }
    
    private static void viewCustomerParcels(Customer customer) {
        System.out.println("\n=== YOUR PARCELS ===");
        
        boolean hasParcels = false;
        for (Parcel parcel : parcels) {
            if (parcel.getSender().getUserId().equals(customer.getUserId())) {
                System.out.println("\nParcel ID: " + parcel.getParcelId());
                System.out.println("Type: " + parcel.getClass().getSimpleName());
                System.out.println("Description: " + parcel.getDescription());
                System.out.println("Status: " + parcel.getStatus());
                System.out.println("Price: RM" + parcel.getPrice());
                System.out.println("Recipient: " + parcel.getReceiver().getName());
                System.out.println("-".repeat(30));
                hasParcels = true;
            }
        }
        
        if (!hasParcels) {
            System.out.println("You have no parcels yet.");
        }
    }
    
    private static void trackCustomerParcel(Customer customer) {
        System.out.println("\n=== TRACK PARCEL ===");
        
        // Show customer's parcels
        ArrayList<Parcel> customerParcels = new ArrayList<>();
        int count = 0;
        
        for (Parcel parcel : parcels) {
            if (parcel.getSender().getUserId().equals(customer.getUserId())) {
                System.out.println((count + 1) + ". " + parcel.getParcelId() + 
                                 " - " + parcel.getDescription() + 
                                 " (" + parcel.getStatus() + ")");
                customerParcels.add(parcel);
                count++;
            }
        }
        
        if (count == 0) {
            System.out.println("You have no parcels to track.");
            return;
        }
        
        System.out.print("\nSelect parcel to track (1-" + count + "): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice < 1 || choice > customerParcels.size()) {
                System.out.println("Invalid selection!");
                return;
            }
            
            Parcel selectedParcel = customerParcels.get(choice - 1);
            selectedParcel.displayParcelInfo();
            
            // Show delivery info if exists
            for (Delivery delivery : deliveries) {
                if (delivery.getParcel().getParcelId().equals(selectedParcel.getParcelId())) {
                    delivery.displayDeliveryInfo();
                }
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Please enter a number!");
            scanner.nextLine();
        }
    }
    
   private static void makePaymentForCustomer(Customer customer) {
    System.out.println("\n=== MAKE PAYMENT ===");
    
    // Find unpaid parcels (status = "Created")
    ArrayList<Parcel> unpaidParcels = new ArrayList<>();
    int count = 0;
    
    for (Parcel parcel : parcels) {
        if (parcel.getSender().getUserId().equals(customer.getUserId()) &&
            parcel.getStatus().equals("Created")) {
            
            double amount = parcel.getPrice();
            String feeInfo = "";
            String deliveryInfo = "";
            
            // Show URGENT FEE for Express parcels
            if (parcel instanceof ExpressParcel) {
                ExpressParcel expressParcel = (ExpressParcel) parcel;
                double urgentFee = expressParcel.getUrgentFee();
                feeInfo = " (+RM" + String.format("%.2f", urgentFee) + " urgent fee)";
            }
            
            // Show CUSTOMS FEE for International parcels
            if (parcel instanceof InternationalParcel) {
                InternationalParcel intParcel = (InternationalParcel) parcel;
                double customsFee = intParcel.getCustomsFee();
                feeInfo = " (+RM" + String.format("%.2f", customsFee) + " customs fee)";
            }
            
            // Show delivery time estimate
            if (parcel instanceof StandardParcel) {
                StandardParcel stdParcel = (StandardParcel) parcel;
                deliveryInfo = " (Delivery: " + stdParcel.getMaxDeliveryDays() + " days)";
            } else if (parcel instanceof ExpressParcel) {
                ExpressParcel expParcel = (ExpressParcel) parcel;
                deliveryInfo = " (Guaranteed: 24 hours)";
            }
            
            System.out.println("\n" + (count + 1) + ". " + parcel.getParcelId());
            System.out.println("   Description: " + parcel.getDescription());
            System.out.println("   Type: " + parcel.getClass().getSimpleName());
            System.out.println("   Weight: " + parcel.getWeight() + " kg");
            System.out.println("   Amount: RM" + String.format("%.2f", amount) + feeInfo);
            System.out.println("   Service: " + deliveryInfo);
            System.out.println("   Recipient: " + parcel.getReceiver().getName());
            
            unpaidParcels.add(parcel);
            count++;
        }
    }
    
    if (count == 0) {
        System.out.println("\nNo unpaid parcels found.");
        System.out.println("All your parcels are either paid or delivered.");
        return;
    }
    
    System.out.print("\nSelect parcel to pay (1-" + count + "): ");
    
    try {
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice < 1 || choice > unpaidParcels.size()) {
            System.out.println("❌ Invalid selection!");
            return;
        }
        
        Parcel selectedParcel = unpaidParcels.get(choice - 1);
        double amount = selectedParcel.getPrice();
        
        System.out.println("\n" + "=".repeat(40));
        System.out.println("        PAYMENT DETAILS");
        System.out.println("=".repeat(40));
        System.out.println("Parcel ID: " + selectedParcel.getParcelId());
        System.out.println("Description: " + selectedParcel.getDescription());
        
        // Show detailed breakdown for Express parcels
        if (selectedParcel instanceof ExpressParcel) {
            ExpressParcel expressParcel = (ExpressParcel) selectedParcel;
            System.out.println("\n--- Cost Breakdown ---");
            System.out.println("Base shipping: RM" + String.format("%.2f", (amount - expressParcel.getUrgentFee())));
            System.out.println("Urgent fee: RM" + String.format("%.2f", expressParcel.getUrgentFee()));
            System.out.println("Guaranteed delivery: Within 24 hours");
        }
        
        // Show detailed breakdown for International parcels
        if (selectedParcel instanceof InternationalParcel) {
            InternationalParcel intParcel = (InternationalParcel) selectedParcel;
            System.out.println("\n--- Cost Breakdown ---");
            System.out.println("Base shipping: RM" + String.format("%.2f", (amount - intParcel.getCustomsFee())));
            System.out.println("Customs fee: RM" + String.format("%.2f", intParcel.getCustomsFee()));
            System.out.println("Destination: " + intParcel.getDestinationCountry());
        }
        
        System.out.println("\n" + "-".repeat(40));
        System.out.println("TOTAL AMOUNT: RM" + String.format("%.2f", amount));
        System.out.println("=".repeat(40));
        
        System.out.println("\nSelect Payment Method:");
        System.out.println("1. 💳 Credit/Debit Card");
        System.out.println("2. 🏦 Online Banking");
        System.out.println("3. 💰 Cash on Delivery");
        System.out.print("Choice (1-3): ");
        
        int methodChoice = scanner.nextInt();
        scanner.nextLine();
        
        String paymentMethod = "";
        String paymentDetails = "";
        
        switch (methodChoice) {
            case 1:
                paymentMethod = "Credit Card";
                System.out.print("Enter card number (16 digits): ");
                String card = scanner.nextLine();
                System.out.print("Enter cardholder name: ");
                String name = scanner.nextLine();
                System.out.print("Enter expiry (MM/YY): ");
                String expiry = scanner.nextLine();
                System.out.print("Enter CVV: ");
                String cvv = scanner.nextLine();
                
                // Simple validation
                if (card.length() != 16 || !card.matches("\\d+")) {
                    System.out.println("❌ Invalid card number! Must be 16 digits.");
                    return;
                }
                
                paymentDetails = "Card: ****" + card.substring(12) + 
                               ", Exp: " + expiry;
                System.out.println("\n🔐 Processing secure payment...");
                Thread.sleep(1500); // Simulate processing
                break;
                
            case 2:
                paymentMethod = "Online Banking";
                System.out.println("\nAvailable Banks:");
                System.out.println("1. Maybank2u");
                System.out.println("2. CIMB Clicks");
                System.out.println("3. Bank Islam");
                System.out.print("Select bank: ");
                int bankChoice = scanner.nextInt();
                scanner.nextLine();
                
                String bankName = "";
                switch (bankChoice) {
                    case 1: bankName = "Maybank2u"; break;
                    case 2: bankName = "CIMB Clicks"; break;
                    case 3: bankName = "Bank Islam"; break;
                    default: bankName = "Online Banking";
                }
                
                System.out.println("\n🌐 Redirecting to " + bankName + " portal...");
                Thread.sleep(1500); // Simulate redirect
                paymentDetails = "Bank: " + bankName;
                break;
                
            case 3:
                paymentMethod = "Cash on Delivery";
                System.out.println("\n💰 Payment will be collected upon delivery.");
                System.out.println("Delivery staff will provide receipt.");
                paymentDetails = "To be collected";
                break;
                
            default:
                System.out.println("❌ Invalid choice! Using default payment.");
                paymentMethod = "Unknown";
        }
        
        // Create payment
        String paymentId = "PAY" + System.currentTimeMillis();
        Payment payment = new Payment(paymentId, amount, paymentMethod);
        
        // Process payment
        System.out.println("\n⏳ Processing payment...");
        Thread.sleep(1000); // Simulate processing
        
        payment.processPayment();
        payments.add(payment);
        
        // Update parcel status
        selectedParcel.updateStatus("Paid - Processing");
        
        // Find and update delivery status if exists
        for (Delivery delivery : deliveries) {
            if (delivery.getParcel().getParcelId().equals(selectedParcel.getParcelId())) {
                delivery.updateDeliveryStatus("Processing");
                break;
            }
        }
        
        // Add loyalty points (10 points per RM10 spent)
        int points = (int)(amount / 10) * 10;
        if (points < 5) points = 5; // Minimum 5 points
        customer.addLoyaltyPoints(points);
        
        // Display receipt
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          PAYMENT RECEIPT");
        System.out.println("=".repeat(50));
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Date: " + java.time.LocalDateTime.now());
        System.out.println("-".repeat(50));
        System.out.println("Parcel: " + selectedParcel.getParcelId());
        System.out.println("Description: " + selectedParcel.getDescription());
        System.out.println("Amount: RM" + String.format("%.2f", amount));
        System.out.println("Method: " + paymentMethod);
        System.out.println("Details: " + paymentDetails);
        System.out.println("Status: ✅ COMPLETED");
        System.out.println("-".repeat(50));
        System.out.println("Loyalty Points: +" + points);
        System.out.println("Total Points: " + customer.getLoyaltyPoints());
        System.out.println("=".repeat(50));
        
        System.out.println("\n✅ PAYMENT SUCCESSFUL!");
        System.out.println("📧 Receipt has been sent to " + customer.getEmail());
        System.out.println("📦 Your parcel is now being processed.");
        System.out.println("🔔 You will be notified when it's shipped.");
        
    } catch (InputMismatchException e) {
        System.out.println("❌ Invalid input! Please enter numbers only.");
        scanner.nextLine();
    } catch (InterruptedException e) {
        System.out.println("Payment processing interrupted.");
    }
}

    
    // ==================== STAFF DASHBOARD ====================
    private static void staffDashboard() {
        if (!(currentUser instanceof Staff)) {
            System.out.println("Access denied! Staff login required.");
            return;
        }
        
        Staff staff = (Staff) currentUser;
        boolean logout = false;
        
        while (!logout) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("         STAFF DASHBOARD");
            System.out.println("=".repeat(40));
            System.out.println("Staff ID: " + staff.getUserId());
            System.out.println("Name: " + staff.getName());
            System.out.println("Role: " + staff.getRole());
            System.out.println("Available: " + (staff.isAvailable() ? "✅ Yes" : "❌ No"));
            System.out.println("-".repeat(40));
            
            System.out.println("\n1. View My Profile");
            System.out.println("2. View Assigned Deliveries");
            System.out.println("3. Update Parcel Status");
            System.out.println("4. Mark Delivery as Complete");
            System.out.println("5. View Available Vehicles");
            System.out.println("6. Toggle Availability");
            System.out.println("7. View All Parcels");
            System.out.println("8. Logout");
            System.out.print("\nSelect option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        viewStaffProfile(staff);
                        break;
                    case 2:
                        viewAssignedDeliveries(staff);
                        break;
                    case 3:
                        updateParcelStatus();
                        break;
                    case 4:
                        markDeliveryComplete(staff);
                        break;
                    case 5:
                        viewAvailableVehicles();
                        break;
                    case 6:
                        toggleStaffAvailability(staff);
                        break;
                    case 7:
                        viewAllParcelsForStaff();
                        break;
                    case 8:
                        logout = true;
                        staff.logout();
                        currentUser = null;
                        System.out.println("✅ Staff logged out successfully!");
                        break;
                    default:
                        System.out.println("❌ Invalid option! Please choose 1-8.");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
    }
    
    private static void viewStaffProfile(Staff staff) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("        STAFF PROFILE");
        System.out.println("=".repeat(40));
        
        staff.displayStaffInfo();
        
        // Additional staff statistics
        System.out.println("\n--- Delivery Statistics ---");
        int totalDeliveries = 0;
        int completedDeliveries = 0;
        
        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryPerson() != null && 
                delivery.getDeliveryPerson().getUserId().equals(staff.getUserId())) {
                totalDeliveries++;
                if (delivery.getStatus().equals("Delivered")) {
                    completedDeliveries++;
                }
            }
        }
        
        System.out.println("Total Assigned Deliveries: " + totalDeliveries);
        System.out.println("Completed Deliveries: " + completedDeliveries);
        System.out.println("Success Rate: " + 
                          (totalDeliveries > 0 ? (completedDeliveries * 100 / totalDeliveries) + "%" : "N/A"));
    }
    
    private static void viewAssignedDeliveries(Staff staff) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("     ASSIGNED DELIVERIES");
        System.out.println("=".repeat(40));
        
        boolean hasDeliveries = false;
        
        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryPerson() != null && 
                delivery.getDeliveryPerson().getUserId().equals(staff.getUserId())) {
                
                System.out.println("\n📦 Delivery ID: " + delivery.getDeliveryId());
                System.out.println("   Parcel: " + delivery.getParcel().getParcelId() + 
                                 " - " + delivery.getParcel().getDescription());
                System.out.println("   Status: " + delivery.getStatus());
                System.out.println("   Route: " + delivery.getRoute());
                System.out.println("   Est. Delivery: " + delivery.getEstimatedTime());
                System.out.println("   Recipient: " + delivery.getParcel().getReceiver().getName());
                
                // Show parcel details if needed
                System.out.print("   View parcel details? (y/n): ");
                String viewDetails = scanner.nextLine();
                if (viewDetails.equalsIgnoreCase("y")) {
                    delivery.getParcel().displayParcelInfo();
                }
                
                System.out.println("-".repeat(30));
                hasDeliveries = true;
            }
        }
        
        if (!hasDeliveries) {
            System.out.println("No deliveries assigned to you at the moment.");
            System.out.println("Please check back later or contact your supervisor.");
        }
    }
    
    private static void updateParcelStatus() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("     UPDATE PARCEL STATUS");
        System.out.println("=".repeat(40));
        
        System.out.print("Enter Parcel ID: ");
        String parcelId = scanner.nextLine();
        
        // Find parcel
        Parcel foundParcel = null;
        for (Parcel parcel : parcels) {
            if (parcel.getParcelId().equals(parcelId)) {
                foundParcel = parcel;
                break;
            }
        }
        
        if (foundParcel == null) {
            System.out.println("❌ Parcel not found!");
            return;
        }
        
        System.out.println("\nCurrent Status: " + foundParcel.getStatus());
        System.out.println("\nSelect new status:");
        System.out.println("1. 📦 Processing");
        System.out.println("2. 🚚 In Transit");
        System.out.println("3. 🚀 Out for Delivery");
        System.out.println("4. ✅ Delivered");
        System.out.println("5. ↩️ Returned");
        System.out.print("Choice (1-5): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            String newStatus;
            switch (choice) {
                case 1:
                    newStatus = "Processing";
                    break;
                case 2:
                    newStatus = "In Transit";
                    break;
                case 3:
                    newStatus = "Out for Delivery";
                    break;
                case 4:
                    newStatus = "Delivered";
                    break;
                case 5:
                    newStatus = "Returned";
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
                    return;
            }
            
            foundParcel.updateStatus(newStatus);
            System.out.println("✅ Status updated to: " + newStatus);
            
            // Update delivery status if exists
            for (Delivery delivery : deliveries) {
                if (delivery.getParcel().getParcelId().equals(parcelId)) {
                    delivery.updateDeliveryStatus(newStatus);
                }
            }
            
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid input! Please enter a number.");
            scanner.nextLine();
        }
    }
    
    private static void markDeliveryComplete(Staff staff) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("     MARK DELIVERY COMPLETE");
        System.out.println("=".repeat(40));
        
        // Show deliveries assigned to this staff that are "Out for Delivery"
        ArrayList<Delivery> pendingDeliveries = new ArrayList<>();
        int count = 0;
        
        System.out.println("\n📦 Deliveries ready for completion:");
        for (Delivery delivery : deliveries) {
            if (delivery.getDeliveryPerson() != null && 
                delivery.getDeliveryPerson().getUserId().equals(staff.getUserId()) &&
                delivery.getStatus().equals("Out for Delivery")) {
                
                System.out.println((count + 1) + ". Delivery ID: " + delivery.getDeliveryId());
                System.out.println("   Parcel: " + delivery.getParcel().getParcelId());
                System.out.println("   Recipient: " + delivery.getParcel().getReceiver().getName());
                System.out.println("   Address: " + 
                                 ((Customer)delivery.getParcel().getReceiver()).getAddress());
                pendingDeliveries.add(delivery);
                count++;
            }
        }
        
        if (count == 0) {
            System.out.println("No deliveries ready for completion.");
            System.out.println("Deliveries must be 'Out for Delivery' status.");
            return;
        }
        
        System.out.print("\nSelect delivery to complete (1-" + count + "): ");
        
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice < 1 || choice > pendingDeliveries.size()) {
                System.out.println("❌ Invalid selection!");
                return;
            }
            
            Delivery selectedDelivery = pendingDeliveries.get(choice - 1);
            
            System.out.println("\n=== DELIVERY CONFIRMATION ===");
            System.out.println("Delivery ID: " + selectedDelivery.getDeliveryId());
            System.out.println("Parcel: " + selectedDelivery.getParcel().getDescription());
            System.out.println("Recipient: " + selectedDelivery.getParcel().getReceiver().getName());
            
            System.out.print("\nEnter recipient signature/name: ");
            String signature = scanner.nextLine();
            
            System.out.print("Any delivery notes? (optional): ");
            String notes = scanner.nextLine();
            
            // Update status
            selectedDelivery.updateDeliveryStatus("Delivered");
            selectedDelivery.getParcel().updateStatus("Delivered");
            
            // Add success message
            System.out.println("\n🎉 DELIVERY COMPLETED SUCCESSFULLY!");
            System.out.println("📝 Signature: " + signature);
            if (!notes.isEmpty()) {
                System.out.println("📋 Notes: " + notes);
            }
            System.out.println("⏰ Time: " + java.time.LocalDateTime.now());
            
        } catch (InputMismatchException e) {
            System.out.println("❌ Invalid input!");
            scanner.nextLine();
        }
    }
    
    private static void viewAvailableVehicles() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("     AVAILABLE VEHICLES");
        System.out.println("=".repeat(40));
        
        boolean hasAvailableVehicles = false;
        
        System.out.println("\n🚗 Vehicle Fleet:");
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            System.out.println("\n" + (i + 1) + ". " + vehicle.getVehicleId());
            System.out.println("   Type: " + vehicle.getVehicleType());
            System.out.println("   Plate: " + vehicle.getPlateNumber());
            System.out.println("   Capacity: " + vehicle.getCapacity() + " kg");
            System.out.println("   Status: " + (vehicle.isAvailable() ? "✅ Available" : "❌ In Use"));
            
            if (vehicle.isAvailable()) {
                hasAvailableVehicles = true;
            }
        }
        
        if (!hasAvailableVehicles) {
            System.out.println("\n⚠️ No vehicles currently available.");
            System.out.println("Please contact the warehouse manager.");
        }
    }
    
    private static void toggleStaffAvailability(Staff staff) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("     TOGGLE AVAILABILITY");
        System.out.println("=".repeat(40));
        
        boolean currentStatus = staff.isAvailable();
        staff.setAvailable(!currentStatus);
        
        System.out.println("\n✅ Availability updated!");
        System.out.println("New Status: " + (staff.isAvailable() ? "✅ Available for assignments" : "❌ Not available"));
        
        if (!staff.isAvailable()) {
            System.out.println("\n⚠️ Note: You will not receive new delivery assignments");
            System.out.println("   until you set your status back to available.");
        }
    }
    
    private static void viewAllParcelsForStaff() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("        ALL PARCELS");
        System.out.println("=".repeat(40));
        
        if (parcels.isEmpty()) {
            System.out.println("No parcels in the system.");
            return;
        }
        
        System.out.println("\nTotal Parcels: " + parcels.size());
        System.out.println("\n📦 Parcel List:");
        
        for (int i = 0; i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);
            System.out.println("\n" + (i + 1) + ". " + parcel.getParcelId());
            System.out.println("   Type: " + parcel.getClass().getSimpleName());
            System.out.println("   Description: " + parcel.getDescription());
            System.out.println("   Status: " + parcel.getStatus());
            System.out.println("   Sender: " + parcel.getSender().getName());
            System.out.println("   Receiver: " + parcel.getReceiver().getName());
            System.out.println("-".repeat(30));
        }
        
        System.out.print("\nView details of specific parcel? (enter number or 0 to skip): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            if (choice > 0 && choice <= parcels.size()) {
                parcels.get(choice - 1).displayParcelInfo();
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
        }
    }
    
    // ==================== ADMIN DASHBOARD ====================
    private static void adminDashboard() {
        if (!isAdmin) {
            System.out.println("Access denied! Admin only.");
            return;
        }
        
        boolean logout = false;
        
        while (!logout) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. View All Users");
            System.out.println("2. View All Parcels");
            System.out.println("3. View All Deliveries");
            System.out.println("4. View System Statistics");
            System.out.println("5. Assign Staff to Delivery");
            System.out.println("6. Assign Vehicle to Delivery");
            System.out.println("7. Generate System Report");
            System.out.println("8. View Array Demonstration");
            System.out.println("9. Logout");
            System.out.print("Select option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1:
                        viewAllUsers();
                        break;
                    case 2:
                        viewAllParcels();
                        break;
                    case 3:
                        viewAllDeliveries();
                        break;
                    case 4:
                        displaySystemStatistics();
                        break;
                    case 5:
                        assignStaffToDelivery();
                        break;
                    case 6:
                        assignVehicleToDelivery();
                        break;
                    case 7:
                        generateSystemReport();
                        break;
                    case 8:
                        demonstrateArrays();
                        break;
                    case 9:
                        isAdmin = false;
                        logout = true;
                        System.out.println("Admin logged out successfully!");
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number (1-9)!");
                scanner.nextLine();
            }
        }
    }
    
    private static void viewAllUsers() {
        System.out.println("\n=== ALL SYSTEM USERS ===");
        
        System.out.println("\n--- CUSTOMERS ---");
        int customerCount = 0;
        for (User user : users) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                customer.displayCustomerInfo();
                System.out.println("-".repeat(30));
                customerCount++;
            }
        }
        
        System.out.println("\n--- STAFF ---");
        int staffCount = 0;
        for (User user : users) {
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                staff.displayStaffInfo();
                System.out.println("-".repeat(30));
                staffCount++;
            }
        }
        
        System.out.println("\nTotal Users: " + users.size());
        System.out.println("Customers: " + customerCount);
        System.out.println("Staff: " + staffCount);
    }
    
    private static void viewAllParcels() {
        System.out.println("\n=== ALL PARCELS IN SYSTEM ===");
        
        for (Parcel parcel : parcels) {
            parcel.displayParcelInfo();
            System.out.println("-".repeat(40));
        }
        
        System.out.println("\nTotal Parcels: " + parcels.size());
        
        // Count by type (showing polymorphism)
        int standard = 0, express = 0, international = 0;
        for (Parcel parcel : parcels) {
            if (parcel instanceof StandardParcel) standard++;
            else if (parcel instanceof ExpressParcel) express++;
            else if (parcel instanceof InternationalParcel) international++;
        }
        
        System.out.println("Standard Parcels: " + standard);
        System.out.println("Express Parcels: " + express);
        System.out.println("International Parcels: " + international);
    }
    
    private static void viewAllDeliveries() {
        System.out.println("\n=== ALL DELIVERIES ===");
        
        for (Delivery delivery : deliveries) {
            delivery.displayDeliveryInfo();
            System.out.println();
        }
        
        System.out.println("Total Deliveries: " + deliveries.size());
    }
    
    private static void displaySystemStatistics() {
        System.out.println("\n=== SYSTEM STATISTICS ===");
        System.out.println("Total Users: " + users.size());
        
        int customerCount = 0, staffCount = 0;
        for (User user : users) {
            if (user instanceof Customer) customerCount++;
            else if (user instanceof Staff) staffCount++;
        }
        System.out.println("  - Customers: " + customerCount);
        System.out.println("  - Staff: " + staffCount);
        
        System.out.println("Total Parcels: " + parcels.size());
        System.out.println("Total Deliveries: " + deliveries.size());
        System.out.println("Total Vehicles: " + vehicles.size());
        System.out.println("Total Payments: " + payments.size());
        
        // Calculate total revenue
        double totalRevenue = 0;
        for (Payment payment : payments) {
            if (payment.getStatus().equals("Completed")) {
                totalRevenue += payment.getAmount();
            }
        }
        System.out.println("Total Revenue: RM" + totalRevenue);
        
        // Count parcels by status
        int created = 0, processing = 0, transit = 0, delivered = 0;
        for (Parcel parcel : parcels) {
            String status = parcel.getStatus();
            if (status.contains("Created")) created++;
            else if (status.contains("Processing")) processing++;
            else if (status.contains("Transit")) transit++;
            else if (status.contains("Delivered")) delivered++;
        }
        
        System.out.println("\nParcel Status Breakdown:");
        System.out.println("  - Created/Unpaid: " + created);
        System.out.println("  - Processing: " + processing);
        System.out.println("  - In Transit: " + transit);
        System.out.println("  - Delivered: " + delivered);
    }
    
    private static void assignStaffToDelivery() {
    System.out.println("\n=== ASSIGN STAFF TO DELIVERY ===");
    
    // 🚨 CHANGE: Show ALL deliveries, not just unassigned
    System.out.println("\nAll Deliveries:");
    ArrayList<Delivery> allDeliveriesList = new ArrayList<>();
    int count = 0;
    
    for (Delivery delivery : deliveries) {
        String staffInfo = (delivery.getDeliveryPerson() == null) ? 
                          "❌ No staff assigned" : 
                          "✅ " + delivery.getDeliveryPerson().getName();
        
        System.out.println((count + 1) + ". Delivery ID: " + delivery.getDeliveryId());
        System.out.println("   Parcel: " + delivery.getParcel().getParcelId() + 
                         " - " + delivery.getParcel().getDescription());
        System.out.println("   Current Staff: " + staffInfo);
        allDeliveriesList.add(delivery);
        count++;
    }
    
    if (count == 0) {
        System.out.println("No deliveries in system.");
        return;
    }
    
    System.out.print("\nSelect delivery to assign/change staff (1-" + count + "): ");
    
    try {
        int deliveryChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (deliveryChoice < 1 || deliveryChoice > allDeliveriesList.size()) {
            System.out.println("❌ Invalid selection!");
            return;
        }
        
        Delivery selectedDelivery = allDeliveriesList.get(deliveryChoice - 1);
        
        // Show available staff
        System.out.println("\n=== AVAILABLE STAFF ===");
        ArrayList<Staff> availableStaff = new ArrayList<>();
        int staffCount = 0;
        
        for (User user : users) {
            if (user instanceof Staff) {
                Staff staff = (Staff) user;
                if (staff.isAvailable()) {
                    System.out.println((staffCount + 1) + ". " + staff.getName() + 
                                     " (" + staff.getRole() + ")");
                    availableStaff.add(staff);
                    staffCount++;
                }
            }
        }
        
        if (staffCount == 0) {
            System.out.println("No staff available!");
            return;
        }
        
        System.out.print("\nSelect staff (1-" + staffCount + "): ");
        int staffChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (staffChoice < 1 || staffChoice > availableStaff.size()) {
            System.out.println("❌ Invalid selection!");
            return;
        }
        
        Staff selectedStaff = availableStaff.get(staffChoice - 1);
        
        // Assign staff (can be new assignment or reassignment)
        if (selectedDelivery.getDeliveryPerson() != null) {
            System.out.println("🔄 Reassigning staff from " + 
                             selectedDelivery.getDeliveryPerson().getName() + 
                             " to " + selectedStaff.getName());
        }
        
        selectedDelivery.setDeliveryPerson(selectedStaff);
        
        System.out.println("✅ Staff " + selectedStaff.getName() + 
                         " assigned to Delivery " + selectedDelivery.getDeliveryId());
        
    } catch (InputMismatchException e) {
        System.out.println("❌ Invalid input!");
        scanner.nextLine();
    }
}
    
    private static void assignVehicleToDelivery() {
    System.out.println("\n" + "=".repeat(40));
    System.out.println("     ASSIGN VEHICLE TO DELIVERY");
    System.out.println("=".repeat(40));
    
    if (deliveries.isEmpty()) {
        System.out.println("No deliveries available.");
        return;
    }
    
    // Show all deliveries
    System.out.println("\n📦 Available Deliveries:");
    for (int i = 0; i < deliveries.size(); i++) {
        Delivery delivery = deliveries.get(i);
        System.out.println("\n" + (i + 1) + ". Delivery ID: " + delivery.getDeliveryId());
        System.out.println("   Parcel: " + delivery.getParcel().getParcelId() + 
                         " - " + delivery.getParcel().getDescription());
        System.out.println("   Weight: " + delivery.getParcel().getWeight() + " kg");
        System.out.println("   Current Status: " + delivery.getStatus());
        
        if (delivery.getAssignedVehicle() != null) {
            System.out.println("   Already assigned to: " + 
                             delivery.getAssignedVehicle().getVehicleId());
        } else {
            System.out.println("   Vehicle: Not assigned yet");
        }
    }
    
    System.out.print("\nSelect delivery to assign vehicle (1-" + deliveries.size() + "): ");
    
    try {
        int deliveryChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (deliveryChoice < 1 || deliveryChoice > deliveries.size()) {
            System.out.println("❌ Invalid selection!");
            return;
        }
        
        Delivery selectedDelivery = deliveries.get(deliveryChoice - 1);
        double parcelWeight = selectedDelivery.getParcel().getWeight();
        
        // Check if already has a vehicle
        if (selectedDelivery.getAssignedVehicle() != null) {
            System.out.println("\n⚠️ This delivery already has vehicle: " + 
                             selectedDelivery.getAssignedVehicle().getVehicleId());
            System.out.print("Replace it? (y/n): ");
            String replace = scanner.nextLine();
            if (!replace.equalsIgnoreCase("y")) {
                return;
            }
            // Release old vehicle
            selectedDelivery.getAssignedVehicle().releaseVehicle();
        }
        
        // Show suitable vehicles (available and can carry weight)
        System.out.println("\n🚗 Suitable Vehicles (Available & Capacity ≥ " + parcelWeight + " kg):");
        ArrayList<Vehicle> suitableVehicles = new ArrayList<>();
        int vehicleCount = 0;
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable() && vehicle.canCarry(parcelWeight)) {
                System.out.println((vehicleCount + 1) + ". " + vehicle.getVehicleId() + 
                                 " - " + vehicle.getVehicleType() + 
                                 " (Capacity: " + vehicle.getCapacity() + " kg, " +
                                 "Plate: " + vehicle.getPlateNumber() + ")");
                suitableVehicles.add(vehicle);
                vehicleCount++;
            }
        }
        
        if (vehicleCount == 0) {
            System.out.println("\n❌ No suitable vehicles available!");
            System.out.println("   Vehicles must be: 1) Available 2) Capacity ≥ " + parcelWeight + " kg");
            return;
        }
        
        System.out.print("\nSelect vehicle (1-" + vehicleCount + "): ");
        int vehicleChoice = scanner.nextInt();
        scanner.nextLine();
        
        if (vehicleChoice < 1 || vehicleChoice > suitableVehicles.size()) {
            System.out.println("❌ Invalid selection!");
            return;
        }
        
        Vehicle selectedVehicle = suitableVehicles.get(vehicleChoice - 1);
        
        // Assign vehicle to delivery
        selectedVehicle.assignVehicle(selectedDelivery.getDeliveryId());
        selectedDelivery.setAssignedVehicle(selectedVehicle);
        
        // Update route with vehicle info
        String newRoute = "Route for " + selectedVehicle.getVehicleType() + 
                         " (" + selectedVehicle.getPlateNumber() + ")";
        selectedDelivery.assignRoute(newRoute);
        
        System.out.println("\n✅ VEHICLE ASSIGNED SUCCESSFULLY!");
        System.out.println("Vehicle: " + selectedVehicle.getVehicleId() + 
                         " (" + selectedVehicle.getVehicleType() + ")");
        System.out.println("Assigned to: Delivery " + selectedDelivery.getDeliveryId());
        System.out.println("Carrying: " + selectedDelivery.getParcel().getDescription() + 
                         " (" + parcelWeight + " kg)");
        System.out.println("Route: " + newRoute);
        
    } catch (InputMismatchException e) {
        System.out.println("❌ Invalid input! Please enter numbers only.");
        scanner.nextLine();
    }
}
    
    private static void generateSystemReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("              SYSTEM REPORT");
        System.out.println("=".repeat(60));
        System.out.println("Generated: " + java.time.LocalDateTime.now());
        System.out.println();
        
        displaySystemStatistics();
        
        System.out.println("\n--- Top 3 Customers by Loyalty Points ---");
        ArrayList<Customer> topCustomers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Customer) {
                topCustomers.add((Customer) user);
            }
        }
        
        // Sort by loyalty points (descending)
        topCustomers.sort((c1, c2) -> c2.getLoyaltyPoints() - c1.getLoyaltyPoints());
        
        for (int i = 0; i < Math.min(3, topCustomers.size()); i++) {
            Customer customer = topCustomers.get(i);
            System.out.println((i + 1) + ". " + customer.getName() + 
                             " - " + customer.getLoyaltyPoints() + " points");
        }
        
        System.out.println("\n--- Recent Activities ---");
        int recentCount = Math.min(5, parcels.size());
        System.out.println("Last " + recentCount + " parcels created:");
        
        for (int i = Math.max(0, parcels.size() - recentCount); i < parcels.size(); i++) {
            Parcel parcel = parcels.get(i);
            System.out.println("  - " + parcel.getParcelId() + " (" + 
                             parcel.getClass().getSimpleName() + ") - " + 
                             parcel.getStatus());
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           END OF REPORT");
        System.out.println("=".repeat(60));
    }
}