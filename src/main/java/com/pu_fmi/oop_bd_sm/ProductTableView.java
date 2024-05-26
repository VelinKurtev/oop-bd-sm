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
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
/**
 *
 * @author amtis
 */
public class ProductTableView extends javax.swing.JPanel {

    /**
     * Creates new form ProductTableView
     */
    public ProductTableView() {
        initComponents();
        fetchRows();

        ProductSearchTextBox1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ProductSearchTextBox1.getText().equals("Search by Name")) {
                    ProductSearchTextBox1.setText("");
                    ProductSearchTextBox1.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ProductSearchTextBox1.getText().isEmpty()) {
                    ProductSearchTextBox1.setForeground(Color.GRAY);
                    ProductSearchTextBox1.setText("Search by Name");
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

        jTable1.getModel().addTableModelListener((TableModelEvent e) -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (row >= 0 && column >= 0) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                Object[] updatedModel = {model.getValueAt(row, 0), model.getValueAt(row, 1), model.getValueAt(row, 2), model.getValueAt(row, 3)};
                updateRow(updatedModel);
            }
        });
        
        
        SpinnerNumberModel model = new SpinnerNumberModel(0.0, 0.0, 10.0, 0.1);
        this.jSpinner1.setModel(model);
    }
    
    private void fetchRows() {

        try {
            Connection connection = DBConnection.getConnection();

            String sql = "SELECT ID,NAME,PRICE,QUANTITY FROM PRODUCT";
            if (!ProductSearchTextBox1.getText().equals("Search by Name")) {
                sql += " WHERE lower(name) LIKE ?";
            }
            PreparedStatement statement = connection.prepareStatement(sql);

            if (!ProductSearchTextBox1.getText().equals("Search by Name")) {
                statement.setString(1, "%" + ProductSearchTextBox1.getText().toLowerCase() + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            int columnCount = resultSet.getMetaData().getColumnCount();
            
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

            jTable1.setModel(new javax.swing.table.DefaultTableModel(dataArray, new String[]{"ID", "Name", "Price", "Quantity"}));
            jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));

        } catch (SQLException ex) {
            Logger.getLogger(ClientsTableView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void updateRow(Object[] updatedModel) {
        try {
            Connection connection = DBConnection.getConnection();
            
            String sql = "UPDATE PRODUCT SET NAME = ?, PRICE = ?, QUANTITY = ? WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, (String) updatedModel[1]);
            statement.setDouble(2, Double.parseDouble((String) updatedModel[2]));
            statement.setInt(3, Integer.parseInt((String) updatedModel[3]));
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        nameToAddTField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        ProductSearchTextBox1 = new javax.swing.JTextField();

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel1.setText("Products Information");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Price", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Delete Selected");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("New Product");

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        nameToAddTField.setText("Name");
        nameToAddTField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameToAddTFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("Price");

        jLabel4.setText("Quantity");

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(0.0f, 0.0f, null, 1.0f));

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        ProductSearchTextBox1.setText("Search by Name");
        ProductSearchTextBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ProductSearchTextBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductSearchTextBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nameToAddTField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ProductSearchTextBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ProductSearchTextBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameToAddTField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

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

            String sql = "DELETE FROM PRODUCT WHERE ID=?";

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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // Validate and trim the name field
            String name = nameToAddTField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price;
            int quantity;

            try {
                // Parse price and quantity from spinners
                price = Double.parseDouble(jSpinner1.getValue().toString());
                quantity = Integer.parseInt(jSpinner2.getValue().toString());
            } catch (NumberFormatException e) {
                // Handle invalid number format in spinners
                JOptionPane.showMessageDialog(this, "Price and Quantity must be valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate price and quantity values
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Connect to the database and insert the new record
            Connection connection = DBConnection.getConnection();
            String sql = "INSERT INTO PRODUCT (NAME, PRICE, QUANTITY) VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameters for the prepared statement
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);

            // Execute the statement
            statement.execute();

            // Refresh the data in the UI
            fetchRows();

            // Clear the input fields
            nameToAddTField.setText("");
            jSpinner1.setValue(0.00);
            jSpinner2.setValue(0);
        } catch (SQLException ex) {
            // Handle SQL exceptions
            JOptionPane.showMessageDialog(this, "SQL error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Handle all other exceptions
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void nameToAddTFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameToAddTFieldActionPerformed
        String name = nameToAddTField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            nameToAddTField.setForeground(Color.red);
            return;
        }
        nameToAddTField.setForeground(Color.BLACK);
    }//GEN-LAST:event_nameToAddTFieldActionPerformed

    private void ProductSearchTextBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductSearchTextBox1ActionPerformed
        fetchRows();
    }//GEN-LAST:event_ProductSearchTextBox1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ProductSearchTextBox1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nameToAddTField;
    // End of variables declaration//GEN-END:variables
}
