package SalonandSpa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static void addCustomer(String customerName, String contactNo) {
        Connection conn = DatabaseConnector.connect();
        String insertQuery = "INSERT INTO Customer (Name, ContactNo) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, contactNo);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insertion successful. Rows affected: " + rowsAffected);
            } else {
                System.out.println("Insertion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection conn = DatabaseConnector.connect();

        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT * FROM customer";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int customerID = resultSet.getInt("CustomerID");
                String customerName = resultSet.getString("Name");
                String contactNo = resultSet.getString("ContactNo");

                Customer customer = new Customer(customerID, customerName, contactNo);
                customers.add(customer);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }

        return customers;
    }
    
    public static void updateCustomer(int customerID, String customerName, String contactNo) {
        Connection conn = DatabaseConnector.connect();
        String updateQuery = "UPDATE customer SET Name = ?, ContactNo  =  ? WHERE CustomerID = ?";
        try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setString(1, customerName);
            statement.setString(2, contactNo);
            statement.setInt(3, customerID);
            
            System.out.println(updateQuery);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    

    public static void deleteCustomer(int customerId) {
        Connection conn = DatabaseConnector.connect();
        String deleteQuery = "DELETE FROM customer WHERE CustomerID = ?";
                    
        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    
    public static void getID(){

        Connection conn = DatabaseConnector.connect();

        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT CutomerID FROM customer WHERE CustomerID = ? ";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int customerID = resultSet.getInt("CustomerID");
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    
}


     
    
    

