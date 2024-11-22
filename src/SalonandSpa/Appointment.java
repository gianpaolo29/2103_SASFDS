package SalonandSpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Appointment {
    public UUID appointmentID;
    public Customer customer; 
    public Date date;
    public LocalTime startTime;
    public LocalTime endTime;
    public String status;
    public List<AppointmentService> items;

    public Appointment(UUID appointmentID, Customer customer, Date date, LocalTime startTime, LocalTime endTime, String status, List<AppointmentService> items) {
        this.appointmentID = appointmentID;
        this.customer = customer;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.items = items;
    }

    // Getters and setters

    public UUID getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(UUID appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
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

    
    public void save() {
        Connection conn = DatabaseConnector.connect();
        String insertQuery = "INSERT INTO appointment (AppointmentID, CustomerID, Date, StartTime, EndTime, Status) VALUES (?, ?, ?, ?, ?, ?)";
       
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        try (PreparedStatement preparedStatement = conn.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, this.appointmentID.toString());
            preparedStatement.setInt(2, this.customer.getCustomerID());
            preparedStatement.setString(3, dateFormatter.format(this.date));
            preparedStatement.setString(4, timeFormat.format(this.startTime));
            preparedStatement.setString(5, timeFormat.format(this.endTime));
            preparedStatement.setString(6, this.status);


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
    
    public String getServicesString() {
        ArrayList<String> list = new ArrayList<>();
        
        System.out.println(this.items.size());
        for (AppointmentService appointmentService : this.items) {
            list.add(appointmentService.service.getServiceName());
            System.out.println(appointmentService.service.getServiceName());
        }
        
        return String.join(",", list);
    }
    
    public double getTotalAmount() {
        double total = 0;
        
        for (AppointmentService appointmentService : this.items) {
            total = total + appointmentService.amount;
        }
        
        return total;
    }
    
    public double getTotalTip() {
        double total = 0;
        
        for (AppointmentService appointmentService : this.items) {
            total = total + appointmentService.tip;
        }
        
        return total;
    }
    
    public double getTotal () {
        return  this.getTotalAmount() + this.getTotalTip();
    }
    
    

    public static List<Appointment> getAllAppointments(String statusFilter) {
        List<Appointment> appointments = new ArrayList<>();
        List<AppointmentService> appointmentServices = AppointmentService.getAllAppointmentServices();
        
        Connection conn = DatabaseConnector.connect();

        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT * FROM `appointment` JOIN customer on customer.CustomerID = appointment.CustomerID";
            
            if (statusFilter != null) {
                selectQuery = selectQuery + " WHERE `appointment`.status = '" + statusFilter + "'"; 
            }
            
            System.err.println(selectQuery);

            ResultSet resultSet = statement.executeQuery(selectQuery);
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

            while (resultSet.next()) {
                int customerID = resultSet.getInt("CustomerID");
                String customerName = resultSet.getString("Name");
                String contactNo = resultSet.getString("ContactNo");

                Customer customer = new Customer(customerID, customerName, contactNo);
                
                UUID appointmendId = UUID.fromString(resultSet.getString("AppointmentID"));
                
                Date date  = dateFormatter.parse(resultSet.getString("Date"));
                LocalTime startTime = LocalTime.parse(resultSet.getString("StartTime"), timeFormat);
                LocalTime endTime = LocalTime.parse(resultSet.getString("EndTime"), timeFormat);
                String status = resultSet.getString("Status");
                
                List<AppointmentService> items = new ArrayList<>();
                
                for (AppointmentService appointmentService : appointmentServices) {
                    if (appointmentService.appointmentId.toString().equals(appointmendId.toString())) {
                        items.add(appointmentService);
                    }
                }
                
                Appointment appointment = new Appointment(appointmendId, customer, date, startTime, endTime, status, items);
                appointments.add(appointment);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }

        return appointments;
    }
    
    public void pay() {
        Connection conn = DatabaseConnector.connect();
        String updateQuery = "UPDATE appointment SET Status = ? WHERE AppointmentID = ?";
        try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
            statement.setString(1, "PAID");
            statement.setString(2, this.appointmentID.toString());
            
            System.out.println(updateQuery);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }
    }
    
}
