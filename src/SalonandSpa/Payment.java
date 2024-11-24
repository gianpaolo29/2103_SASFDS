package SalonandSpa;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Payment {
    public UUID paymentId;
    public Appointment appointment;
    public Date createdAt;

    
    public Payment(UUID paymentId, Appointment appointment, Date createdAt) {
        this.paymentId = paymentId;
        this.appointment = appointment;
        this.createdAt = createdAt;
    }
    
    public void save() {
        Connection conn = DatabaseConnector.connect();
        String insertQuery = "INSERT INTO payment (PaymentId, AppointmentId, CreatedAt) VALUES (?, ?, ?)";
       
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, this.paymentId.toString());
            preparedStatement.setString(2, this.appointment.appointmentID.toString());
            preparedStatement.setString(3, dateFormatter.format(this.createdAt));


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
    
    public static List<Object[]> getAllTransaction(){
        Connection conn = DatabaseConnector.connect();
        List<Object[]> services = new ArrayList<>();
        
        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT c.Name AS CustomerName, a.Date AS AppointmentDate, GROUP_CONCAT(s.ServiceName SEPARATOR ', ') AS Services, " + 
                "SUM(asv.Amount) AS TotalAmount FROM customer c JOIN appointment a ON c.CustomerID = a.CustomerID " +
                "JOIN appointmentservice asv ON a.AppointmentID = asv.AppointmentID JOIN service s ON asv.ServiceID = s.ServiceID WHERE a.Status = 'PAID' " +
                "GROUP BY c.CustomerID, c.Name, a.Date, a.AppointmentID ORDER BY a.Date, c.Name;";
            
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {

                String customerName = resultSet.getString("CustomerName");
                String appointmentDate = resultSet.getString("AppointmentDate");
                String service = resultSet.getString("Services");
                double amount = resultSet.getDouble("TotalAmount");
                
                
            services.add(new Object[]{customerName, appointmentDate, service, amount});
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
