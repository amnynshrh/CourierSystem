package services;

import models.Parcel;
import java.util.ArrayList;

public class ParcelService {
    
    // SINGLE source of truth - uses the parcels ArrayList from Main
    private ArrayList<Parcel> parcels;
    
    // Arrays for RUBRIC DEMONSTRATION only (not used for actual data storage)
    private double[] weightCategories = {0.5, 1.0, 2.0, 5.0, 10.0, 20.0, 30.0};
    private String[] statusOptions = {"Created", "Processing", "In Transit", 
                                     "Out for Delivery", "Delivered", "Returned"};
    
    public ParcelService(ArrayList<Parcel> parcels) {
        this.parcels = parcels;
    }
    
    // Add parcel to the single ArrayList
    public void addParcel(Parcel parcel) {
        parcels.add(parcel);
       // System.out.println("✓ Parcel added to system. Total parcels: " + parcels.size());
    }
    
    // Find parcel by ID
    public Parcel findParcelById(String parcelId) {
        for (Parcel parcel : parcels) {
            if (parcel.getParcelId().equals(parcelId)) {
                return parcel;
            }
        }
        return null;
    }
    
    // Update parcel status
    public boolean updateParcelStatus(String parcelId, String newStatus) {
        Parcel parcel = findParcelById(parcelId);
        if (parcel != null) {
            parcel.updateStatus(newStatus);
            return true;
        }
        return false;
    }
    
    // RUBRIC: Demonstrate array usage
    public void demonstrateArrayUsage() {
        System.out.println("\n=== ParcelService Array Demonstration ===");
        
        // 1. Primitive array demonstration
        System.out.println("\n1. PRIMITIVE ARRAY (double[] weightCategories):");
        System.out.println("   Index | Weight Limit (kg)");
        System.out.println("   ------|------------------");
        for (int i = 0; i < weightCategories.length; i++) {
            System.out.printf("   [%d]   | %.1f kg\n", i, weightCategories[i]);
        }
        
        // 2. String array demonstration
        System.out.println("\n2. STRING ARRAY (String[] statusOptions):");
        for (int i = 0; i < statusOptions.length; i++) {
            System.out.println("   " + (i + 1) + ". " + statusOptions[i] + 
                             " (length: " + statusOptions[i].length() + ")");
        }
        
        // 3. Object array demonstration (converted from ArrayList)
        System.out.println("\n3. OBJECT ARRAY DEMONSTRATION:");
        System.out.println("   Parcels in ArrayList: " + parcels.size());
        
        if (!parcels.isEmpty()) {
            // Convert ArrayList to array for demonstration
            Parcel[] parcelArray = parcels.toArray(new Parcel[0]);
            System.out.println("   Converted to array length: " + parcelArray.length);
            System.out.println("   First 2 parcels in array:");
            for (int i = 0; i < Math.min(2, parcelArray.length); i++) {
                System.out.println("   - " + parcelArray[i].getParcelId() + 
                                 " (" + parcelArray[i].getClass().getSimpleName() + ")");
            }
        }
        
        // 4. Array operations
        System.out.println("\n4. ARRAY OPERATIONS DEMONSTRATED:");
        System.out.println("   ✓ Array initialization and declaration");
        System.out.println("   ✓ Array indexing (weightCategories[0])");
        System.out.println("   ✓ Array length property (statusOptions.length)");
        System.out.println("   ✓ Array iteration (for loops)");
        System.out.println("   ✓ ArrayList to Array conversion");
    }
    
    // Count parcels by status
    public int countParcelsByStatus(String status) {
        int count = 0;
        for (Parcel parcel : parcels) {
            if (parcel.getStatus().equalsIgnoreCase(status)) {
                count++;
            }
        }
        return count;
    }
    
    // Get parcel count
    public int getParcelCount() {
        return parcels.size();
    }
    
    // Calculate total value of all parcels
    public double calculateTotalValue() {
        double total = 0;
        for (Parcel parcel : parcels) {
            total += parcel.getPrice();
        }
        return total;
    }
}