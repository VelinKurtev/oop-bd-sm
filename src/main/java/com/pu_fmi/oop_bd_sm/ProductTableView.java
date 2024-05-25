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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
        
        ProductSearchTextBox.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ProductSearchTextBox.getText().equals("Search")) {
                    ProductSearchTextBox.setText("");
                    ProductSearchTextBox.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ProductSearchTextBox.getText().isEmpty()) {
                    ProductSearchTextBox.setForeground(Color.GRAY);
                    ProductSearchTextBox.setText("Search");
                }
                fetchRows();
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

            String sql = "SELECT ID,NAME,PRICE,QUANTITY FROM PRODUCT";
            if (!ProductSearchTextBox.getText().equals("Search")) {
                sql += " WHERE lower(name) LIKE ?";
            }
            PreparedStatement statement = connection.prepareStatement(sql);

            if (!ProductSearchTextBox.getText().equals("Search")) {
                statement.setString(1, "%" + ProductSearchTextBox.getText().toLowerCase() + "%");
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

            jTable1.setModel(new javax.swing.table.DefaultTableModel(dataArray, new String[]{"ID","Name", "Price", "Quantity"}));
            jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));
          
        } catch (SQLException ex) {
            Logger.getLogger(ClientsTableView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void updateRow(Object[] updatedModel){
        try {
            // Establish connection to the database
            Connection connection = DBConnection.getConnection();

            // Prepare the SQL statement to update the row
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
        ProductSearchTextBox = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

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

        ProductSearchTextBox.setText("Search");
        ProductSearchTextBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductSearchTextBoxActionPerformed(evt);
            }
        });
        ProductSearchTextBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProductSearchTextBoxKeyPressed(evt);
            }
        });

        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ProductSearchTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ProductSearchTextBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void ProductSearchTextBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProductSearchTextBoxKeyPressed
        
    }//GEN-LAST:event_ProductSearchTextBoxKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRowIndex = jTable1.getSelectedRow();

        // Check if a row is selected
        if (selectedRowIndex != -1) {
            ((DefaultTableModel) jTable1.getModel()).removeRow(selectedRowIndex);
        } else {
            // If no row is selected, display a message to the user
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ProductSearchTextBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductSearchTextBoxActionPerformed
         fetchRows();
    }//GEN-LAST:event_ProductSearchTextBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ProductSearchTextBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
