package models;

public class Vehicle {
    private String vehicleId;
    private String vehicleType; // Van, Motorcycle, Truck
    private String plateNumber;
    private boolean isAvailable;
    private double capacity; // in kg
    private String currentDeliveryId;
    
    public Vehicle(String vehicleId, String vehicleType, 
                   String plateNumber, double capacity) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.plateNumber = plateNumber;
        this.capacity = capacity;
        this.isAvailable = true;
        this.currentDeliveryId = null;
    }
    
    // Check if vehicle can carry parcel
    public boolean canCarry(double parcelWeight) {
        return isAvailable && parcelWeight <= capacity;
    }
    
    // Assign vehicle (makes it unavailable)
    public void assignVehicle(String deliveryId) {  // MODIFIED
        if (isAvailable) {
            isAvailable = false;
            this.currentDeliveryId = deliveryId;  // Track which delivery
           // System.out.println("Vehicle " + vehicleId + " assigned to Delivery " + deliveryId);
        } else {
            System.out.println("Vehicle " + vehicleId + " is currently assigned to Delivery " + 
                             currentDeliveryId);
        }
    }
    
    // Release vehicle (makes it available again)
    public void releaseVehicle() {
        isAvailable = true;
        System.out.println("Vehicle " + vehicleId + " released from Delivery " + 
                         currentDeliveryId);
        this.currentDeliveryId = null;
    }
    
    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getVehicleType() { return vehicleType; }
    public String getPlateNumber() { return plateNumber; }
    public boolean isAvailable() { return isAvailable; }
    public double getCapacity() { return capacity; }
    public String getCurrentDeliveryId() {return currentDeliveryId; }
    
     public void displayVehicleInfo() {
        System.out.println("\n=== VEHICLE INFORMATION ===");
        System.out.println("Vehicle ID: " + vehicleId);
        System.out.println("Type: " + vehicleType);
        System.out.println("Plate: " + plateNumber);
        System.out.println("Capacity: " + capacity + " kg");
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
        if (!isAvailable && currentDeliveryId != null) {
            System.out.println("Currently on: Delivery " + currentDeliveryId);
        }
    }
}