package SalonandSpa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;



public class Login_Admin extends javax.swing.JFrame {

    public Login_Admin() {
        initComponents();
    }

    Connection conn = DatabaseConnector.connect();
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        Right = new javax.swing.JPanel();
        txt_icon = new javax.swing.JLabel();
        Left = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        UserName_TxtField = new javax.swing.JTextField();
        Password_TxtField = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btn_login = new javax.swing.JButton();
        ReceptionistBottom = new javax.swing.JRadioButton();
        AdminBottom = new javax.swing.JRadioButton();
        jMenuBar2 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        Right.setBackground(new java.awt.Color(0, 102, 102));

        txt_icon.setIcon(new javax.swing.ImageIcon("C:\\Users\\user\\Downloads\\logo-brand-beauty-parlour-natural-beauty-salon-spa-natural-beauty-thumbnail-removebg-preview.png")); // NOI18N

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(txt_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(txt_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jPanel1.add(Right);
        Right.setBounds(0, 0, 400, 500);

        Left.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("Password");

        UserName_TxtField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        UserName_TxtField.setForeground(new java.awt.Color(102, 102, 102));
        UserName_TxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserName_TxtFieldActionPerformed(evt);
            }
        });

        Password_TxtField.setPreferredSize(new java.awt.Dimension(7, 28));
        Password_TxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Password_TxtFieldActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jLabel3.setText("LOGIN");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Username");

        btn_login.setBackground(new java.awt.Color(0, 102, 102));
        btn_login.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btn_login.setForeground(new java.awt.Color(255, 255, 255));
        btn_login.setText("Login");
        btn_login.setBorder(null);
        btn_login.setPreferredSize(new java.awt.Dimension(7, 28));
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });

        ReceptionistBottom.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(ReceptionistBottom);
        ReceptionistBottom.setFont(new java.awt.Font("Segoe UI Emoji", 1, 12)); // NOI18N
        ReceptionistBottom.setText("Receptionist");
        ReceptionistBottom.setBorder(null);
        ReceptionistBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReceptionistBottomActionPerformed(evt);
            }
        });

        AdminBottom.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(AdminBottom);
        AdminBottom.setFont(new java.awt.Font("Segoe UI Emoji", 1, 12)); // NOI18N
        AdminBottom.setText("Admin");
        AdminBottom.setBorder(null);
        AdminBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminBottomActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftLayout.createSequentialGroup()
                .addContainerGap(128, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(UserName_TxtField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                        .addComponent(Password_TxtField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(LeftLayout.createSequentialGroup()
                            .addComponent(AdminBottom)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ReceptionistBottom))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UserName_TxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Password_TxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ReceptionistBottom)
                    .addComponent(AdminBottom))
                .addGap(21, 21, 21)
                .addComponent(btn_login, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(146, 146, 146))
        );

        jPanel1.add(Left);
        Left.setBounds(400, 0, 400, 500);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Password_TxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Password_TxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Password_TxtFieldActionPerformed

    private void UserName_TxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserName_TxtFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UserName_TxtFieldActionPerformed

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_loginActionPerformed

        
        boolean isAdmin = AdminBottom.isSelected();
        boolean isReceptionist= ReceptionistBottom.isSelected();
        
       String adminUsername = UserName_TxtField.getText().trim();
       String adminPassword = String.valueOf(Password_TxtField.getPassword()).trim();
       String receptionistUsername = UserName_TxtField.getText().trim();
       String receptionistPassword = String.valueOf(Password_TxtField.getPassword()).trim();

        if (adminUsername.isEmpty() || adminPassword.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        return;
    }

    String query = "SELECT * FROM admin WHERE Username = ? AND Password = ?";
    String query1 = "SELECT * FROM receptionist WHERE Username = ? AND Password = ?";

    
    if(isAdmin){
    try {
        
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, adminUsername);
        pstmt.setString(2, adminPassword);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            javax.swing.JOptionPane.showMessageDialog(this, "LOGIN SUCCESSFUL", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);


            Admin_Menu menuFrame = new Admin_Menu();
            menuFrame.setVisible(true);
            menuFrame.pack();
            menuFrame.setLocationRelativeTo(null);
            this.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Login Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Database error. Please try again later.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }
    else if(isReceptionist){
        try {
        
        java.sql.PreparedStatement pstmt = conn.prepareStatement(query1);
        pstmt.setString(1, receptionistUsername);
        pstmt.setString(2, receptionistPassword);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Login successful.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

 
            Receptionist_Menu ReceptionistFrame = new Receptionist_Menu();
            ReceptionistFrame.setVisible(true);
            ReceptionistFrame.pack();
            ReceptionistFrame.setLocationRelativeTo(null);
       this.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.", "Login Failed", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Database error. Please try again later.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }
    else{
        javax.swing.JOptionPane.showMessageDialog(this, "Select a Role", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_btn_loginActionPerformed

    
    private void ReceptionistBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReceptionistBottomActionPerformed
       
        
    }//GEN-LAST:event_ReceptionistBottomActionPerformed

    private void AdminBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminBottomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AdminBottomActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton AdminBottom;
    private javax.swing.JPanel Left;
    private javax.swing.JPasswordField Password_TxtField;
    private javax.swing.JRadioButton ReceptionistBottom;
    private javax.swing.JPanel Right;
    private javax.swing.JTextField UserName_TxtField;
    private javax.swing.JButton btn_login;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txt_icon;
    // End of variables declaration//GEN-END:variables
}
