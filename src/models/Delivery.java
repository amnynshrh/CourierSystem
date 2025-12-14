package models;

import java.time.LocalDate;

public class Delivery {
    private String deliveryId;
    private Parcel parcel;
    private Staff deliveryPerson;
    private Vehicle assignedVehicle; 
    private String status;
    private String route;
    private LocalDate estimatedTime;
    
    public Delivery(String deliveryId, Parcel parcel, Staff deliveryPerson) {
        this.deliveryId = deliveryId;
        this.parcel = parcel;
        this.deliveryPerson = deliveryPerson;
        this.assignedVehicle = null; 
        this.status = "Scheduled";
        this.route = "To be assigned";
        this.estimatedTime = LocalDate.now().plusDays(2);
    }
    
    // Update delivery status
    public void updateDeliveryStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Delivery " + deliveryId + " status: " + newStatus);
    }
    
    // Assign route
    public void assignRoute(String route) {
        this.route = route;
        System.out.println("Route assigned: " + route);
    }
    
    // Getters
    public String getDeliveryId() { return deliveryId; }
    public Parcel getParcel() { return parcel; }
    public Staff getDeliveryPerson() { return deliveryPerson; }
    public Vehicle getAssignedVehicle() { return assignedVehicle; }
    public String getStatus() { return status; }
    public String getRoute() { return route; }
    public LocalDate getEstimatedTime() { return estimatedTime; }
    
    public void setDeliveryPerson(Staff staff) {
        this.deliveryPerson = staff;
    }
    
     public void setAssignedVehicle(Vehicle vehicle) {
        this.assignedVehicle = vehicle;
       // if (vehicle != null) {
       //     System.out.println("Vehicle " + vehicle.getVehicleId() + " assigned to this delivery.");
       // }
    }
    
    public void displayDeliveryInfo() {
        System.out.println("\n=== DELIVERY INFORMATION ===");
        System.out.println("Delivery ID: " + deliveryId);
        System.out.println("Parcel ID: " + parcel.getParcelId());
        if (deliveryPerson != null) {
            System.out.println("Delivery Person: " + deliveryPerson.getName());
        }
        if (assignedVehicle != null) {
            System.out.println("Assigned Vehicle: " + assignedVehicle.getVehicleId() + 
                             " (" + assignedVehicle.getVehicleType() + ")");
        }
        System.out.println("Status: " + status);
        System.out.println("Route: " + route);
        System.out.println("Estimated Delivery: " + estimatedTime);
    }

}