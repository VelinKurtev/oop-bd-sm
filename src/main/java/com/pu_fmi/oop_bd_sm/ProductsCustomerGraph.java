/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.pu_fmi.oop_bd_sm;

import com.pu_fmi.oop_bd_sm.Entities.DBConnection;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author amtis
 */
public class ProductsCustomerGraph extends javax.swing.JPanel {

    private DefaultCategoryDataset createDataset() {
        
        try {
            String series1 = "Sales";

            String salesSQL = "SELECT WEEK(created_at), COALESCE(SUM(o.quantity * p.price),0) FROM Orders o"
                    + " JOIN product p on o.Product_id = p.id ";
            
            
            int selectedClient = ((Pair<String, Integer>) clientChooseBox.getSelectedItem()).Hidden;
            int selectedProduct = productChooseBox.getSelectedItem() == null ? -1 : ((Pair<String, Integer>) productChooseBox.getSelectedItem()).Hidden;
            int selectedYear = yearSlider.getValue();
            System.out.println("selectedYear reday " + selectedYear);
            salesSQL += " WHERE YEAR(o.created_at)=?";
            if (selectedClient != -1 && selectedProduct != -1) {
                salesSQL += " AND o.Client_id=? AND p.id=?";
            } else if (selectedClient != -1) {
                salesSQL += "  AND o.Client_id=?";

            } else if (selectedProduct != -1) {
                salesSQL += "  AND p.id=?";
            }

            salesSQL += " GROUP BY WEEK(created_at) ORDER BY WEEK(created_at)";
            System.out.println(salesSQL);

            Connection connection = DBConnection.getConnection();
            var st = connection.prepareStatement(salesSQL);
            st.setInt(1, selectedYear);
            if (selectedClient != -1 && selectedProduct != -1) {

                st.setInt(2, selectedClient);
                st.setInt(3, selectedProduct);

            } else if (selectedClient != -1) {

                st.setInt(2, selectedClient);
            } else if (selectedProduct != -1) {

                st.setInt(2, selectedProduct);
            }

            ResultSet sales = st.executeQuery();

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            while (sales.next()) {
                Number salesValue = sales.getDouble(2); // Cast to Number
                Comparable category = sales.getInt(1); // Make sure this is Comparable
                dataset.addValue(salesValue, series1, category);

            }
            System.out.println("Dataset ready ");
            return dataset;
        } catch (SQLException ex) {
            Logger.getLogger(ProductsCustomerGraph.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    ChartPanel chartPan = null;
    JFreeChart chart = null;

    /**
     * Creates new form ProductsCustomerGraph
     */
    public ProductsCustomerGraph() {
        initComponents();
        try {
            fetchCheckboxes();
        } catch (SQLException ex) {
            Logger.getLogger(ProductsCustomerGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        chartHolder.setDoubleBuffered(false);
        //chartHolder.setPreferredSize(new Dimension(548,300));
        // Create chart  
        yearSlider.setMaximum(Year.now().getValue() + 1);
        yearSlider.setValue(Year.now().getValue());
        drawChart();

    }
    int zsofar = 0;

    void drawChart() {
        if (chartPan != null) {
            chartPan.removeAll();
            
        }
        chartHolder.removeAll();
        DefaultCategoryDataset dataset = createDataset();

        chart = ChartFactory.createLineChart(
                "Sales", // Chart title  
                "Week", // X-Axis Label  
                "Income", // Y-Axis Label  
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );

        ChartPanel panel = new ChartPanel(chart);

        panel.setSize(648, 200);
        chartPan = panel;
        chartHolder.add(panel, 0);
        MouseEvent event = new MouseEvent(
                panel,
                MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(),
                0,
                10,
                10,
                1,
                true
        );
        chart.fireChartChanged();
        // Dispatch the event to the component
        this.dispatchEvent(event);
        chartHolder.dispatchEvent(event);
        chart.fireChartChanged();

        System.out.println("Draw ready ");

    }

    private void fetchCheckboxes() throws SQLException {
        String selCustomers = "SELECT ID, CONCAT(NAME,' ',Phone) FROM CLIENT";
        String selProduct = "SELECT ID, NAME FROM Product";

        Connection connection = DBConnection.getConnection();
        Statement st = connection.createStatement();
        ResultSet customers = st.executeQuery(selCustomers);
        clientChooseBox.addItem(new Pair<String, Integer>("All", -1));

        while (customers.next()) {
            var t = new Pair<String, Integer>(customers.getString(2), customers.getInt(1));
            clientChooseBox.addItem(t);

        }
        ResultSet products = st.executeQuery(selProduct);
        productChooseBox.addItem(new Pair<String, Integer>("All", -1));

        while (products.next()) {
            var t = new Pair<String, Integer>(products.getString(2), products.getInt(1));
            productChooseBox.addItem(t);

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

        clientChooseBox = new javax.swing.JComboBox<>();
        productChooseBox = new javax.swing.JComboBox<>();
        chartHolder = new javax.swing.JScrollPane();
        yearSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        clientChooseBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientChooseBoxActionPerformed(evt);
            }
        });

        productChooseBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productChooseBoxActionPerformed(evt);
            }
        });

        chartHolder.setName(""); // NOI18N

        yearSlider.setMinimum(2000);
        yearSlider.setMinorTickSpacing(1);
        yearSlider.setSnapToTicks(true);
        yearSlider.setValue(2023);
        yearSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yearSliderStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 3, 24)); // NOI18N
        jLabel1.setText("Year");

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 3, 24)); // NOI18N
        jLabel2.setText("Client");

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 3, 24)); // NOI18N
        jLabel3.setText("Product");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chartHolder)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clientChooseBox, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productChooseBox, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(yearSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clientChooseBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productChooseBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(chartHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void clientChooseBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientChooseBoxActionPerformed
        drawChart();
    }//GEN-LAST:event_clientChooseBoxActionPerformed

    private void productChooseBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productChooseBoxActionPerformed
        drawChart();
    }//GEN-LAST:event_productChooseBoxActionPerformed

    private void yearSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yearSliderStateChanged
        jLabel1.setText("Year " + yearSlider.getValue());
        drawChart();
    }//GEN-LAST:event_yearSliderStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane chartHolder;
    private javax.swing.JComboBox<Pair<String,Integer>> clientChooseBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox<Pair<String,Integer>> productChooseBox;
    private javax.swing.JSlider yearSlider;
    // End of variables declaration//GEN-END:variables
}
