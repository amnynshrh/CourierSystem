package views;

import models.Customer;
import models.Parcel;

public class CustomerView {
    
    public static void showDashboard(Customer customer) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        CUSTOMER DASHBOARD");
        System.out.println("=".repeat(50));
        System.out.printf("%-15s: %s\n", "Customer ID", customer.getUserId());
        System.out.printf("%-15s: %s\n", "Name", customer.getName());
        System.out.printf("%-15s: %s\n", "Email", customer.getEmail());
        System.out.printf("%-15s: %s\n", "Phone", customer.getPhone());
        System.out.printf("%-15s: %s\n", "Address", customer.getAddress());
        System.out.printf("%-15s: %d points\n", "Loyalty Points", customer.getLoyaltyPoints());
        System.out.println("=".repeat(50));
    }
    
   public static void showCustomerMenu() {
    System.out.println("\n--- CUSTOMER OPTIONS ---");
    System.out.println("1. Send New Parcel");
        System.out.println("2. View My Parcels");
        System.out.println("3. Track My Parcels");
    System.out.println("4. Track Parcel by ID");
    System.out.println("5. Make Payment");
    System.out.println("6. View My Profile");
    System.out.println("7. Logout");
    System.out.print("\nSelect option (1-7): ");
   } 
    
    public static void showParcelTypes() {
        System.out.println("\n--- AVAILABLE PARCEL TYPES ---");
        System.out.println("1. STANDARD PARCEL");
        System.out.println("   - Delivery: 3-7 days");
        System.out.println("   - Price: RM8.00 + RM2.50/kg");
        System.out.println("   - Max weight: 30kg");
        
        System.out.println("\n2. EXPRESS PARCEL");
        System.out.println("   - Delivery: 24 hours guaranteed");
        System.out.println("   - Price: RM15.00 + RM4.00/kg + urgent fee");
        System.out.println("   - Urgent fee: RM10.00 + RM1.50/kg");
        
        System.out.println("\n3. INTERNATIONAL PARCEL");
        System.out.println("   - Delivery: 5-14 days");
        System.out.println("   - Price: RM25.00 + RM8.00/kg + customs fee");
        System.out.println("   - Customs fee: RM5.00/kg");
        System.out.println("-".repeat(40));
    }
    
    public static void showParcelCreationHeader() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        CREATE NEW PARCEL");
        System.out.println("=".repeat(50));
    }
    
    public static void showParcelSummary(Parcel parcel) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        PARCEL SUMMARY");
        System.out.println("=".repeat(50));
        System.out.printf("%-20s: %s\n", "Parcel ID", parcel.getParcelId());
        System.out.printf("%-20s: %s\n", "Type", parcel.getClass().getSimpleName());
        System.out.printf("%-20s: %s\n", "Description", parcel.getDescription());
        System.out.printf("%-20s: %.2f kg\n", "Weight", parcel.getWeight());
        System.out.printf("%-20s: %s\n", "Dimensions", parcel.getDimensions());
        System.out.printf("%-20s: RM%.2f\n", "Price", parcel.getPrice());
        System.out.printf("%-20s: %s\n", "Status", parcel.getStatus());
        System.out.printf("%-20s: %s\n", "Sender", parcel.getSender().getName());
        System.out.printf("%-20s: %s\n", "Receiver", parcel.getReceiver().getName());
        System.out.println("=".repeat(50));
    }
    
    public static void showPaymentHeader() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        MAKE PAYMENT");
        System.out.println("=".repeat(50));
    }
    
    public static void showPaymentMethods() {
        System.out.println("\n--- PAYMENT METHODS ---");
        System.out.println("1. Credit/Debit Card");
        System.out.println("2. Online Banking");
        System.out.println("3. Cash on Delivery");
        System.out.print("\nSelect payment method (1-3): ");
    }
    
    public static void showNoParcelsMessage() {
        System.out.println("\n📭 You haven't sent any parcels yet.");
        System.out.println("Create your first parcel using option 1.");
    }
}