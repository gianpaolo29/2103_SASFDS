package SalonandSpa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private final int serviceID;
    private final String serviceName;
    private final String description;
    private final double price;

    // Constructor
    public Service(int serviceID, String serviceName, String description, double price) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
    }

    // Getters
    public int getServiceID() {
        return serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public static List<Service> getAllService() {
        List<Service> services = new ArrayList<>();
        Connection conn = DatabaseConnector.connect();

        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT * FROM service WHERE Status = 'Active'";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int serviceID = resultSet.getInt("ServiceID");
                String name = resultSet.getString("ServiceName");
                String descrip = resultSet.getString("Description");
                double pricee = resultSet.getDouble("Price");

                Service service = new Service(serviceID, name, descrip, pricee);
                services.add(service);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }

        return services;
    }

    // Method to add a new service to the database
    public static void addService(String serviceName, String description, double price) {
        Connection conn = DatabaseConnector.connect();
        String insertQuery = "INSERT INTO service (ServiceName, Description, Price) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, serviceName);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, price);

            int rowsAffected = preparedStatement.executeUpdate();

            // Check the number of rows affected
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
        public static void deleteService(int serviceId) {
        Connection conn = DatabaseConnector.connect();
        String deleteQuery = "UPDATE service SET Status = 'Inactive' WHERE ServiceID = ?";
                    
        try (PreparedStatement statement = conn.prepareStatement(deleteQuery)) {
            statement.setInt(1, serviceId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    
       public static void updateService(int serviceID, String serviceName, String description, double price) {
        Connection conn = DatabaseConnector.connect();
        String updateQuery = "UPDATE service SET ServiceName = ?, Description  =  ?, Price = ? WHERE ServiceID = ?";
        try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setString(1, serviceName);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, serviceID);
            
            System.out.println(updateQuery);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
       
}
