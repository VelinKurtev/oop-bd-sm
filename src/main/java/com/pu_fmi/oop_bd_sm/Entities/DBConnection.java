/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pu_fmi.oop_bd_sm.Entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author amtis
 */
public class DBConnection {

    private static final String JDBC_URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE"; // Use the appropriate path and database name
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void Init() throws SQLException {
        String initMigration = 
                "CREATE TABLE IF NOT EXISTS Product (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    name VARCHAR(255) NOT NULL,\n"
                + "    price DECIMAL(10, 2) NOT NULL,\n"                
                + "    quantity INT NOT NULL\n"
                + ");\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS Client (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    address VARCHAR(255) NOT NULL,\n"
                + "    name VARCHAR(255) NOT NULL,\n"
                + "    phone VARCHAR(15) NOT NULL UNIQUE\n"
                + ");\n"
                + "\n"
                + "CREATE TABLE IF NOT EXISTS Orders (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    Client_id INT NOT NULL,\n"
                + "    Product_id INT NOT NULL,\n"
                + "    quantity INT NOT NULL CHECK (quantity > 0),\n"
                + "    created_at DATETIME NOT NULL,\n"
                + "    delivered_at DATETIME,\n"
                + "    delivered_from VARCHAR(255),\n"
                + "    FOREIGN KEY (Client_id) REFERENCES Client(id),\n"
                + "    FOREIGN KEY (Product_id) REFERENCES Product(id)\n" +
        ")";
        Connection connection = DBConnection.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(initMigration);

        return;
    }
}
