/*
 * Data Access Object - Statements and Prepared Statements
 */

package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQuery {

    private static Statement statement; //Statement reference
    private static PreparedStatement preparedStatement; //Statement reference

    //Create Statement Object
    public static void setStatement(Connection conn) throws SQLException {
        statement = conn.createStatement();
    }

    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        preparedStatement = conn.prepareStatement(sqlStatement);
    }

    //Return Statement Object
    public static Statement getStatement(){
        return statement;
    }
    //Return Statement Object
    public static PreparedStatement getPreparedStatement(){
        return preparedStatement;
    }
}
