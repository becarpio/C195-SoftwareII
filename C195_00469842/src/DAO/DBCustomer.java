/*
 * Data Access Object - customer and address Tables
 * Set up to getAll, filteredList, add, delete, and update both tables
 */

package DAO;

import Model.City;
import Model.Customer;
import Utility.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.ZonedDateTime;


public class DBCustomer{
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();
    private static Connection conn = DBConnection.getConnection();

    public static ObservableList<Customer> getAll() throws SQLException {

        allCustomers.clear();

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT c.customerId, c.customerName, c.active, c.addressId, a.address, a.address2, a.postalCode, " +
                "a.phone, city.city FROM customer c LEFT OUTER JOIN address a ON c.addressId = a.addressId LEFT OUTER JOIN city ON a.cityId = city.cityId";


        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int customerID = rs.getInt("customerId");
            String customer = rs.getString("customerName");
            Boolean active = rs.getBoolean("active");
            int addressID = rs.getInt("addressId");
            String address = rs.getString("address");
            String address2 = rs.getString("address2");
            int postalCode = rs.getInt("postalCode");
            String phone = rs.getString("phone");
            String city = rs.getString("city");


            Customer newCustomer = new Customer(customerID, customer, active, addressID, address, address2, postalCode, phone, city);
            allCustomers.add(newCustomer);
        }

        return allCustomers;
    }

    public static ObservableList<Customer> getFiltered(String colName, String s) throws SQLException {
        if(!filteredCustomers.isEmpty()){
            filteredCustomers.clear();
        }

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();

        String selectStatement = "SELECT c.customerId, c.customerName, c.active, c.addressId, a.address, a.address2, a.postalCode, " +
                "a.phone, city.city FROM customer c LEFT OUTER JOIN address a ON c.addressId = a.addressId LEFT OUTER JOIN city " +
                "ON a.cityId = city.cityId WHERE " + colName.toLowerCase() + " LIKE \"%" + s.toLowerCase() + "%\"";

        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int customerID = rs.getInt("customerId");
            String customer = rs.getString("customerName");
            Boolean active = rs.getBoolean("active");
            int addressID = rs.getInt("addressId");
            String address = rs.getString("address");
            String address2 = rs.getString("address2");
            int postalCode = rs.getInt("postalCode");
            String phone = rs.getString("phone");
            String city = rs.getString("city");

            Customer newCustomer = new Customer(customerID, customer, active, addressID, address, address2, postalCode, phone, city);
            filteredCustomers.add(newCustomer);
        }
        return filteredCustomers;
    }

    public static int update(Customer customer){
        try {
            String updateStatement = "UPDATE customer c INNER JOIN address a ON c.addressId = a.addressId INNER JOIN city ON a.cityId = city.cityId " +
                "SET c.customerId = ?, c.customerName = ?, c.active = ?, c.addressId = ?, c.lastUpdateBy = ?, a.address = ?, " +
                "a.address2 = ?, a.postalCode = ?, a.phone = ?, city.city = ?, a.lastUpdateBy = ? WHERE customerId = ?";
            DBQuery.setPreparedStatement(conn, updateStatement); //Create prepared statement
            PreparedStatement statement = DBQuery.getPreparedStatement();

            //As many SetStrings needed as there are ? marks
            statement.setInt(1, customer.getCustomerID());
            statement.setString(2, customer.getCustomer());

            if(customer.getActive()) {
                statement.setInt(3, 1);
            }
            else {
                statement.setInt(3, 0);
            }

           statement.setInt(4, customer.getAddressID());
           statement.setString(5, Main.getCurrentUser().getUser());
           statement.setString(6, customer.getAddress());
           statement.setString(7, customer.getAddress2());
           statement.setInt(8, customer.getPostalCode());
           statement.setString(9, customer.getPhone());
           statement.setString(10, customer.getCity());
           statement.setString(11, Main.getCurrentUser().getUser());
           statement.setInt(12, customer.getCustomerID());

            statement.execute();

            //Check rows affected
            return statement.getUpdateCount();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static int delete(Customer customer) {
        try {
            String deleteCustomerStatement = "DELETE FROM customer WHERE customerId = ?";
            DBQuery.setPreparedStatement(conn, deleteCustomerStatement); //Create prepared statement
            PreparedStatement statement = DBQuery.getPreparedStatement();


            //As many SetStrings needed as there are ? marks
            statement.setInt(1, customer.getCustomerID());
            statement.execute();

            String deleteAddressStatement = "DELETE FROM address WHERE address = ?";
            DBQuery.setPreparedStatement(conn, deleteAddressStatement); //Create prepared statement
            PreparedStatement statement2 = DBQuery.getPreparedStatement();

            //As many SetStrings needed as there are ? marks
            statement2.setString(1, customer.getAddress());
            statement2.execute();

            return statement.getUpdateCount();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public static int add(Customer customer){
        try {

            //Needs to be done in two statements
            //Insert into address first
            //Find ID for address and then insert into customer table
            String addressInsertStatement = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            DBQuery.setPreparedStatement(conn, addressInsertStatement); //Create prepared statement
            PreparedStatement addressStatement = DBQuery.getPreparedStatement();

            //As many SetStrings needed as there are ? marks
            addressStatement.setString(1, customer.getAddress());
            addressStatement.setString(2, customer.getAddress2());

            //Convert City to cityID
            //Only return one city so no for loop needed
            ObservableList<City> filteredCity = DBCity.getFiltered("city", customer.getCity());
            addressStatement.setInt(3, filteredCity.get(0).getCityID());

            addressStatement.setInt(4, customer.getPostalCode());
            addressStatement.setString(5, customer.getPhone());

            //Convert ZonedDateTime to string for DB
            ZonedDateTime createDate = ZonedDateTime.now();
            String date = String.valueOf(createDate.toLocalDate());
            String time = String.valueOf(createDate.toLocalTime());
            String dateTime = date + " " + time;


            addressStatement.setString(6, dateTime);//
            addressStatement.setString(7, String.valueOf(Main.getCurrentUser().getUser()));
            addressStatement.setString(8, String.valueOf(Main.getCurrentUser().getUser()));

            addressStatement.execute();

            DBQuery.setStatement(conn);
            Statement statement = DBQuery.getStatement();
            String selectStatement = "SELECT MAX(addressId) FROM address";
            statement.execute(selectStatement);

            ResultSet rs = statement.getResultSet();
            int lastAddress = 0;
            if (rs.next()) {
                lastAddress = rs.getInt(1);
            }

            String customerInsertStatement = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdateBy) VALUES(?, ?, ?, ?, ?, ?)";
            DBQuery.setPreparedStatement(conn, customerInsertStatement); //Create prepared statement
            PreparedStatement customerStatement = DBQuery.getPreparedStatement();

            //As many SetStrings needed as there are ? marks
            customerStatement.setString(1, customer.getCustomer());
            customerStatement.setInt(2, lastAddress);
            if(customer.getActive()) {
                customerStatement.setInt(3, 1);
            }
            else {
                customerStatement.setInt(3, 0);
            }

            customerStatement.setString(4, dateTime);//
            customerStatement.setString(5, String.valueOf(Main.getCurrentUser().getUser()));
            customerStatement.setString(6, String.valueOf(Main.getCurrentUser().getUser()));

            customerStatement.execute();

            return customerStatement.getUpdateCount();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return 0;
    }
}
