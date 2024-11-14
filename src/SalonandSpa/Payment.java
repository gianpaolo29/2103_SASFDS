package SalonandSpa;

public class Payment {
    Appointment appointmentID;
    Customer customerId;
    Service serviceID;
    Staff staffID;
    Appointment date;
    Appointment time;
    double total;
    double staffTip;
    
    public Payment(Appointment appointmentID, Customer customerId, Service serviceID, Staff staffID, Appointment date, Appointment time, double total, double staffTip){
        this.appointmentID = appointmentID;
        this.customerId = customerId;
        this.serviceID = serviceID;
        this.staffID = staffID;
        this.date = date;
        this.time = time;
        this.total = total;
        this.staffTip = staffTip;
        
    }
    
    
    
}
