/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SalonandSpa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Receptionist {

    
    private String username;
    String password; 
    private String name;
    private String address;
    
    public Receptionist(String username, String password, String name, String address){
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    
    public static List<Receptionist> getAllReceptionist() {
        List<Receptionist> receptionists = new ArrayList<>();
        Connection conn = DatabaseConnector.connect();

        try {
            Statement statement = conn.createStatement();
            String selectQuery = "SELECT * FROM receptionist";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                
                Receptionist receptionist = new Receptionist(username, password, name, address);
                receptionists.add(receptionist);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnector.closeConnection(conn);
        }

        return receptionists;
    }
    
    
}
