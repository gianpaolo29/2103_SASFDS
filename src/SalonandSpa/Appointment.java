package SalonandSpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private int appointmentID;
    private Customer customer; 
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

    public Appointment(int appointmentID, Customer customer, LocalDate date, LocalTime startTime, LocalTime endTime, String status) {
        this.appointmentID = appointmentID;
        this.customer = customer;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Getters and setters

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    
    public void addAppointment(int appointmentID, Customer customer, LocalDate date, LocalTime startTime, LocalTime endTime, String status) {
        Connection conn = DatabaseConnector.connect();
        String insertQuery = "INSERT INTO appointment (AppointmentID, CustomerID, Date, StartTime, EndTime, Status) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, appointmentID);
            preparedStatement.setInt(2, customer.getCustomerID()); // Use customerID from Customer object
            preparedStatement.setDate(3, java.sql.Date.valueOf(date));
            preparedStatement.setTime(4, java.sql.Time.valueOf(startTime));
            preparedStatement.setTime(5, java.sql.Time.valueOf(endTime));
            preparedStatement.setString(6, status);

            // Execute the query
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Appointment added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
}
