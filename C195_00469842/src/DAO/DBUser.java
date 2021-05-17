/*
 * Data Access Object - user Table
 * Only set up to getAll() and getFiltered(), program is not configured to add/delete/update users
 */

package DAO;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


public class DBUser{

    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static ObservableList<User> filteredUsers = FXCollections.observableArrayList();
    private static Connection conn = DBConnection.getConnection();


    public static ObservableList<User> getAll() throws SQLException {
        allUsers.clear();

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM user";

        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int userID = rs.getInt("userId");
            String user = rs.getString("userName");
            String password = rs.getString("password");
            Boolean active = rs.getBoolean("active");

            User newUser = new User(userID, user, password, active);
            allUsers.add(newUser);
        }
        return allUsers;
    }

    public static ObservableList<User> getFiltered(String col, String s) throws SQLException {
        filteredUsers.clear();

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM user WHERE "+ col +" LIKE \"%" + s + "%\"";

        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int userID = rs.getInt("userId");
            String user = rs.getString("userName");
            String password = rs.getString("password");
            Boolean active = rs.getBoolean("active");

            User newUser = new User(userID, user, password, active);
            filteredUsers.add(newUser);
        }
        return filteredUsers;
    }
}
