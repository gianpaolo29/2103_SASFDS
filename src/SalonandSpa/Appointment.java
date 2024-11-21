package SalonandSpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Appointment {
    private int appointmentID;
    private Customer customerID; 
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    public List<AppointmentService> items;

    public Appointment(int appointmentID, Customer customerID, LocalDate date, LocalTime startTime, LocalTime endTime, String status, List<AppointmentService> items) {
        this.appointmentID = appointmentID;
        this.customerID = customerID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.items = items;
    }

    // Getters and setters

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Customer getCustomerID() {
        return customerID;
    }

    

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    public static void addAppointment(Customer customer, LocalDate date, LocalTime startTime, LocalTime endTime, String status) {
    Connection conn = DatabaseConnector.connect();
    String insertQuery = "INSERT INTO appointment (CustomerID, Date, StartTime, EndTime, Status) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
        if (customer.getCustomerID() <= 0) {
            System.out.println("Invalid customerID.");
            return;
        }

        preparedStatement.setInt(1, customer.getCustomerID());
        preparedStatement.setDate(2, java.sql.Date.valueOf(date)); // Convert LocalDate to SQL Date
        preparedStatement.setTime(3, java.sql.Time.valueOf(startTime)); // Convert LocalTime to SQL Time
        preparedStatement.setTime(4, java.sql.Time.valueOf(endTime)); // Convert LocalTime to SQL Time
        preparedStatement.setString(5, status);

        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Appointment added successfully!");
        } else {
            System.out.println("Failed to add appointment.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        DatabaseConnector.closeConnection(conn);
    }
}

}
