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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author amtis
 */
public class OrderTableView extends javax.swing.JPanel {

    /**
     * Creates new form OrderTableView
     */
    public OrderTableView() {
        initComponents();
        fetchRows();

        ProductSearchTextBox1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ProductSearchTextBox1.getText().equals("Search by Product Name")) {
                    ProductSearchTextBox1.setText("");
                    ProductSearchTextBox1.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ProductSearchTextBox1.getText().isEmpty()) {
                    ProductSearchTextBox1.setForeground(Color.GRAY);
                    ProductSearchTextBox1.setText("Search by Product Name");
                }
                fetchRows();
            }
        });

        ClientSearchTextBox2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ClientSearchTextBox2.getText().equals("Search by Client Name")) {
                    ClientSearchTextBox2.setText("");
                    ClientSearchTextBox2.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ClientSearchTextBox2.getText().isEmpty()) {
                    ClientSearchTextBox2.setForeground(Color.GRAY);
                    ClientSearchTextBox2.setText("Search by Client Name");
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

            String sql = "SELECT o.ID, c.name AS CLIENT_NAME, p.name AS PRODUCT_NAME, o.QUANTITY, o.CREATED_AT, o.DELIVERED_AT "
                    + "FROM ORDERS o "
                    + "JOIN Client c ON o.CLIENT_ID = c.ID "
                    + "JOIN Product p ON o.PRODUCT_ID = p.ID ";

            List<String> conditions = new ArrayList<>();
            if (!ProductSearchTextBox1.getText().equals("Search by Product Name")) {
                conditions.add("lower(p.name) LIKE ?");
            }

            if (!ClientSearchTextBox2.getText().equals("Search by Client Name")) {
                conditions.add("lower(c.name) LIKE ?");
            }

            if (!conditions.isEmpty()) {
                sql += "WHERE " + String.join(" AND ", conditions);
            }

            PreparedStatement statement = connection.prepareStatement(sql);

            int paramIndex = 1;
            if (!ProductSearchTextBox1.getText().equals("Search by Product Name")) {
                statement.setString(paramIndex++, "%" + ProductSearchTextBox1.getText().toLowerCase() + "%");
            }

            if (!ClientSearchTextBox2.getText().equals("Search by Client Name")) {
                statement.setString(paramIndex++, "%" + ClientSearchTextBox2.getText().toLowerCase() + "%");
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

            jTable1.setModel(new javax.swing.table.DefaultTableModel(dataArray, new String[]{"ID", "Client Name", "Product Name", "Quantity", "Created At", "Delivered At"}));
            jTable1.removeColumn(jTable1.getColumnModel().getColumn(0));

        } catch (SQLException ex) {
            Logger.getLogger(ClientsTableView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateRow(Object[] updatedModel) {
        try {
            // Establish connection to the database
            Connection connection = DBConnection.getConnection();

            // Prepare the SQL statement to update the row
            String sql = "UPDATE ORDER SET CLIENT_ID = ?, PRODUCT_ID = ?, QUANTITY = ?, CREATED_AT = ?, DELIVERED_AT = ?, WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, (String) updatedModel[1]);
//            statement.setDouble(2, Double.parseDouble((String) updatedModel[2]));
//            statement.setInt(3, Integer.parseInt((String) updatedModel[3]));
//            statement.setInt(4, Integer.parseInt((String) updatedModel[0]));

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

        jDialog1 = new javax.swing.JDialog();
        label1 = new java.awt.Label();
        label2 = new java.awt.Label();
        label3 = new java.awt.Label();
        jButton4 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        label4 = new java.awt.Label();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jCalendar2 = new com.toedter.calendar.JCalendar();
        label5 = new java.awt.Label();
        label6 = new java.awt.Label();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        ProductSearchTextBox1 = new javax.swing.JTextField();
        ClientSearchTextBox2 = new javax.swing.JTextField();

        jDialog1.setMinimumSize(new java.awt.Dimension(530, 730));

        label1.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        label1.setText("Add Order");

        label2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label2.setText("Client");

        label3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label3.setText("Product");

        jButton4.setText("Add Order");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        label4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label4.setText("Quantity");

        jComboBox2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jComboBox3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        label5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label5.setText("Quantity");

        label6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        label6.setText("Quantity");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.LEADING, 0, 87, Short.MAX_VALUE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                            .addComponent(jCalendar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCalendar2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );

        setPreferredSize(new java.awt.Dimension(548, 387));

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel1.setText("Orders Information");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Client Name", "Product Name", "Quantity", "Created At", "Delivered At"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Delete Selected");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Edit Selected");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        ProductSearchTextBox1.setText("Search by Product Name");
        ProductSearchTextBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ProductSearchTextBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductSearchTextBox1ActionPerformed(evt);
            }
        });

        ClientSearchTextBox2.setText("Search by Client Name");
        ClientSearchTextBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        ClientSearchTextBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientSearchTextBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProductSearchTextBox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ClientSearchTextBox2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ClientSearchTextBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(ProductSearchTextBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton1))
                .addContainerGap())
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

            String sql = "DELETE FROM ORDERS WHERE ID=?";

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

    private void ProductSearchTextBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductSearchTextBox1ActionPerformed

    }//GEN-LAST:event_ProductSearchTextBox1ActionPerformed

    private void ClientSearchTextBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientSearchTextBox2ActionPerformed

    }//GEN-LAST:event_ClientSearchTextBox2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.jDialog1.setLocationRelativeTo(this);
        this.jDialog1.setVisible(true);
        this.label1.setText("Add Order");
        this.jButton4.setText("Add Order");
        
        //populate things
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.jDialog1.setLocationRelativeTo(this);
        this.jDialog1.setVisible(true);
        this.label1.setText("Edit Order");
        this.jButton4.setText("Edit Order");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
//        updateRow();
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ClientSearchTextBox2;
    private javax.swing.JTextField ProductSearchTextBox1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private com.toedter.calendar.JCalendar jCalendar1;
    private com.toedter.calendar.JCalendar jCalendar2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    // End of variables declaration//GEN-END:variables
}
