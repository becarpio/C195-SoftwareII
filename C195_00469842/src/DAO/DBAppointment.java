/*
 * Data Access Object - appointment Table
 * Set up to getAll, filteredList, add, delete, and update
 *
 *
 *
 *
 *
 */

package DAO;

import Model.Appointment;
import Model.Customer;
import Model.User;
import Utility.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.util.TimeZone;

public class DBAppointment{
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
    private static Connection conn = DBConnection.getConnection();

    public static ObservableList<Appointment> getAll() throws SQLException {
        allAppointments.clear();

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM appointment a LEFT OUTER JOIN customer c ON c.customerId = a.customerId " +
                "LEFT OUTER JOIN user u ON u.userId = a.userId";


        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int appointmentID = rs.getInt("appointmentId");
            String customer = rs.getString("customerName");
            String user = rs.getString("userName");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");

            //Pull Start Date/Time and convert to local time
            LocalDate startDate = rs.getDate("start").toLocalDate();
            LocalTime startTime = rs.getTime("start").toLocalTime();
            ZonedDateTime startDateTime = ZonedDateTime.of(startDate,startTime, ZoneId.of(TimeZone.getDefault().getID()));

            //Pull End Date/Time and convert to local time.
            LocalDate endDate = rs.getDate("end").toLocalDate();
            LocalTime endTime = rs.getTime("end").toLocalTime();
            ZonedDateTime endDateTime = ZonedDateTime.of(endDate,endTime, ZoneId.of(TimeZone.getDefault().getID()));


            if(ZoneId.of(TimeZone.getDefault().getID()).getRules().isDaylightSavings(ZonedDateTime.now().toInstant())){
                startDateTime = startDateTime.plusHours(1);
                endDateTime = endDateTime.plusHours(1);
            }

            Appointment newAppointment = new Appointment(appointmentID, customer, user, title, description, location, contact, type,
                    startDateTime, endDateTime);
            allAppointments.add(newAppointment);
        }

        return allAppointments;
    }

    public static ObservableList<Appointment> getFiltered(String colName, String s) throws SQLException {
        if(!filteredAppointments.isEmpty()){
            filteredAppointments.clear();
        }

        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        String selectStatement = "SELECT * FROM appointment a LEFT OUTER JOIN customer c ON c.customerId = a.customerId " +
                "LEFT OUTER JOIN user u ON u.userId = a.userId WHERE a." + colName + " LIKE \"%" + s.toLowerCase() + "%\"";


        statement.execute(selectStatement);
        ResultSet rs = statement.getResultSet();
        while(rs.next()){
            int appointmentID = rs.getInt("appointmentId");
            String customer = rs.getString("customerName");
            String user = rs.getString("userName");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");

            //Pull Start Date/Time and convert to local time
            LocalDate startDate = rs.getDate("start").toLocalDate();
            LocalTime startTime = rs.getTime("start").toLocalTime();
            ZonedDateTime startDateTime = ZonedDateTime.of(startDate,startTime, ZoneId.of(TimeZone.getDefault().getID()));

            //Pull End Date/Time and convert to local time.
            LocalDate endDate = rs.getDate("end").toLocalDate();
            LocalTime endTime = rs.getTime("end").toLocalTime();
            ZonedDateTime endDateTime = ZonedDateTime.of(endDate,endTime, ZoneId.of(TimeZone.getDefault().getID()));


            if(ZoneId.of(TimeZone.getDefault().getID()).getRules().isDaylightSavings(ZonedDateTime.now().toInstant())){
                startDateTime = startDateTime.plusHours(1);
                endDateTime = endDateTime.plusHours(1);
            }

            Appointment newAppointment = new Appointment(appointmentID, customer, user, title, description, location, contact, type,
                    startDateTime, endDateTime);
            filteredAppointments.add(newAppointment);
        }

        return filteredAppointments;
    }

    public static int update(Appointment appointment){
        try {
            String updateStatement = "UPDATE appointment SET customerId = ?, userId = ?, title = ?, " +
                    "description = ?, location = ?, contact = ?, type = ?, " +
                    "start = ?, end = ?, lastUpdateBy = ? WHERE appointmentId = ?";
            DBQuery.setPreparedStatement(conn, updateStatement); //Create prepared statement
            PreparedStatement statement = DBQuery.getPreparedStatement();

            //As many SetStrings needed as there are ? marks

            //Convert customerName to customerId
            ObservableList<Customer> filteredCustomers = DBCustomer.getFiltered("customerName", appointment.getCustomer());
            statement.setString(1, String.valueOf(filteredCustomers.get(0).getCustomerID()));

            //Convert userName to userId
            ObservableList<User> filteredUsers = DBUser.getFiltered("userName", appointment.getUser());
            statement.setString(2, String.valueOf(filteredUsers.get(0).getUserID()));


            statement.setString(3, appointment.getTitle());//
            statement.setString(4, appointment.getDescription());
            statement.setString(5, appointment.getLocation());
            statement.setString(6, appointment.getContact());
            statement.setString(7, appointment.getType());//

            String dateStart = String.valueOf(LocalDateTime.ofInstant(appointment.getStartDateTime().toInstant(), ZoneId.of("UTC")).toLocalDate());
            String timeStart = String.valueOf(LocalDateTime.ofInstant(appointment.getStartDateTime().toInstant(), ZoneId.of("UTC")).toLocalTime());
            String startDate = dateStart + " " + timeStart;
            statement.setString(8, startDate);

            //Convert ZonedDateTime to string End Date for DB
            String dateEnd = String.valueOf(LocalDateTime.ofInstant(appointment.getEndDateTime().toInstant(), ZoneId.of("UTC")).toLocalDate());
            String timeEnd = String.valueOf(LocalDateTime.ofInstant(appointment.getEndDateTime().toInstant(), ZoneId.of("UTC")).toLocalTime());
            String endDate = dateEnd + " " + timeEnd;
            statement.setString(9, endDate);

            statement.setString(10, Main.getCurrentUser().getUser());

            statement.setInt(11, appointment.getAppointmentID());


            statement.execute();

            //Check rows affected
            return statement.getUpdateCount();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static int delete(Appointment appointment){
        try {
            String deleteStatement = "DELETE FROM appointment WHERE appointmentId = ?";
            DBQuery.setPreparedStatement(conn, deleteStatement); //Create prepared statement
            PreparedStatement statement = DBQuery.getPreparedStatement();

            //As many SetStrings needed as there are ? marks
            statement.setInt(1, appointment.getAppointmentID());

            statement.execute();

            return statement.getUpdateCount();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static int add(Appointment appointment){
        try {
            String insertStatement = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, start, end, createDate, createdBy, lastUpdateBy, url) " +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '')";
            DBQuery.setPreparedStatement(conn, insertStatement); //Create prepared statement
            PreparedStatement statement = DBQuery.getPreparedStatement();

            //As many SetStrings needed as there are ? marks

            //Convert customerName to customerId
            ObservableList<Customer> filteredCustomers = DBCustomer.getFiltered("customerName", appointment.getCustomer());
            statement.setString(1, String.valueOf(filteredCustomers.get(0).getCustomerID()));

            //Convert userName to userId
            ObservableList<User> filteredUsers = DBUser.getFiltered("userName", appointment.getUser());
            statement.setString(2, String.valueOf(filteredUsers.get(0).getUserID()));

            statement.setString(3, appointment.getTitle());//
            statement.setString(4, appointment.getDescription());
            statement.setString(5, appointment.getLocation());
            statement.setString(6, appointment.getContact());
            statement.setString(7, appointment.getType());//

            //Convert from default TimeZone to GMT
            Instant startGMT = appointment.getStartDateTime().toInstant();

            //Convert ZonedDateTime to string Start Date for DB

            String dateStart = String.valueOf(LocalDateTime.ofInstant(appointment.getStartDateTime().toInstant(), ZoneId.of("UTC")).toLocalDate());
            String timeStart = String.valueOf(LocalDateTime.ofInstant(appointment.getStartDateTime().toInstant(), ZoneId.of("UTC")).toLocalTime());
            String startDate = dateStart + " " + timeStart;
            statement.setString(8, startDate);

            //Convert ZonedDateTime to string End Date for DB
            String dateEnd = String.valueOf(LocalDateTime.ofInstant(appointment.getEndDateTime().toInstant(), ZoneId.of("UTC")).toLocalDate());
            String timeEnd = String.valueOf(LocalDateTime.ofInstant(appointment.getEndDateTime().toInstant(), ZoneId.of("UTC")).toLocalTime());
            String endDate = dateEnd + " " + timeEnd;
            statement.setString(9, endDate);

            //Convert ZonedDateTime to Create Date string for DB
            ZonedDateTime createDate = ZonedDateTime.now();
            String dateCreate = String.valueOf(LocalDateTime.ofInstant(createDate.toInstant(), ZoneId.of("UTC")).toLocalDate());
            String timeCreate = String.valueOf(LocalDateTime.ofInstant(createDate.toInstant(), ZoneId.of("UTC")).toLocalTime());
            String CreateDate = dateCreate + " " + timeCreate;
            statement.setString(10, CreateDate);

            statement.setString(11, Main.getCurrentUser().getUser());
            statement.setString(12, Main.getCurrentUser().getUser());

            statement.execute();
            return statement.getUpdateCount();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return 0;
    }
}
