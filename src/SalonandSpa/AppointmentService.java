package SalonandSpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AppointmentService {
   public Service service;
   public Staff staff;
   public UUID appointmentServiceId;
   public UUID appointmentId;
   public double amount;
   public double tip;
    
    public AppointmentService(UUID appointmentServiceId, UUID appointmentId, Service service, Staff staff, double amount, double  tip) {
        this.service = service;
        this.staff = staff;
        this.appointmentId = appointmentId;
        this.appointmentServiceId = appointmentServiceId;
        this.amount = amount;
        this.tip = tip;
    }
    
    public void save() {
        Connection conn = DatabaseConnector.connect();
        String insertQuery = "INSERT INTO appointmentservice (AppointmentServiceID, AppointmentID, ServiceID, StaffID, Amount) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, this.appointmentServiceId.toString());
            preparedStatement.setString(2, this.appointmentId.toString());
            preparedStatement.setInt(3, this.service.getServiceID());
            preparedStatement.setInt(4, this.staff.getStaffID());
            preparedStatement.setDouble(5, this.amount);

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
    
    public static List<AppointmentService> getAllAppointmentServices() {
        List<AppointmentService> items = new ArrayList<>();
        Connection conn = DatabaseConnector.connect();

        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT * FROM appointmentservice JOIN staff on staff.StaffID = appointmentservice.StaffID JOIN service on service.ServiceID = appointmentservice.ServiceID";
            ResultSet resultSet = statement.executeQuery(selectQuery);


            while (resultSet.next()) {
                int serviceId = resultSet.getInt("ServiceID");
                String serviceName = resultSet.getString("ServiceName");
                String serviceDescription = resultSet.getString("ServiceName");
                double servicePrice = resultSet.getDouble("Price");
                
                Service service = new Service(serviceId, serviceName, serviceDescription, servicePrice);
                
                int staffId = resultSet.getInt("StaffID");
                String staffName = resultSet.getString("StaffName");
                boolean isAvailable = resultSet.getBoolean("Availability");
                
                Staff staff = new Staff(staffId, staffName, service, isAvailable);
                
                UUID appointmentServiceId = UUID.fromString(resultSet.getString("AppointmentServiceID"));
                UUID appointmentId = UUID.fromString(resultSet.getString("AppointmentID"));
                double amount = resultSet.getDouble("Amount");
                double tip = resultSet.getDouble("Tip");
                
                System.out.println(tip);
                
                AppointmentService appointmentService = new AppointmentService(appointmentServiceId, appointmentId, service, staff, amount, tip);
                
                items.add(appointmentService);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }

        return items;
    }
    
    public void saveTip() {
        Connection conn = DatabaseConnector.connect();
        String updateQuery = "UPDATE appointmentservice SET Tip = ? WHERE AppointmentServiceID = ?";
        try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setDouble(1, this.tip);
            statement.setString(2, this.appointmentServiceId.toString());
            
            System.out.println(updateQuery);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    public static List<Object[]> getServiceByDate(String date) {
    List<Object[]> services = new ArrayList<>();

    Connection conn = DatabaseConnector.connect();

    String query = "SELECT service.ServiceName AS ServiceName, " +
                   "COUNT(appointmentservice.ServiceID) AS ServiceCount, " +
                   "SUM(appointmentservice.Amount) AS TotalSales " +
                   "FROM appointmentservice " +
                   "JOIN appointment ON appointment.AppointmentID = appointmentservice.AppointmentID " +
                   "JOIN service ON service.ServiceID = appointmentservice.ServiceID " +
                   "WHERE appointment.Date = ? AND appointment.Status = 'Paid' " +
                   "GROUP BY service.ServiceName";

    try (PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setString(1, date);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String serviceName = resultSet.getString("ServiceName");
            int serviceCount = resultSet.getInt("ServiceCount");
            double totalSales = resultSet.getDouble("TotalSales");


            services.add(new Object[]{serviceName, serviceCount, totalSales});
        }

        resultSet.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error while fetching services: " + e.getMessage());
    } finally {
        DatabaseConnector.closeConnection(conn);
    }

    return services;
}

}
    
   

