/*
 * Data Access Object - Set up and manage connection to the database
 */
package DAO;

import java.sql.*;

public class DBConnection {
    //JDBC URL Parts utility class
    private static final String protocol = "jdbc";
    //Also called the Product name
    //Colons included because they are used to separate each section in a connection
    private static final String vendor = ":mysql:";
    //Server Name followed by a slash and the DB Name
    private static final String IPAddress = "";

    //Combined URL information
    private static final String JDBCUrl = protocol + vendor + IPAddress;

    //Driver and Connection interface reference
    private static final String mySQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    //Username and Password for the database
    private static final String Username = "";
    private static final String Password = "";

    //Makes the connection to the mysql database
    //Uses try/catch clauses, CI preferred this method
    public static Connection startConnection(){
        //Try to connect to the database
        try {
            Class.forName(mySQLJDBCDriver);
            conn = DriverManager.getConnection(JDBCUrl, Username, Password);
        }
        //Used if it can't find the driver
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        //Used if it can't find the connection
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void endConnection(){
        try{
            conn.close();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Connection getConnection(){
        return conn;
    }

}
