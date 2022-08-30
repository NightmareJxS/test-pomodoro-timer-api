/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tdtrung.pomodorotimer.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Jason 2.0
 */
public class DBUtils {

        public static Connection getConnection() throws ClassNotFoundException, SQLException {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String url = "jdbc:sqlserver://localhost:1433;databaseName=PomodoroTimer;encrypt=true;trustServerCertificate=true;";
                Connection conn = DriverManager.getConnection(url, "sa", "sa12345");
                return conn;

        }
}
