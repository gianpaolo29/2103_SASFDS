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
    
    public AppointmentService(UUID appointmentServiceId, UUID appointmentId, Service service, Staff staff) {
        this.service = service;
        this.staff = staff;
        this.appointmentId = appointmentId;
        this.appointmentServiceId = appointmentServiceId;
    }
    
    public void save() {
        Connection conn = DatabaseConnector.connect();
        String insertQuery = "INSERT INTO appointmentservice (AppointmentServiceID, AppointmentID, ServiceID, StaffID) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, this.appointmentServiceId.toString());
            preparedStatement.setString(2, this.appointmentId.toString());
            preparedStatement.setInt(3, this.service.getServiceID());
            preparedStatement.setInt(4, this.staff.getStaffID());


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
                
                AppointmentService appointmentService = new AppointmentService(appointmentServiceId, appointmentId, service, staff);
                
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
}
