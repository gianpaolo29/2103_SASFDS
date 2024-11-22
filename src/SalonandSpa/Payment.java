package SalonandSpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
}
