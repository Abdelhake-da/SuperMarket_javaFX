package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class GoConnection {
    private static Connection connection = null;
    private static int role = 0;
    public static Connection getConnection() throws SQLException {
        String Url = "jdbc:mysql://localhost:3306/supermarket_javafx"
                + "?useunicode=true&charcterEncoding=URF-8";
        connection = DriverManager.getConnection(Url,"root","");
        return connection;
    }

    public static int getRole() {
        return role;
    }

    public static void setRole(int role) {
        GoConnection.role = role;
    }
}
