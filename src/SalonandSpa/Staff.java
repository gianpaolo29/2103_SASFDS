package SalonandSpa;


public class Staff {
    
    int staffID;
    String staffName;
    String service;
    boolean availability;
    
    public Staff(int serviceID,String staffName, String service, boolean availability){
        this.staffID = staffID;
        this.staffName = staffName;
        this.service = service;
        this.availability = true;
    }
    
    public void set_availability(boolean availability) {
        this.availability = availability;
    }
    
    public String get_availability(){
        if(availability){
            return "Available";
        }
        else{
            return "Not Available";
        }
    }
    
    
}



    

   