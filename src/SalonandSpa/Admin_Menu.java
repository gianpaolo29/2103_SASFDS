package SalonandSpa;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.swing.DefaultListModel;
import javax.swing.plaf.OptionPaneUI;

public class Admin_Menu extends javax.swing.JFrame {
    List<Customer> customers;
    List<Service> services;
    List<Staff> staffs;
    List<Appointment> appointments;
    List<AppointmentService> newAppointmentServices;
    AppointmentService newAppointmentService;
    Appointment selectedAppointment;
    
    List<Service> appointmentServiceOptions;
    List<Staff> appointmentStaffOptions;

    public Admin_Menu() {
        initComponents();
    }

    Connection conn = DatabaseConnector.connect();
    
    public void selectService(){
        Connection conn = DatabaseConnector.connect();
        Set<String> serviceSet = new HashSet<>();
        
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT ServiceName FROM service");
            
            ServiceComboBox.removeAllItems();
            while (rs.next()) {
                String serviceName = rs.getString("ServiceName");
                if (serviceSet.add(serviceName)) {
                ServiceComboBox.addItem(serviceName);
                }    
            }
        } catch (Exception e) {
            
        }
        
    }
    
public void totalSales(String date) {
    // Establish a connection to the database
    Connection conn = DatabaseConnector.connect();

    String query = "SELECT SUM(appointmentservice.Amount) AS TotalAmount " +
                   "FROM appointmentservice " +
                   "JOIN appointment ON appointment.AppointmentID = appointmentservice.AppointmentID " +
                   "WHERE appointment.Date = ? AND appointment.Status = 'Paid'";

    try (PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setString(1, date);

        ResultSet resultSet = statement.executeQuery();

        // Process the result
        if (resultSet.next()) {
            double totalAmount = resultSet.getDouble("TotalAmount");
            
            TotalSales.setText(Double.toString(totalAmount));
        } else {
            System.out.println("No sales data found for the date: " + date);
        }

        // Close the ResultSet
        resultSet.close();
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error while fetching total sales: " + e.getMessage());
    } finally {
        // Close the database connection
        DatabaseConnector.closeConnection(conn);
    }
}

    
    private void clearService(){
        serviceName_txt.setText("");
        serviceDescription_txt.setText("");
        servicePrice_txt.setText("");
    }
    
    private void clearStaff(){
        StaffName_txt.setText("");
    }

    
    private void clearCustomer(){
        Cname_txt.setText("");
        Ccno_txt.setText("");
    }
    private void clearAppointment(){
        newAppointmentServicesTable.removeAll();
    }
    
    
private void refreshAppointment() {
    DefaultTableModel appointmentTableModel = (DefaultTableModel) AppointmentJtable.getModel();

    this.appointments = Appointment.getAllAppointments(null);
    appointmentTableModel.setRowCount(0);

    for (Appointment appointment : this.appointments) {
        // Add the data row to the table
        Object[] rowData = {
            appointment.getCustomer().getCustomerName(),
            appointment.getDate().toString(),
            appointment.getStartTime().toString(),
            appointment.getEndTime().toString(),
            appointment.getStatus(),
            appointment.getServicesString()
        };
        
        appointmentTableModel.addRow(rowData);
    }
}

private void refreshAppointmentsToPay() {
    List<Appointment> appointments = Appointment.getAllAppointments("PENDING");

    paymentAppointmentCombobox.removeAllItems();
    for (Appointment appointment : appointments) {
        paymentAppointmentCombobox.addItem(appointment.appointmentID.toString());
    }
}



    private void refreshStaff() {
    DefaultTableModel staffTableModel = (DefaultTableModel) StaffsJtable.getModel();

    this.staffs = Staff.getAllStaff();
    staffTableModel.setRowCount(0);

    for (Staff staff : this.staffs) {
        String availability = staff.availability ? "Available" : "Not Available";
        Object[] rowData = {staff.getStaffID(),staff.getStaffName(), staff.getService().getServiceName(), availability};
        staffTableModel.addRow(rowData);
    }
    }
    
    private void refreshService() {
        DefaultTableModel serviceTableModel = (DefaultTableModel) ServiceJtable.getModel();

        this.services = Service.getAllService();
        serviceTableModel.setRowCount(0);
        ServiceComboBox.removeAllItems();


        for (Service service : this.services) {
            Object[] rowData = {service.getServiceID(),service.getServiceName(), service.getDescription(), service.getPrice()};
            serviceTableModel.addRow(rowData);
            ServiceComboBox.addItem(service.getServiceName());
        }
    }
    
    public void refreshDashboard(String date) {
    // Fetch the data for the given date
    List<Object[]> services = AppointmentService.getServiceByDate(date);

    // Define column headers
    String[] columnNames = {"Service Name", "Service Count", "Total Sales"};

    // Create a table model with the column names
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

    // Add each row from the service list to the model
    for (Object[] service : services) {
        model.addRow(service);
    }

    // Set the model to the JTable
    DashboardTable.setModel(model);
}

    
    private void refreshAppointmentServiceOptions () {
        this.appointmentServiceOptions = new ArrayList<>();
        this.appointmentStaffOptions = new ArrayList<>();
        
        DefaultListModel<String> staffListModel = new DefaultListModel<String>();
        staffListModel.clear();
        newAppointmentStaffList.setModel(staffListModel);
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        listModel.clear();

        newAppointmentServicesList.setModel(listModel);
        
        for (Service service : this.services) {
            boolean found = false;
            
            for (AppointmentService appointmentService: this.newAppointmentServices) {
                if (appointmentService.service.getServiceID() == service.getServiceID()) {
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                this.appointmentServiceOptions.add(service);
                listModel.addElement(service.getServiceName());
            }
        }
    }
    

    private void refreshCustomer() {
        DefaultTableModel customerTableModel = (DefaultTableModel) CustomerJTable.getModel();

        this.customers = Customer.getAllCustomers();
        customerTableModel.setRowCount(0);

        newAppointmentCustomerCmbobx.removeAllItems();

        for (Customer customer : this.customers) {
            Object[] rowData = {customer.getCustomerID(), customer.getCustomerName(), customer.getContactNo()};
            customerTableModel.addRow(rowData);
            newAppointmentCustomerCmbobx.addItem(customer.getCustomerName());
        }
    }
    
    
    private void getReceptionist() {
    DefaultTableModel receptionistTableModel = (DefaultTableModel) ReceptionistJtable.getModel();

    List<Receptionist> receptionist = Receptionist.getAllReceptionist();
    receptionistTableModel.setRowCount(0);

    for (Receptionist receptionists : receptionist) {
        Object[] rowData = {receptionists.getUsername(), receptionists.getName(), receptionists.getAddress()};
        receptionistTableModel.addRow(rowData);
    }
}
    public void logout(){
        int logout = JOptionPane.showConfirmDialog(null, "Do you want to Logout", "Confirmation", JOptionPane.YES_NO_OPTION);
        
       if (logout == JOptionPane.YES_OPTION){
       Login_Admin Login_AdminFrame = new Login_Admin();
       Login_AdminFrame.setVisible(true);
       Login_AdminFrame.pack();
       Login_AdminFrame.setLocationRelativeTo(null);
       this.dispose();
       }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel30 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        newAppointmentNavBtn = new javax.swing.JButton();
        staffNavBtn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        DashboardPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        TotalSales = new javax.swing.JLabel();
        DashboardDateChooser = new com.toedter.calendar.JDateChooser();
        jScrollPane13 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        DashboardTable = new javax.swing.JTable();
        jButton34 = new javax.swing.JButton();
        Dbcheck = new javax.swing.JButton();
        ReceptionistPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ReceptionistJtable = new javax.swing.JTable();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        AppointmentsPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        AppointmentJtable = new javax.swing.JTable();
        CreateAppoinmentPanel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        newAppointmentFormDate = new com.toedter.calendar.JDateChooser();
        newAppointmentFormEndTime = new javax.swing.JComboBox<>();
        newAppointmentFormStartTime = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        newAppointmentServicesTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        newAppointmentTotalLabel = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        newAppointmentCustomerCmbobx = new javax.swing.JComboBox<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        newAppointmentServicesList = new javax.swing.JList<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        newAppointmentStaffList = new javax.swing.JList<>();
        StaffPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        StaffName_txt = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        ServiceComboBox = new javax.swing.JComboBox<>();
        jButton22 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        StaffsJtable = new javax.swing.JTable();
        ServicePane = new javax.swing.JPanel();
        gshe = new javax.swing.JScrollPane();
        ServiceJtable = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        serviceName_txt = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        servicePrice_txt = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        serviceDescription_txt = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        CustomersPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        CustomerJTable = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        Cname_txt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        Ccno_txt = new javax.swing.JTextField();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        PaymentDetailsPanel = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jScrollPane15 = new javax.swing.JScrollPane();
        paymentServicesTable = new javax.swing.JTable();
        paymentAppointmentCombobox = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        paymentAddTipInput = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        paymentAddTipButton = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        paymentTotalLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        PaymentCsName = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        PaymentDate = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        PaymentStTime = new javax.swing.JLabel();
        PaymentEndTime = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();

        jLabel30.setText("Service");

        jLabel15.setText("jLabel15");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("Dashboard");
        jButton1.setBorder(null);
        jButton1.setPreferredSize(new java.awt.Dimension(7, 28));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("Receptionists");
        jButton2.setBorder(null);
        jButton2.setPreferredSize(new java.awt.Dimension(7, 28));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton4.setText("Appointments");
        jButton4.setBorder(null);
        jButton4.setPreferredSize(new java.awt.Dimension(7, 28));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        newAppointmentNavBtn.setBackground(new java.awt.Color(255, 255, 255));
        newAppointmentNavBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        newAppointmentNavBtn.setText("New Appointments");
        newAppointmentNavBtn.setBorder(null);
        newAppointmentNavBtn.setPreferredSize(new java.awt.Dimension(7, 28));
        newAppointmentNavBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAppointmentNavBtnActionPerformed(evt);
            }
        });

        staffNavBtn.setBackground(new java.awt.Color(255, 255, 255));
        staffNavBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        staffNavBtn.setText("Staffs");
        staffNavBtn.setBorder(null);
        staffNavBtn.setPreferredSize(new java.awt.Dimension(7, 28));
        staffNavBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffNavBtnActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton7.setText("Services");
        jButton7.setBorder(null);
        jButton7.setPreferredSize(new java.awt.Dimension(7, 28));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton8.setText("Customers");
        jButton8.setBorder(null);
        jButton8.setPreferredSize(new java.awt.Dimension(7, 28));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(255, 255, 255));
        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton10.setText("Payments");
        jButton10.setBorder(null);
        jButton10.setPreferredSize(new java.awt.Dimension(7, 28));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newAppointmentNavBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(staffNavBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newAppointmentNavBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(staffNavBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 34)); // NOI18N
        jLabel1.setText("Dashboard");

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 1, 16)); // NOI18N
        jLabel8.setText("TOTAL SALES:");

        TotalSales.setFont(new java.awt.Font("Segoe UI Emoji", 1, 48)); // NOI18N
        TotalSales.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalSales.setText("00.00");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(TotalSales, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalSales, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        DashboardDateChooser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DashboardDateChooserMouseClicked(evt);
            }
        });

        DashboardTable.setBackground(new java.awt.Color(204, 204, 204));
        DashboardTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Service", "Count", "Sales"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(DashboardTable);
        if (DashboardTable.getColumnModel().getColumnCount() > 0) {
            DashboardTable.getColumnModel().getColumn(0).setResizable(false);
            DashboardTable.getColumnModel().getColumn(1).setResizable(false);
            DashboardTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jScrollPane13.setViewportView(jScrollPane1);

        jButton34.setBackground(new java.awt.Color(0, 0, 0));
        jButton34.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton34.setForeground(new java.awt.Color(255, 255, 255));
        jButton34.setText("LOG OUT");
        jButton34.setBorder(null);
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        Dbcheck.setBackground(new java.awt.Color(0, 153, 0));
        Dbcheck.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        Dbcheck.setForeground(new java.awt.Color(255, 255, 255));
        Dbcheck.setText("✓");
        Dbcheck.setBorder(null);
        Dbcheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DbcheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DashboardPanelLayout = new javax.swing.GroupLayout(DashboardPanel);
        DashboardPanel.setLayout(DashboardPanelLayout);
        DashboardPanelLayout.setHorizontalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                                .addComponent(DashboardDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Dbcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))))
                .addContainerGap())
        );
        DashboardPanelLayout.setVerticalGroup(
            DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardPanelLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                        .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(DashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DashboardDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Dbcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DashboardPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab1", DashboardPanel);

        jLabel9.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel9.setText("Receptionist");

        ReceptionistJtable.setBackground(new java.awt.Color(204, 204, 204));
        ReceptionistJtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Usename", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(ReceptionistJtable);
        if (ReceptionistJtable.getColumnModel().getColumnCount() > 0) {
            ReceptionistJtable.getColumnModel().getColumn(1).setResizable(false);
        }

        jButton36.setBackground(new java.awt.Color(0, 0, 0));
        jButton36.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton36.setForeground(new java.awt.Color(255, 255, 255));
        jButton36.setText("LOG OUT");
        jButton36.setBorder(null);

        jButton37.setBackground(new java.awt.Color(0, 0, 0));
        jButton37.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton37.setForeground(new java.awt.Color(255, 255, 255));
        jButton37.setText("REFRESH");
        jButton37.setBorder(null);

        javax.swing.GroupLayout ReceptionistPanelLayout = new javax.swing.GroupLayout(ReceptionistPanel);
        ReceptionistPanel.setLayout(ReceptionistPanelLayout);
        ReceptionistPanelLayout.setHorizontalGroup(
            ReceptionistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReceptionistPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReceptionistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
                    .addGroup(ReceptionistPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        ReceptionistPanelLayout.setVerticalGroup(
            ReceptionistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReceptionistPanelLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(ReceptionistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReceptionistPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(370, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab2", ReceptionistPanel);

        jLabel10.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel10.setText("Appointments");

        AppointmentJtable.setBackground(new java.awt.Color(204, 204, 204));
        AppointmentJtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer", "Date", "Start Time", "End Time", "Status", "Service"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(AppointmentJtable);
        if (AppointmentJtable.getColumnModel().getColumnCount() > 0) {
            AppointmentJtable.getColumnModel().getColumn(0).setResizable(false);
            AppointmentJtable.getColumnModel().getColumn(1).setResizable(false);
            AppointmentJtable.getColumnModel().getColumn(2).setResizable(false);
            AppointmentJtable.getColumnModel().getColumn(3).setResizable(false);
            AppointmentJtable.getColumnModel().getColumn(4).setResizable(false);
            AppointmentJtable.getColumnModel().getColumn(5).setResizable(false);
        }

        jScrollPane4.setViewportView(jScrollPane3);

        javax.swing.GroupLayout AppointmentsPanelLayout = new javax.swing.GroupLayout(AppointmentsPanel);
        AppointmentsPanel.setLayout(AppointmentsPanelLayout);
        AppointmentsPanelLayout.setHorizontalGroup(
            AppointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppointmentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AppointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AppointmentsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 421, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        AppointmentsPanelLayout.setVerticalGroup(
            AppointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AppointmentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", AppointmentsPanel);

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel11.setText("Create Appointment");

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Appointment Details");

        jLabel4.setText("Date");

        jLabel12.setText("Customer");

        newAppointmentFormEndTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM" }));
        newAppointmentFormEndTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAppointmentFormEndTimeActionPerformed(evt);
            }
        });

        newAppointmentFormStartTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM" }));
        newAppointmentFormStartTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAppointmentFormStartTimeActionPerformed(evt);
            }
        });

        jLabel13.setText("Start time");

        jLabel14.setText("End time");

        newAppointmentServicesTable.setBackground(new java.awt.Color(204, 204, 204));
        newAppointmentServicesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Service", "Staff", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(newAppointmentServicesTable);
        if (newAppointmentServicesTable.getColumnModel().getColumnCount() > 0) {
            newAppointmentServicesTable.getColumnModel().getColumn(0).setResizable(false);
            newAppointmentServicesTable.getColumnModel().getColumn(1).setResizable(false);
            newAppointmentServicesTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jScrollPane10.setViewportView(jScrollPane9);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Total: ");

        newAppointmentTotalLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        newAppointmentTotalLabel.setText("0");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newAppointmentTotalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(newAppointmentTotalLabel)))
        );

        jButton15.setText("Clear");
        jButton15.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.lightGray));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Submit");
        jButton16.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.black, java.awt.Color.lightGray));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel29.setText("Staffs");

        jLabel32.setText("Services");

        newAppointmentCustomerCmbobx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newAppointmentCustomerCmbobxActionPerformed(evt);
            }
        });

        newAppointmentServicesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newAppointmentServicesListMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(newAppointmentServicesList);

        newAppointmentStaffList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newAppointmentStaffListMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(newAppointmentStaffList);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newAppointmentFormDate, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(newAppointmentCustomerCmbobx, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(301, 301, 301))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(newAppointmentFormStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(newAppointmentFormEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(253, 253, 253))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel12))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newAppointmentCustomerCmbobx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newAppointmentFormDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newAppointmentFormStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(newAppointmentFormEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addComponent(jScrollPane11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jButton16))
                .addGap(22, 22, 22))
        );

        jScrollPane5.setViewportView(jPanel11);

        javax.swing.GroupLayout CreateAppoinmentPanelLayout = new javax.swing.GroupLayout(CreateAppoinmentPanel);
        CreateAppoinmentPanel.setLayout(CreateAppoinmentPanelLayout);
        CreateAppoinmentPanelLayout.setHorizontalGroup(
            CreateAppoinmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreateAppoinmentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CreateAppoinmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CreateAppoinmentPanelLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
                .addContainerGap())
        );
        CreateAppoinmentPanelLayout.setVerticalGroup(
            CreateAppoinmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreateAppoinmentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab4", CreateAppoinmentPanel);

        jLabel16.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel16.setText("Staffs");

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Create New Staff");

        jLabel18.setText("Service");

        StaffName_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StaffName_txtActionPerformed(evt);
            }
        });

        jLabel19.setText("Name");

        jButton3.setText("Clear");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton9.setText("Add");
        jButton9.setBorder(null);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        ServiceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServiceComboBoxActionPerformed(evt);
            }
        });

        jButton22.setText("Delete");
        jButton22.setBorder(null);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton25.setText("Update");
        jButton25.setBorder(null);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 294, Short.MAX_VALUE)
                        .addComponent(jLabel18)
                        .addGap(214, 214, 214))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(StaffName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(74, 74, 74)
                                .addComponent(ServiceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(24, 24, 24))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(StaffName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ServiceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton26.setBackground(new java.awt.Color(0, 0, 0));
        jButton26.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton26.setForeground(new java.awt.Color(255, 255, 255));
        jButton26.setText("REFRESH");
        jButton26.setBorder(null);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setBackground(new java.awt.Color(0, 0, 0));
        jButton27.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton27.setForeground(new java.awt.Color(255, 255, 255));
        jButton27.setText("LOG OUT");
        jButton27.setBorder(null);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        StaffsJtable.setBackground(new java.awt.Color(204, 204, 204));
        StaffsJtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "StaffID", "Name", "Service", "Availability"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        StaffsJtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                StaffsJtableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(StaffsJtable);
        if (StaffsJtable.getColumnModel().getColumnCount() > 0) {
            StaffsJtable.getColumnModel().getColumn(0).setMaxWidth(80);
            StaffsJtable.getColumnModel().getColumn(2).setResizable(false);
            StaffsJtable.getColumnModel().getColumn(3).setResizable(false);
        }

        jScrollPane14.setViewportView(jScrollPane6);

        javax.swing.GroupLayout StaffPanelLayout = new javax.swing.GroupLayout(StaffPanel);
        StaffPanel.setLayout(StaffPanelLayout);
        StaffPanelLayout.setHorizontalGroup(
            StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StaffPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane14)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, StaffPanelLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        StaffPanelLayout.setVerticalGroup(
            StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StaffPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StaffPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab5", StaffPanel);

        gshe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gsheMouseClicked(evt);
            }
        });

        ServiceJtable.setBackground(new java.awt.Color(204, 204, 204));
        ServiceJtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ServiceID", "Name", "Description", "Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ServiceJtable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ServiceJtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ServiceJtableMouseClicked(evt);
            }
        });
        gshe.setViewportView(ServiceJtable);
        if (ServiceJtable.getColumnModel().getColumnCount() > 0) {
            ServiceJtable.getColumnModel().getColumn(0).setMaxWidth(80);
            ServiceJtable.getColumnModel().getColumn(1).setResizable(false);
            ServiceJtable.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel24.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel24.setText("Services");

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Create New Services");

        jLabel26.setText("Price");

        serviceName_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceName_txtActionPerformed(evt);
            }
        });

        jLabel27.setText("Name");

        servicePrice_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servicePrice_txtActionPerformed(evt);
            }
        });

        jButton13.setText("Add");
        jButton13.setBorder(null);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        serviceDescription_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceDescription_txtActionPerformed(evt);
            }
        });

        jLabel28.setText("Description");

        jButton21.setText("Clear");
        jButton21.setBorder(null);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton14.setText("Update");
        jButton14.setBorder(null);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton20.setText("Delete");
        jButton20.setBorder(null);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(serviceDescription_txt)
                        .addContainerGap())
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(serviceName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(servicePrice_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(servicePrice_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serviceName_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceDescription_txt, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton23.setBackground(new java.awt.Color(0, 0, 0));
        jButton23.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton23.setForeground(new java.awt.Color(255, 255, 255));
        jButton23.setText("LOG OUT");
        jButton23.setBorder(null);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setBackground(new java.awt.Color(0, 0, 0));
        jButton24.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton24.setForeground(new java.awt.Color(255, 255, 255));
        jButton24.setText("REFRESH");
        jButton24.setBorder(null);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ServicePaneLayout = new javax.swing.GroupLayout(ServicePane);
        ServicePane.setLayout(ServicePaneLayout);
        ServicePaneLayout.setHorizontalGroup(
            ServicePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ServicePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ServicePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ServicePaneLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(gshe))
                .addContainerGap())
        );
        ServicePaneLayout.setVerticalGroup(
            ServicePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ServicePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ServicePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ServicePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gshe, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab6", ServicePane);

        CustomerJTable.setBackground(new java.awt.Color(204, 204, 204));
        CustomerJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Contact Service"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CustomerJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CustomerJTableMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(CustomerJTable);
        if (CustomerJTable.getColumnModel().getColumnCount() > 0) {
            CustomerJTable.getColumnModel().getColumn(0).setMaxWidth(80);
            CustomerJTable.getColumnModel().getColumn(1).setResizable(false);
            CustomerJTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jLabel20.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel20.setText("Customers");

        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Create New Customer");

        jLabel22.setText("Contact Number");

        Cname_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cname_txtActionPerformed(evt);
            }
        });

        jLabel23.setText("Name");

        Ccno_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ccno_txtActionPerformed(evt);
            }
        });

        jButton28.setText("Clear");
        jButton28.setBorder(null);
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setText("Delete");
        jButton29.setBorder(null);
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton30.setText("Update");
        jButton30.setBorder(null);
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton31.setText("Add");
        jButton31.setBorder(null);
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Cname_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(Ccno_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cname_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ccno_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton32.setBackground(new java.awt.Color(0, 0, 0));
        jButton32.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton32.setForeground(new java.awt.Color(255, 255, 255));
        jButton32.setText("REFRESH");
        jButton32.setBorder(null);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setBackground(new java.awt.Color(0, 0, 0));
        jButton33.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton33.setForeground(new java.awt.Color(255, 255, 255));
        jButton33.setText("LOG OUT");
        jButton33.setBorder(null);
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CustomersPanelLayout = new javax.swing.GroupLayout(CustomersPanel);
        CustomersPanel.setLayout(CustomersPanelLayout);
        CustomersPanelLayout.setHorizontalGroup(
            CustomersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CustomersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CustomersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, CustomersPanelLayout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)))
                .addContainerGap())
        );
        CustomersPanelLayout.setVerticalGroup(
            CustomersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CustomersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CustomersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab7", CustomersPanel);

        jLabel33.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel33.setText("Payment Details");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        paymentServicesTable.setBackground(new java.awt.Color(204, 204, 204));
        paymentServicesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Service", "Staff", "Price", "Tip"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymentServicesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymentServicesTableMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(paymentServicesTable);
        if (paymentServicesTable.getColumnModel().getColumnCount() > 0) {
            paymentServicesTable.getColumnModel().getColumn(0).setResizable(false);
            paymentServicesTable.getColumnModel().getColumn(1).setResizable(false);
            paymentServicesTable.getColumnModel().getColumn(2).setResizable(false);
            paymentServicesTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jScrollPane12.setViewportView(jScrollPane15);

        paymentAppointmentCombobox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                paymentAppointmentComboboxItemStateChanged(evt);
            }
        });
        paymentAppointmentCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentAppointmentComboboxActionPerformed(evt);
            }
        });

        jLabel31.setText("Services");

        jLabel35.setText("Appointment");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Appointment Payment");

        paymentAddTipInput.setEnabled(false);
        paymentAddTipInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paymentAddTipInputKeyReleased(evt);
            }
        });

        jButton5.setText("PAY");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        paymentAddTipButton.setText("ADD TIP");
        paymentAddTipButton.setEnabled(false);
        paymentAddTipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentAddTipButtonActionPerformed(evt);
            }
        });

        jLabel37.setText("TOTAL:");

        paymentTotalLabel.setText("XXXXXX");

        jLabel6.setText("Name:");

        PaymentCsName.setText("XXXXXXXXX");

        jLabel34.setText("Date:");

        PaymentDate.setText("XX");

        jLabel38.setText("Start Time:");

        PaymentStTime.setText("XX");

        PaymentEndTime.setText("XX");

        jLabel41.setText("End Time:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(paymentAddTipButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(paymentAddTipInput, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                    .addComponent(paymentTotalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(21, 21, 21))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(PaymentCsName, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(55, 55, 55)
                                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel41)
                                                    .addComponent(jLabel38)))
                                            .addComponent(paymentAppointmentCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(PaymentEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                                .addComponent(PaymentStTime, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(33, 33, 33))))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel35)
                                        .addGap(281, 281, 281)
                                        .addComponent(jLabel34)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(PaymentDate, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 12, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(12, 12, 12))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(PaymentDate)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentAppointmentCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38)
                    .addComponent(PaymentStTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(PaymentCsName)
                    .addComponent(PaymentEndTime)
                    .addComponent(jLabel41))
                .addGap(18, 18, 18)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentAddTipInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentAddTipButton)
                    .addComponent(jLabel37)
                    .addComponent(paymentTotalLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout PaymentDetailsPanelLayout = new javax.swing.GroupLayout(PaymentDetailsPanel);
        PaymentDetailsPanel.setLayout(PaymentDetailsPanelLayout);
        PaymentDetailsPanelLayout.setHorizontalGroup(
            PaymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaymentDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PaymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PaymentDetailsPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PaymentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PaymentDetailsPanelLayout.setVerticalGroup(
            PaymentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaymentDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab4", PaymentDetailsPanel);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, -30, 600, 530));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        getReceptionist();
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        refreshAppointment();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void newAppointmentNavBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAppointmentNavBtnActionPerformed
        jTabbedPane1.setSelectedIndex(3);
        refreshCustomer();
        refreshStaff();
        refreshService();
        this.newAppointmentServices = new ArrayList<>();
        refreshAppointmentServiceOptions();
    }//GEN-LAST:event_newAppointmentNavBtnActionPerformed

    private void staffNavBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffNavBtnActionPerformed
        jTabbedPane1.setSelectedIndex(4);
        refreshService();
        refreshStaff();
    }//GEN-LAST:event_staffNavBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        refreshService();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jTabbedPane1.setSelectedIndex(6);
        refreshCustomer();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void StaffName_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StaffName_txtActionPerformed

    }//GEN-LAST:event_StaffName_txtActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    clearStaff();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        String staffName = StaffName_txt.getText();      

        if (staffName.isEmpty() || ServiceComboBox.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(null, "Please complete the form.");
        } else {
            Service selectedService = this.services.get(ServiceComboBox.getSelectedIndex());
            Staff.addStaff(staffName, selectedService.getServiceID());
                
        }


        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void Cname_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cname_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Cname_txtActionPerformed

    private void Ccno_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ccno_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Ccno_txtActionPerformed

    private void serviceName_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceName_txtActionPerformed
        
        
        
        
    }//GEN-LAST:event_serviceName_txtActionPerformed

    private void servicePrice_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servicePrice_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_servicePrice_txtActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        String serviceName = serviceName_txt.getText();
        String servicePrice = servicePrice_txt.getText();
        String serviceDescription = serviceDescription_txt.getText();
        
        if (serviceName.isEmpty() || servicePrice.isEmpty() || serviceDescription.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please complete the form.");
        } else {
            double price = Double.parseDouble(servicePrice);
            Service.addService(serviceName,serviceDescription, price);
                
        }      
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
    clearAppointment();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
    int customerComboxIndex = newAppointmentCustomerCmbobx.getSelectedIndex();
    java.util.Date appointementDate = newAppointmentFormDate.getDate();
    
//    String contactNo = ContactNo_txt.getText();
//    java.util.Date selectedDate = ewDate.getDate();
    String startTimeStr = newAppointmentFormStartTime.getItemAt(newAppointmentFormStartTime.getSelectedIndex());
    String endTimeStr = newAppointmentFormEndTime.getItemAt(newAppointmentFormEndTime.getSelectedIndex());
    
        if (appointementDate == null || customerComboxIndex < 0 || this.newAppointmentServices.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please complete the form.");
            return; 
        }

//    // Convert selected date to LocalDate
//        LocalDate date = newAppointmentFormDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        Customer customer = this.customers.get(customerComboxIndex);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        LocalTime startTime = LocalTime.parse(startTimeStr, formatter);
        LocalTime endTime = LocalTime.parse(endTimeStr, formatter);
        
        UUID appointmentId = UUID.randomUUID();
        Appointment newAppointment = new Appointment(appointmentId, customer, new java.util.Date(), startTime, endTime, "PENDING", this.newAppointmentServices);
        
        newAppointment.save();
        
        for (AppointmentService appointmentService: newAppointment.items) {
            appointmentService.appointmentId = appointmentId;
            appointmentService.save();
        }
        
        this.newAppointmentService = null;
        this.newAppointmentServices = new ArrayList<>();
        refreshAppointmentServiceOptions();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void newAppointmentFormEndTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAppointmentFormEndTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newAppointmentFormEndTimeActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
    clearService();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
       
       logout();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
    refreshService();
    clearService();
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void serviceDescription_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceDescription_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serviceDescription_txtActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
     refreshStaff();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
    logout();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        int row = ServiceJtable.getSelectedRow();
        System.out.println(row);
        if (row < 0) {
            JOptionPane.showMessageDialog(ServicePane, "Please Select row to update.");
            return;
        }
        int serviceId = (int) ServiceJtable.getValueAt(row, 0);
        
        String serviceName = serviceName_txt.getText();
        String description = serviceDescription_txt.getText();
        String updatedPrice = servicePrice_txt.getText().trim();

        if (updatedPrice.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please enter a valid price.");
        return;
        }

        double price;
        try {
            price = Double.parseDouble(updatedPrice);
        } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Invalid price format. Please enter a valid number.");
           return;
        }


        if (serviceName.isEmpty() || description.isEmpty() || updatedPrice.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please complete the form.");
        } else {
            Service.updateService(serviceId, serviceName, description, price);
            refreshService();
            JOptionPane.showMessageDialog(ServicePane, "Update Successfully.");
        }
        

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        int row = ServiceJtable.getSelectedRow();
        System.out.println(row);
        if (row < 0) {
            JOptionPane.showMessageDialog(ServicePane, "Please Select row to delete.");
            return;
        }
        int serviceId = (int) ServiceJtable.getValueAt(row, 0);
        Service.deleteService(serviceId);
        refreshService();
        JOptionPane.showMessageDialog(ServicePane, "Delete Successfully.");
        
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
       int row = StaffsJtable.getSelectedRow();
        System.out.println(row);
        if (row < 0) {
            JOptionPane.showMessageDialog(StaffPanel, "Please Select row to delete.");
            return;
        }
        int serviceId = (int) StaffsJtable.getValueAt(row, 0);
        Staff.deleteStaff(serviceId);
        refreshStaff();
        JOptionPane.showMessageDialog(StaffPanel, "Delete Successfully.");
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
    
        int row = StaffsJtable.getSelectedRow();
            System.out.println(row);
        if (row < 0) {
            JOptionPane.showMessageDialog(StaffPanel, "Please Select row to update.");
            return;
        }
        int staffId = (int) StaffsJtable.getValueAt(row, 0);

        String staffName = StaffName_txt.getText();
        String service = ServiceComboBox.getItemAt(ServiceComboBox.getSelectedIndex());

        if (staffName.isEmpty() || service.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please complete the form.");
        } else {
            Staff.updateStaff(staffId, staffName, service);
            clearStaff();
            refreshStaff();
            JOptionPane.showMessageDialog(ServicePane, "Update Successfully.");
        }
        
        
    }//GEN-LAST:event_jButton25ActionPerformed

    private void ServiceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServiceComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ServiceComboBoxActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        clearCustomer();
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        
        int row = CustomerJTable.getSelectedRow();
        System.out.println(row);
        if (row < 0) {
            JOptionPane.showMessageDialog(CustomersPanel, "Please Select row to delete.");
            return;
        }
        int customerID = (int) CustomerJTable.getValueAt(row, 0);
        Customer.deleteCustomer(customerID);
        refreshCustomer();
        JOptionPane.showMessageDialog(CustomersPanel, "Delete Successfully.");
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        
        int row = CustomerJTable.getSelectedRow();
        System.out.println(row);
        if (row < 0) {
            JOptionPane.showMessageDialog(CustomersPanel, "Please Select row to update.");
            return;
        }
        
        int customerID = (int) CustomerJTable.getValueAt(row, 0);
        

        
        
       
        String customerName = Cname_txt.getText();
        String contactNo = Ccno_txt.getText();
        
        System.out.println(customerName + contactNo);

        if (customerName.isEmpty() || contactNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please complete the form.");
        } else {
            Customer.updateCustomer(customerID, customerName, contactNo);
            clearCustomer();
            refreshCustomer();
            JOptionPane.showMessageDialog(CustomersPanel, "Update Successfully.");
        }
        

    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        String customerName = Cname_txt.getText();
        String contactNo = Ccno_txt.getText();
        
        if (customerName.isEmpty() || contactNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please complete the form.");
        } else {
            Customer.addCustomer(customerName,contactNo);
                
        }        
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
    refreshCustomer();
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
    logout();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void newAppointmentCustomerCmbobxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAppointmentCustomerCmbobxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newAppointmentCustomerCmbobxActionPerformed

    private void newAppointmentServicesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newAppointmentServicesListMouseClicked

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        listModel.clear();
        newAppointmentStaffList.setModel(listModel);
        
        this.appointmentStaffOptions = new ArrayList<>();
        
        int index = newAppointmentServicesList.getSelectedIndex();
        if (index >= 0) { 
            Service service =this.appointmentServiceOptions.get(index);
            
            this.newAppointmentService = new AppointmentService(UUID.randomUUID(), UUID.randomUUID(), service, null, service.getPrice(), 0);
    
            for (Staff staff : this.staffs) {
                 if (staff.getService().getServiceID() == service.getServiceID()) {
                    this.appointmentStaffOptions.add(staff);
                    listModel.addElement(staff.getStaffName());
                }
            }
        }
    }//GEN-LAST:event_newAppointmentServicesListMouseClicked

    private void newAppointmentStaffListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newAppointmentStaffListMouseClicked
        int index = newAppointmentStaffList.getSelectedIndex();
        
        if (index >= 0) {
            Staff staff = this.appointmentStaffOptions.get(index);
            
            this.newAppointmentService.staff = staff;
            
            this.newAppointmentServices.add(this.newAppointmentService);
            
            System.err.println(this.newAppointmentServices.size());
            
            DefaultTableModel newAppointmentServicesTableModel = (DefaultTableModel) newAppointmentServicesTable.getModel();

            newAppointmentServicesTableModel.setRowCount(0);

            for (AppointmentService appointmentService : this.newAppointmentServices) {
                Object[] rowData = {appointmentService.service.getServiceName(), appointmentService.staff.getStaffName(), appointmentService.service.getPrice()};
                newAppointmentServicesTableModel.addRow(rowData);
            }
            
            refreshAppointmentServiceOptions();
            calculateNewAppointmentTotal();
        }
    }//GEN-LAST:event_newAppointmentStaffListMouseClicked

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
    jTabbedPane1.setSelectedIndex(7);
    refreshAppointment();
    refreshAppointmentsToPay();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void paymentAppointmentComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentAppointmentComboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentAppointmentComboboxActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Payment payment = new Payment(UUID.randomUUID(), this.selectedAppointment, new java.util.Date());
        payment.save();
        
        this.selectedAppointment.pay();
        
        JOptionPane.showMessageDialog(null, "Payment Successfully");
        
        this.selectedAppointment = null;
        refreshAppointment();
        refreshAppointmentsToPay();
        
        paymentAddTipButton.setEnabled(false);
        paymentAddTipInput.setEnabled(false);
        paymentAddTipInput.setText("");
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void paymentAddTipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentAddTipButtonActionPerformed
        int index = paymentServicesTable.getSelectedRow();
        
        if (index >= 0 && !paymentAddTipInput.getText().isEmpty()) {
            
            AppointmentService seleAppointmentService = this.selectedAppointment.items.get(index);
            seleAppointmentService.tip = Double.parseDouble(paymentAddTipInput.getText());
            seleAppointmentService.saveTip();
            
            this.selectedAppointment.items.set(index, seleAppointmentService);
            
            updatePaymentServicesTable();
        }
    }//GEN-LAST:event_paymentAddTipButtonActionPerformed

    private void paymentAppointmentComboboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_paymentAppointmentComboboxItemStateChanged
        if (paymentAppointmentCombobox.getSelectedIndex() >= 0) {
            for (Appointment appointment : this.appointments) {
                if (appointment.appointmentID.toString().equals(paymentAppointmentCombobox.getSelectedItem().toString())) {
                    this.selectedAppointment = appointment;
                    break;
                }
            }
        }
        
        this.updatePaymentServicesTable();
    }//GEN-LAST:event_paymentAppointmentComboboxItemStateChanged

    private void updatePaymentServicesTable () {
        DefaultTableModel paymentServicesTableModel = (DefaultTableModel) paymentServicesTable.getModel();
        paymentServicesTableModel.setRowCount(0);
        
     

        if (this.selectedAppointment != null) {
            for (AppointmentService appointmentService : this.selectedAppointment.items) {
                    Object[] rowData = {
                        appointmentService.service.getServiceName(),
                        appointmentService.staff.getStaffName(),
                        appointmentService.amount,
                        appointmentService.tip,
                    };

                    paymentServicesTableModel.addRow(rowData);
                }
            PaymentCsName.setText(this.selectedAppointment.getCustomer().getCustomerName());
            PaymentDate.setText(this.selectedAppointment.getDate().toString());
            PaymentStTime.setText(this.selectedAppointment.getStartTime().toString());
            PaymentEndTime.setText(this.selectedAppointment.getEndTime().toString());
            
            
            
            this.displayTotalToPay(this.selectedAppointment);
        }
    }
    
    private void paymentServicesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymentServicesTableMouseClicked
        int index = paymentServicesTable.getSelectedRow();

        paymentAddTipButton.setEnabled(index >= 0);
        paymentAddTipInput.setEnabled(index >= 0);
        
        if (index >= 0) {
            AppointmentService selectedAppointmentService = this.selectedAppointment.items.get(index);
            
             paymentAddTipInput.setText(String.valueOf(selectedAppointmentService.tip));
        }
    }//GEN-LAST:event_paymentServicesTableMouseClicked

    private void paymentAddTipInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymentAddTipInputKeyReleased
        try {
            Integer.parseInt(paymentAddTipInput.getText());
        } catch (NumberFormatException nfe) {
            paymentAddTipInput.setText("");
        }
    }//GEN-LAST:event_paymentAddTipInputKeyReleased

    private void DashboardDateChooserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DashboardDateChooserMouseClicked

    }//GEN-LAST:event_DashboardDateChooserMouseClicked
        
    private void newAppointmentFormStartTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newAppointmentFormStartTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newAppointmentFormStartTimeActionPerformed

    private void CustomerJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CustomerJTableMouseClicked
        int row = CustomerJTable.getSelectedRow();
        String csName = CustomerJTable.getValueAt(row, 1).toString();
        String csCN = CustomerJTable.getValueAt(row, 2).toString();

        Cname_txt.setText(csName);
        Ccno_txt.setText(csCN);
    }//GEN-LAST:event_CustomerJTableMouseClicked

    private void gsheMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gsheMouseClicked
        
    }//GEN-LAST:event_gsheMouseClicked

    private void ServiceJtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ServiceJtableMouseClicked
    int row = ServiceJtable.getSelectedRow();
    String srName = ServiceJtable.getValueAt(row, 1).toString();
    String srdescription = ServiceJtable.getValueAt(row, 2).toString();
    String srupdatedPrice = ServiceJtable.getValueAt(row, 3).toString();
    
    serviceName_txt.setText(srName);
    serviceDescription_txt.setText(srdescription);
    servicePrice_txt.setText(srupdatedPrice);

    }//GEN-LAST:event_ServiceJtableMouseClicked

    private void StaffsJtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_StaffsJtableMouseClicked
    int row = StaffsJtable.getSelectedRow();
    String stName = StaffsJtable.getValueAt(row, 1).toString();
    
    StaffName_txt.setText(stName);
    }//GEN-LAST:event_StaffsJtableMouseClicked

    private void DbcheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DbcheckActionPerformed
    java.util.Date selectedDate = DashboardDateChooser.getDate();
    
    if (selectedDate != null) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        
        String dateSales = dateFormatter.format(selectedDate);
        
        totalSales(dateSales);
        refreshDashboard(dateSales);
        
        
        
    } else {
        JOptionPane.showMessageDialog(DashboardPanel, "Select a Date");
    }    
    }//GEN-LAST:event_DbcheckActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton34ActionPerformed

    private void displayTotalToPay (Appointment appointment) {
        paymentTotalLabel.setText(String.valueOf(appointment.getTotal()));
    }
    
    private void calculateNewAppointmentTotal () {
        double total = 0;
        
        for (AppointmentService appointmentService : this.newAppointmentServices) {
            total = total + appointmentService.service.getPrice();
        }
        
        newAppointmentTotalLabel.setText(String.valueOf(total));
    }
    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AppointmentJtable;
    private javax.swing.JPanel AppointmentsPanel;
    private javax.swing.JTextField Ccno_txt;
    private javax.swing.JTextField Cname_txt;
    private javax.swing.JPanel CreateAppoinmentPanel;
    private javax.swing.JTable CustomerJTable;
    private javax.swing.JPanel CustomersPanel;
    private com.toedter.calendar.JDateChooser DashboardDateChooser;
    private javax.swing.JPanel DashboardPanel;
    private javax.swing.JTable DashboardTable;
    private javax.swing.JButton Dbcheck;
    private javax.swing.JLabel PaymentCsName;
    private javax.swing.JLabel PaymentDate;
    private javax.swing.JPanel PaymentDetailsPanel;
    private javax.swing.JLabel PaymentEndTime;
    private javax.swing.JLabel PaymentStTime;
    private javax.swing.JTable ReceptionistJtable;
    private javax.swing.JPanel ReceptionistPanel;
    private javax.swing.JComboBox<String> ServiceComboBox;
    private javax.swing.JTable ServiceJtable;
    private javax.swing.JPanel ServicePane;
    private javax.swing.JTextField StaffName_txt;
    private javax.swing.JPanel StaffPanel;
    private javax.swing.JTable StaffsJtable;
    private javax.swing.JLabel TotalSales;
    private javax.swing.JScrollPane gshe;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> newAppointmentCustomerCmbobx;
    private com.toedter.calendar.JDateChooser newAppointmentFormDate;
    private javax.swing.JComboBox<String> newAppointmentFormEndTime;
    private javax.swing.JComboBox<String> newAppointmentFormStartTime;
    private javax.swing.JButton newAppointmentNavBtn;
    private javax.swing.JList<String> newAppointmentServicesList;
    private javax.swing.JTable newAppointmentServicesTable;
    private javax.swing.JList<String> newAppointmentStaffList;
    private javax.swing.JLabel newAppointmentTotalLabel;
    private javax.swing.JButton paymentAddTipButton;
    private javax.swing.JTextField paymentAddTipInput;
    private javax.swing.JComboBox<String> paymentAppointmentCombobox;
    private javax.swing.JTable paymentServicesTable;
    private javax.swing.JLabel paymentTotalLabel;
    private javax.swing.JTextField serviceDescription_txt;
    private javax.swing.JTextField serviceName_txt;
    private javax.swing.JTextField servicePrice_txt;
    private javax.swing.JButton staffNavBtn;
    // End of variables declaration//GEN-END:variables
}
