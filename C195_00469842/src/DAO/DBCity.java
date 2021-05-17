/*
 * Data Access Object - city Table
 * Set up to getAll() and getFiltered(), program is not configured to add/delete/update users
 *
 */
package DAO;

import Model.City;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class DBCity{
    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList<City> filteredCities = FXCollections.observableArrayList();
    private static Connection conn = DBConnection.getConnection();

    public static ObservableList<City> getAll() throws SQLException {
        allCities.clear();

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM city";

        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int cityID = rs.getInt("cityId");
            String city = rs.getString("city");
            int countryID = rs.getInt("countryId");

            City newCity = new City(cityID,city,countryID);
            allCities.add(newCity);
        }

        return allCities;
    }

    public static ObservableList<City> getFiltered(String colName, String s) throws SQLException {
        if(!filteredCities.isEmpty()){
            filteredCities.clear();
        }

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM city WHERE " + colName.toLowerCase() + " LIKE \"%" + s.toLowerCase() + "%\"";

        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int cityID = rs.getInt("cityID");
            String city = rs.getString("city");
            int countryID = rs.getInt("countryID");

            City newCity = new City(cityID,city,countryID);
            filteredCities.add(newCity);
        }

            return filteredCities;
    }

}
