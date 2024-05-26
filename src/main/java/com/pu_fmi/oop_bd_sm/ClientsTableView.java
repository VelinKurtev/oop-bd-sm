/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.pu_fmi.oop_bd_sm;

import com.pu_fmi.oop_bd_sm.Entities.DBConnection;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

/**
 *
 * @author amtis
 */
public class ClientsTableView extends javax.swing.JPanel {

    /**
     * Creates new form ClientsTableView
     */
    public ClientsTableView() {
        initComponents();
        fetchRows();
        ClientSearchTextBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ClientSearchTextBox.getText().equals("Search by Name")) {
                    ClientSearchTextBox.setText("");
                    ClientSearchTextBox.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ClientSearchTextBox.getText().isEmpty()) {
                    ClientSearchTextBox.setForeground(Color.GRAY);
                    ClientSearchTextBox.setText("Search by Name");
                }
                fetchRows();

            }

        });
        
        this.nameToAddTField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameToAddTField.getText().equals("Name")) {
                    nameToAddTField.setText("");
                    nameToAddTField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameToAddTField.getText().isEmpty()) {
                    nameToAddTField.setForeground(Color.GRAY);
                    nameToAddTField.setText("Name");
                }
            }
        });

        this.addressToAddTField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (addressToAddTField.getText().equals("Address")) {
                    addressToAddTField.setText("");
                    addressToAddTField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (addressToAddTField.getText().isEmpty()) {
                    addressToAddTField.setForeground(Color.GRAY);
                    addressToAddTField.setText("Address");
                }
            }
        });
        
        this.phoneToAddTField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (phoneToAddTField.getText().equals("Phone")) {
                    phoneToAddTField.setText("");
                    phoneToAddTField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (phoneToAddTField.getText().isEmpty()) {
                    phoneToAddTField.setForeground(Color.GRAY);
                    phoneToAddTField.setText("Phone");
                }
            }
        });

        jTable1.getModel().addTableModelListener((TableModelEvent e) -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (row >= 0 && column >= 0) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                Object[] updatedModel = {model.getValueAt(row, 0), model.getValueAt(row, 1), model.getValueAt(row, 2), model.getValueAt(row, 3)};
                updateRow(updatedModel);
            }
        });

    }

    private void fetchRows() {

        try {
            Connection connection = DBConnection.getConnection();
            boolean haveClient = !ClientSearchTextBox.getText().equals("Search by Name");
            boolean haveAddress = !addressSearchBar.getText().equals("Search by Address");

            String sql = "SELECT c.ID,c.NAME,c.ADDRESS,c.PHONE, CONCAT(COALESCE (SUM(o.quantity * p.price),0)  , '$') AS total_purchases  FROM CLIENT c";

            sql += " LEFT JOIN \n"
                    + "    Orders o ON c.id = o.Client_id\n"
                    + "LEFT JOIN \n"
                    + "    Product p ON o.Product_id = p.id\n";

            if (haveAddress && haveClient) {
                sql += " WHERE lower(c.name) LIKE ? AND lower(c.address) LIKE ?";

            } else if (haveClient) {
                sql += " WHERE lower(c.name) LIKE ?";
            } else if (haveAddress) {
                sql += " WHERE lower(c.address) LIKE ?";

            }
            sql += " GROUP BY \n"
                    + "    c.ID,c.NAME,c.ADDRESS,c.PHONE";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (haveAddress && haveClient) {
                statement.setString(1, "%" + ClientSearchTextBox.getText().toLowerCase() + "%");
                statement.setString(2, "%" + addressSearchBar.getText().toLowerCase() + "%");

            } else if (haveClient) {
                statement.setString(1, "%" + ClientSearchTextBox.getText().toLowerCase() + "%");
            } else if (haveAddress) {
                statement.setString(1, "%" + addressSearchBar.getText().toLowerCase() + "%");

            }

            ResultSet resultSet = statement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();

            // Use ArrayList to dynamically store the results
            ArrayList<Object[]> results = new ArrayList<>();

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int col = 0; col < columnCount; col++) {
                    rowData[col] = resultSet.getString(col + 1);
                }
                results.add(rowData);
            }

            // Convert ArrayList to array
            Object[][] dataArray = new Object[results.size()][];
            for (int i = 0; i < results.size(); i++) {
                dataArray[i] = results.get(i);
            }

            jTable1.setModel(
                    new javax.swing.table.DefaultTableModel(dataArray, new String[]{"ID", "Name", "Address", "Phone", "Purchases"}) {
                boolean[] canEdit = new boolean[]{
                    true, true, true, true, false
                };

                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }

            });
            jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));

        } catch (SQLException ex) {
            Logger.getLogger(ClientsTableView.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void updateRow(Object[] updatedModel) {
        try {
            // Establish connection to the database
            Connection connection = DBConnection.getConnection();

            // Prepare the SQL statement to update the row
            String sql = "UPDATE CLIENT SET NAME = ?, ADDRESS = ?, PHONE = ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, (String) updatedModel[1]);
            statement.setString(2, ((String) updatedModel[2]));
            statement.setString(3, (String) updatedModel[3]);
            statement.setInt(4, Integer.parseInt((String) updatedModel[0]));

            // Execute the update statement
            int rowsAffected = statement.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0) {
                System.out.println("Row updated successfully in the database.");
            } else {
                System.out.println("Failed to update row in the database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println("generic error2");
            ex.printStackTrace();

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        ClientSearchTextBox = new javax.swing.JTextField();
        deleteButton = new javax.swing.JButton();
        nameToAddTField = new javax.swing.JTextField();
        addressToAddTField = new javax.swing.JTextField();
        phoneToAddTField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        addressSearchBar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Address", "Phone", "Purchases"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        ClientSearchTextBox.setText("Search by Name");
        ClientSearchTextBox.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ClientSearchTextBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientSearchTextBoxActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete Selected");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        nameToAddTField.setText("Name");
        nameToAddTField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameToAddTFieldActionPerformed(evt);
            }
        });

        addressToAddTField.setText("Address");
        addressToAddTField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressToAddTFieldActionPerformed(evt);
            }
        });

        phoneToAddTField.setText("Phone");
        phoneToAddTField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneToAddTFieldActionPerformed(evt);
            }
        });

        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("New Client");

        addressSearchBar.setText("Search by Address");
        addressSearchBar.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        addressSearchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressSearchBarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel1.setText("Clients Information");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameToAddTField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addressToAddTField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(phoneToAddTField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addressSearchBar)
                    .addComponent(ClientSearchTextBox)
                    .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ClientSearchTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addressSearchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameToAddTField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addressToAddTField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneToAddTField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ClientSearchTextBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientSearchTextBoxActionPerformed
        fetchRows();
    }//GEN-LAST:event_ClientSearchTextBoxActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        try {
            int selectedRowIndex = jTable1.getSelectedRow();

            // Check if a row is selected
            if (selectedRowIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel model = ((DefaultTableModel) jTable1.getModel());

            String id = (String) model.getValueAt(selectedRowIndex, 0);

            Connection connection = DBConnection.getConnection();

            String sql = "DELETE FROM CLIENT WHERE ID=?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, Integer.valueOf(id));

            statement.execute();

            model.removeRow(selectedRowIndex);
        } catch (SQLException ex) {
            System.out.println("sql error");

            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occured, try again");
        } catch (Exception ex) {
            System.out.println("generic error");
            ex.printStackTrace();

        }


    }//GEN-LAST:event_deleteButtonActionPerformed

    private void nameToAddTFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameToAddTFieldActionPerformed
        String name = nameToAddTField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            nameToAddTField.setForeground(Color.red);
            return;
        }
        nameToAddTField.setForeground(Color.BLACK);

    }//GEN-LAST:event_nameToAddTFieldActionPerformed

    private void addressToAddTFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressToAddTFieldActionPerformed
        String address = addressToAddTField.getText().trim();
        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            addressToAddTField.setForeground(Color.red);
            return;
        }
        addressToAddTField.setForeground(Color.BLACK);

    }//GEN-LAST:event_addressToAddTFieldActionPerformed

    private void phoneToAddTFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneToAddTFieldActionPerformed
        String phone = phoneToAddTField.getText().trim();

        // Define a regular expression pattern for a valid phone number format
        String phonePattern = "(" + "\\" + "+359|0)[0-9]{9,9}"; // Allows optional '+' sign and at least 10 digits

        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile(phonePattern);

        // Check if the input matches the pattern
        if (!pattern.matcher(phone).matches()) {
            JOptionPane.showMessageDialog(this, "Invalid phone number format", "Error", JOptionPane.ERROR_MESSAGE);
            phoneToAddTField.setForeground(Color.red);
            return;
        }
        phoneToAddTField.setForeground(Color.BLACK);

    }//GEN-LAST:event_phoneToAddTFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String phone = phoneToAddTField.getText().trim();

            // Define a regular expression pattern for a valid phone number format
            String phonePattern = "(" + "\\" + "+359|0)[0-9]{9,9}"; // Allows optional '+' sign and at least 10 digits

            // Compile the regular expression pattern
            Pattern pattern = Pattern.compile(phonePattern);

            // Check if the input matches the pattern
            if (!pattern.matcher(phone).matches()) {
                JOptionPane.showMessageDialog(this, "Invalid phone number format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String address = addressToAddTField.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Address cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = nameToAddTField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection connection = DBConnection.getConnection();

            String sql = "INSERT INTO CLIENT (NAME,ADDRESS,PHONE) VALUES(?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, phone);

            statement.execute();

            fetchRows();

            phoneToAddTField.setText("");
            addressToAddTField.setText("");

            nameToAddTField.setText("");
        } catch (JdbcSQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Phone duplicated", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Address cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void addressSearchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressSearchBarActionPerformed
        fetchRows();
    }//GEN-LAST:event_addressSearchBarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ClientSearchTextBox;
    private javax.swing.JTextField addressSearchBar;
    private javax.swing.JTextField addressToAddTField;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nameToAddTField;
    private javax.swing.JTextField phoneToAddTField;
    // End of variables declaration//GEN-END:variables
}
