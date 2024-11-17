package SalonandSpa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Staff {
    
    private int staffID;
    private String staffName;
    private String service;
    boolean availability;
    
    public Staff(int staffID,String staffName, String service, boolean availability){
        this.staffID = staffID;
        this.staffName = staffName;
        this.service = service;
        this.availability = true;
    }
    
    public int getStaffID() {
        return staffID;
    }

    
    public String getStaffName() {
        return staffName;
    }

    
    public String getService() {
        return service;
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
    
    
        public static List<Staff> getAllStaff() {
        List<Staff> staffs = new ArrayList<>();
        Connection conn = DatabaseConnector.connect();

        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT * FROM staff";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int staffID = resultSet.getInt("StaffID");
                String staffName = resultSet.getString("StaffName");
                String service = resultSet.getString("Service");
                boolean isAvailable = resultSet.getBoolean("Availability");
                
                

                Staff staff = new Staff(staffID, staffName, service, isAvailable);
                staffs.add(staff);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }

        return staffs;
    }
    
    public static void addStaff(String staffName, String service) {
        Connection conn = DatabaseConnector.connect();
        try {

            try (Statement statement = conn.createStatement()) {
                
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
        
        public static void updateStaff(int staffID, String staffName, String service) {
        Connection conn = DatabaseConnector.connect();
        String updateQuery = "UPDATE staff SET staffName = ?, service  =  ? WHERE staffID = ?";
        try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            
            statement.setString(1, staffName);
            statement.setString(2, service);
            statement.setInt(3, staffID);
            
            System.out.println(updateQuery);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
        public static void deleteStaff(int staffId) {
        Connection conn = DatabaseConnector.connect();
        String deleteQuery = "DELETE FROM staff WHERE staffID = ?";
                    
        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.setInt(1, staffId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    }

    
 


    

   