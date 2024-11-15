package SalonandSpa;

import java.sql.Connection;
import java.sql.Statement;


public class Service {
   int serviceID;
   String serviceName;
   String description;
   double price;
   
   public Service(int serviceID, String serviceName, String description, double price){
       this.serviceID = serviceID;
       this.serviceName = serviceName;
       this.description = description;
       this.price = price;
   }
   
   public static void addService(String serviceName, String description, double price) {
        Connection conn = DatabaseConnector.connect();
        try {

            try (Statement statement = conn.createStatement()) {
                String insertQuery = "INSERT INTO service (ServiceName, Description, Price) VALUES ('" + serviceName + "', '" + description + "', '" + price + "')";
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
    
    

