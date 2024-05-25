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
    private static final String PASSWORD = "test";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void Init() throws SQLException {
        String initMigration = "-- Create the Category_Product table\n"
                + "CREATE TABLE IF NOT EXISTS Category_Product (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    category_name VARCHAR(255) NOT NULL UNIQUE\n"
                + ");\n"
                + "\n"
                + "-- Create the Product table\n"
                + "CREATE TABLE IF NOT EXISTS Product (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    name VARCHAR(255) NOT NULL,\n"
                + "    Category_Product INT NOT NULL,\n"
                + "    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),\n"
                + "    FOREIGN KEY (Category_Product) REFERENCES Category_Product(id)\n"
                + ");\n"
                + "\n"
                + "-- Create the Warehouse table\n"
                + "CREATE TABLE IF NOT EXISTS Warehouse (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    location VARCHAR(255) NOT NULL,\n"
                + "    name VARCHAR(255) NOT NULL UNIQUE\n"
                + ");\n"
                + "\n"
                + "-- Create the Warehouse_Products table\n"
                + "CREATE TABLE IF NOT EXISTS Warehouse_Products (\n"
                + "    Warehouse_id INT,\n"
                + "    Product_id INT,\n"
                + "    PRIMARY KEY (Warehouse_id, Product_id),\n"
                + "    FOREIGN KEY (Warehouse_id) REFERENCES Warehouse(id),\n"
                + "    FOREIGN KEY (Product_id) REFERENCES Product(id)\n"
                + ");\n"
                + "\n"
                + "-- Create the Client table\n"
                + "CREATE TABLE IF NOT EXISTS Client (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    address VARCHAR(255) NOT NULL,\n"
                + "    name VARCHAR(255) NOT NULL,\n"
                + "    phone VARCHAR(15) NOT NULL UNIQUE CHECK (phone REGEXP '^[0-9]+$')\n"
                + ");\n"
                + "\n"
                + "-- Create the Orders table\n"
                + "CREATE TABLE IF NOT EXISTS Orders (\n"
                + "    id INT PRIMARY KEY AUTO_INCREMENT,\n"
                + "    Client_id INT NOT NULL,\n"
                + "    Product_id INT NOT NULL,\n"
                + "    quantity INT NOT NULL CHECK (quantity > 0),\n"
                + "    created_at DATETIME NOT NULL,\n"
                + "    delivered_at DATETIME,\n"
                + "    delivered_from INT,\n"
                + "    FOREIGN KEY (Client_id) REFERENCES Client(id),\n"
                + "    FOREIGN KEY (Product_id) REFERENCES Product(id),\n"
                + "    FOREIGN KEY (delivered_from) REFERENCES Warehouse(id),\n"
                + "    CHECK (delivered_at IS NULL OR delivered_at >= created_at)\n"
                + ");";
        Connection connection = DBConnection.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(initMigration);
        
        
        return;
    }
}
