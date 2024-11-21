package SalonandSpa;


public class AppointmentService {
   public Service service;
   public Staff staff;
    
    public AppointmentService(Service service, Staff staff) {
        this.service = service;
        this.staff = staff;
    }
}
