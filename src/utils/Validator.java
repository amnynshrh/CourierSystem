package utils;

public class Validator {
    
    // Array of valid Malaysian area codes
    private static final String[] VALID_AREA_CODES = {
        "010", "011", "012", "013", "014", "015", "016", "017", "018", "019"
    };
    
    // Array of valid parcel types
    private static final String[] VALID_PARCEL_TYPES = {
        "STANDARD", "EXPRESS", "INTERNATIONAL"
    };
    
    // String methods demonstration
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        
        // String methods: contains(), length(), indexOf(), lastIndexOf()
        String trimmed = email.trim();
        return trimmed.contains("@") && 
               trimmed.contains(".") && 
               trimmed.length() >= 5 && 
               trimmed.indexOf("@") < trimmed.lastIndexOf(".");
    }
    
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        
        // Remove any spaces or dashes
        String cleaned = phone.replaceAll("[\\s-]", "");
        
        // Check if it contains only digits and has 10-11 digits
        if (!cleaned.matches("\\d{10,11}")) {
            return false;
        }
        
        // Check area code against array (ARRAY ITERATION)
        String areaCode = cleaned.substring(0, 3);
        for (String code : VALID_AREA_CODES) {
            if (areaCode.equals(code)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Method to format phone number
    public static String formatPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) return "";
        
        // String manipulation methods: replaceAll(), substring(), length()
        String cleaned = phone.replaceAll("[^0-9]", "");
        
        if (cleaned.length() == 10) {
            return cleaned.substring(0, 3) + "-" + 
                   cleaned.substring(3, 6) + "-" + 
                   cleaned.substring(6);
        } else if (cleaned.length() == 11) {
            return cleaned.substring(0, 3) + "-" + 
                   cleaned.substring(3, 7) + "-" + 
                   cleaned.substring(7);
        }
        return phone;
    }
    
    // Validate parcel dimensions
    public static boolean isValidDimensions(String dimensions) {
        if (dimensions == null || dimensions.isEmpty()) return false;
        
        // Check format: LxWxH
        if (!dimensions.matches("\\d+x\\d+x\\d+")) {
            return false;
        }
        
        // Split and validate each dimension
        String[] parts = dimensions.split("x");
        if (parts.length != 3) return false;
        
        try {
            int length = Integer.parseInt(parts[0]);
            int width = Integer.parseInt(parts[1]);
            int height = Integer.parseInt(parts[2]);
            return length > 0 && width > 0 && height > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static boolean isValidWeight(double weight) {
        return weight > 0 && weight <= 100; // Max 100kg
    }
    
    // Validate parcel type using array
    public static boolean isValidParcelType(String type) {
        if (type == null) return false;
        
        String upperType = type.toUpperCase().trim();
        
        // ARRAY SEARCH demonstration
        for (String validType : VALID_PARCEL_TYPES) {
            if (upperType.equals(validType)) {
                return true;
            }
        }
        return false;
    }
    
    // Additional validation methods
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        return trimmed.length() >= 2 && trimmed.length() <= 50;
    }
    
    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return address.trim().length() >= 10;
    }
    
    public static boolean isValidCustomerId(String id) {
        return id != null && id.matches("C\\d{3}");
    }
    
    public static boolean isValidStaffId(String id) {
        return id != null && id.matches("S\\d{3}");
    }
    
    // Demonstration of array and string methods
    public static void demonstrateValidationMethods() {
        System.out.println("\n=== VALIDATION METHODS DEMONSTRATION ===");
        
        // Show arrays
        System.out.println("\n1. String Array (VALID_AREA_CODES):");
        for (int i = 0; i < VALID_AREA_CODES.length; i++) {
            System.out.println("   [" + i + "] " + VALID_AREA_CODES[i]);
        }
        
        System.out.println("\n2. String Methods Demonstration:");
        String testPhone = "012-345-6789";
        System.out.println("   Test phone: " + testPhone);
        System.out.println("   - replaceAll(\"[\\\\s-]\", \"\"): " + testPhone.replaceAll("[\\s-]", ""));
        System.out.println("   - substring(0,3): " + testPhone.substring(0, 3));
        System.out.println("   - length(): " + testPhone.length());
        System.out.println("   - matches(\"\\\\d{10,11}\"): " + testPhone.replaceAll("[\\s-]", "").matches("\\d{10,11}"));
        
        System.out.println("\n3. Array Search Example:");
        String searchCode = "012";
        boolean found = false;
        for (String code : VALID_AREA_CODES) {
            if (code.equals(searchCode)) {
                found = true;
                break;
            }
        }
        System.out.println("   Searching for area code '" + searchCode + "': " + (found ? "✓ FOUND" : "✗ NOT FOUND"));
    }
}