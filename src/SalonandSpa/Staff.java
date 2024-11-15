package SalonandSpa;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class Staff {
    
    private int staffID;
    private String staffName;
    private String service;
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
    
    public static void addService(String staffName, String service) {
        Connection conn = DatabaseConnector.connect();
        try {

            // Execute an INSERT query
            try (Statement statement = conn.createStatement()) {
                // Execute an INSERT query
                   // "INSERT INTO person (`fname`, `lname`, `email`, `address`, `contact`) VALUES ('"+fname+"', '"+lname+"', '"+email+"', '"+address+"', '"+contact+"')"
                String insertQuery = "INSERT INTO staff (StaffName, Service) VALUES ('" + staffName + "', '" + service + "')";
                System.out.println(insertQuery);

                int rowsAffected = statement.executeUpdate(insertQuery);
                // Check the number of rows affected
                if (rowsAffected > 0) {
                    System.out.println("Insertion successful" + rowsAffected);
                } else {
                    System.out.println("Insertion failed.");
                }
                // Close the statement
            }
        } catch (Exception e) {
            System.out.print(e);
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }

    
}



    

   