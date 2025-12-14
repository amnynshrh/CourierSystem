package utils;

public class ArrayDemo {
    
    // SINGLE comprehensive array demonstration method
    public static void demonstrateArrays() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("      COMPREHENSIVE ARRAY DEMONSTRATION");
        System.out.println("=".repeat(60));
        
        // 1. PRIMITIVE ARRAYS
        System.out.println("\n1️⃣  PRIMITIVE ARRAYS:");
        
        // int array - delivery statistics
        int[] deliveryStats = {150, 45, 12, 8, 25}; // [total, delivered, pending, returned, express]
        System.out.println("   int[] deliveryStats = {total, delivered, pending, returned, express};");
        System.out.println("   deliveryStats.length = " + deliveryStats.length);
        System.out.println("   deliveryStats[0] = " + deliveryStats[0] + " (total deliveries)");
        System.out.println("   deliveryStats[1] = " + deliveryStats[1] + " (delivered)");
        System.out.println("   deliveryStats[2] = " + deliveryStats[2] + " (pending)");
        
        // double array - shipping rates
        double[] shippingRates = {5.0, 8.0, 12.0, 15.0, 20.0, 25.0};
        System.out.println("\n   double[] shippingRates = {5.0, 8.0, 12.0, 15.0, 20.0, 25.0};");
        System.out.print("   Shipping rates: ");
        for (int i = 0; i < shippingRates.length; i++) {
            System.out.printf("RM%.1f ", shippingRates[i]);
        }
        System.out.println("\n   Array length: " + shippingRates.length);
        
        // 2. STRING ARRAYS
        System.out.println("\n\n2️⃣  STRING ARRAYS:");
        
        String[] cities = {"Kuala Lumpur", "Petaling Jaya", "Shah Alam", "Subang Jaya", "Klang"};
        System.out.println("   String[] cities = {\"Kuala Lumpur\", \"Petaling Jaya\", ...};");
        System.out.println("   cities.length = " + cities.length);
        
        // String methods on array elements
        System.out.println("\n   String methods on array elements:");
        System.out.println("   cities[0].toUpperCase() = " + cities[0].toUpperCase());
        System.out.println("   cities[0].length() = " + cities[0].length());
        System.out.println("   cities[0].contains(\"Lumpur\") = " + cities[0].contains("Lumpur"));
        System.out.println("   cities[0].substring(0, 5) = " + cities[0].substring(0, 5));
        
        // 3. 2D ARRAY (Multi-dimensional)
        System.out.println("\n\n3️⃣  2D ARRAY (Multi-dimensional):");
        
        String[][] deliverySchedule = {
            {"Monday", "KL Central", "Raju", "8:00 AM"},
            {"Tuesday", "PJ Area", "Ahmad", "9:00 AM"},
            {"Wednesday", "Subang", "Siti", "10:00 AM"},
            {"Thursday", "Shah Alam", "Mei", "11:00 AM"},
            {"Friday", "Klang", "Ali", "1:00 PM"}
        };
        
        System.out.println("   String[][] deliverySchedule = { ... };");
        System.out.println("   deliverySchedule.length = " + deliverySchedule.length + " (rows)");
        System.out.println("   deliverySchedule[0].length = " + deliverySchedule[0].length + " (columns per row)");
        
        System.out.println("\n   Delivery Schedule:");
        System.out.println("   ┌──────────┬──────────────┬──────────┬──────────┐");
        System.out.println("   │   Day    │    Area      │  Driver  │   Time   │");
        System.out.println("   ├──────────┼──────────────┼──────────┼──────────┤");
        for (int i = 0; i < deliverySchedule.length; i++) {
            System.out.printf("   │ %-8s │ %-12s │ %-8s │ %-8s │\n",
                deliverySchedule[i][0], deliverySchedule[i][1], 
                deliverySchedule[i][2], deliverySchedule[i][3]);
        }
        System.out.println("   └──────────┴──────────────┴──────────┴──────────┘");
        
        // 4. ARRAY OPERATIONS DEMONSTRATION
        System.out.println("\n\n4️⃣  ARRAY OPERATIONS:");
        
        int[] numbers = {10, 20, 30, 40, 50};
        System.out.println("   int[] numbers = {10, 20, 30, 40, 50};");
        
        // Access element
        System.out.println("   ✓ Access element: numbers[2] = " + numbers[2]);
        
        // Modify element
        numbers[2] = 35;
        System.out.println("   ✓ Modify element: numbers[2] = 35");
        
        // Iterate with for loop
        System.out.print("   ✓ Iterate with for loop: ");
        for (int i = 0; i < numbers.length; i++) {
            System.out.print(numbers[i] + " ");
        }
        
        // Iterate with enhanced for loop
        System.out.print("\n   ✓ Iterate with enhanced for: ");
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        
        // Find maximum value
        int max = numbers[0];
        for (int num : numbers) {
            if (num > max) max = num;
        }
        System.out.println("\n   ✓ Find maximum value: " + max);
        
        // Calculate sum
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        System.out.println("   ✓ Calculate sum: " + sum);
        
        // Copy array
        int[] copy = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            copy[i] = numbers[i];
        }
        System.out.println("   ✓ Copy array: int[] copy = new int[numbers.length]");
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        END OF COMPREHENSIVE ARRAY DEMONSTRATION");
        System.out.println("=".repeat(60));
    }
}