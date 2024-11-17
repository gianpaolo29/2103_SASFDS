package SalonandSpa;

public class Customer {

   
    private int customerID;
    private String customerName;
    private String contactNo;

    public Customer() {
    }

    public Customer(int customerID, String customerName, String contactNo) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.contactNo = contactNo;
    }

    // Getters and Setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    
    
}

     
    
    

