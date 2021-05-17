package Controller;

import DAO.DBAppointment;
import DAO.DBCustomer;
import DAO.DBUser;
import Model.Appointment;
import Model.Customer;
import Model.User;
import Utility.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportController {
    public Button ReportCustAptBtn;
    public Button ReportMonAptBtn;
    public Button ReportConsultSchBtn;
    public Button ReportGenerateAllBtn;
    public Button ReportBackBtn;

    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());

    public void ReportAlert(){
        Alert reportCompleted = new Alert(Alert.AlertType.INFORMATION);
        reportCompleted.setTitle(rb.getString("titleBar"));
        reportCompleted.setHeaderText(rb.getString("completed"));
        reportCompleted.setContentText(rb.getString("reportText"));
        reportCompleted.showAndWait();
    }

    //Prints Customer name and then each of their appointments
    public void ReportCustApt(ActionEvent actionEvent) throws IOException, SQLException {
        //Create and open text file
        String fileName = "src/Files/AppointmentByCustomer.txt";
        PrintWriter outputFile = new PrintWriter(fileName);
        outputFile.println("APPOINTMENTS BY CUSTOMER");
        outputFile.println("");

        ObservableList<Customer> allCustomers = DBCustomer.getAll();

        //Lambdas used to cycle through the allCustomers and aptsByCustomer fields and print to the text file in place of a for loop
        allCustomers.forEach(c ->{
            outputFile.println(c.getCustomer());
            ObservableList<Appointment> aptsByCustomer = null;
            try {
                aptsByCustomer = DBAppointment.getFiltered("customerId", String.valueOf(c.getCustomerID()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(!aptsByCustomer.isEmpty()) {
                aptsByCustomer.forEach (a-> outputFile.println("Date: " + a.getStartDateTime() + " Title: " + a.getTitle()));
            } else{
                outputFile.println("No Appointments");
            }
            outputFile.println("");
        });

        //Close File
        outputFile.close();
        ReportAlert();
    }

    //Prints the number of appointments by type for each month
    public void ReportMonApt(ActionEvent actionEvent) throws FileNotFoundException, SQLException {
        //Create and open text file
        String fileName = "src/Files/AppointmentTypeByMonth.txt";
        PrintWriter outputFile = new PrintWriter(fileName);
        outputFile.println("COUNT OF APPOINTMENT TYPE BY MONTH");
        outputFile.println("");

        ObservableList<Appointment> allAppointments = DBAppointment.getAll();

        for(int i = 1; i <= 12; i++){
            outputFile.println("Month " + i);
            ArrayList<String> appointmentTypes = new ArrayList<>();
            ArrayList<Integer> appointmentCount = new ArrayList<>();
            for(int j = 0; j < allAppointments.size(); j++){
                if(allAppointments.get(j).getStartDateTime().getMonthValue() == i && !appointmentTypes.contains(allAppointments.get(j).getType())){
                    appointmentTypes.add(allAppointments.get(j).getType());
                    appointmentCount.add(1);
                } else if(allAppointments.get(j).getStartDateTime().getMonthValue() == i && appointmentTypes.contains(allAppointments.get(j).getType())){
                    int index = appointmentTypes.indexOf(allAppointments.get(j).getType());
                    appointmentCount.set(index, appointmentCount.get(index) + 1);
                }
            }

            //Print ArrayList to file
            for(int k = 0; k < appointmentTypes.size(); k++){
                outputFile.println(appointmentTypes.get(k) + ": " + appointmentCount.get(k));
            }

            outputFile.println("");
        }

        //Close File
        outputFile.close();
        ReportAlert();
    }

    //Print the appointments for each consultant
    public void ReportConsultSch(ActionEvent actionEvent) throws FileNotFoundException, SQLException {
        //Create and open text file
        String fileName = "src/Files/AppointmentByConsultant.txt";
        PrintWriter outputFile = new PrintWriter(fileName);
        outputFile.println("APPOINTMENTS BY CONSULTANT");
        outputFile.println("");

        ObservableList<User> allUsers = DBUser.getAll();
        for(int i = 0; i < allUsers.size(); i++){
            outputFile.println(allUsers.get(i).getUser());
            ObservableList<Appointment> aptsByUser = DBAppointment.getFiltered("userId", String.valueOf(allUsers.get(i).getUserID()));
            if(!aptsByUser.isEmpty()) {
                for (int j = 0; j < aptsByUser.size(); j++) {
                    outputFile.println("Date: " + aptsByUser.get(j).getStartDateTime() + " Title: " + aptsByUser.get(j).getTitle());
                }
            } else{
                outputFile.println("No Appointments");
            }
            outputFile.println("");
        }

        //Close File
        outputFile.close();
        ReportAlert();
    }


    public void ReportGenerateAll(ActionEvent actionEvent) throws IOException, SQLException {
        ReportCustApt(actionEvent);
        ReportMonApt(actionEvent);
        ReportConsultSch(actionEvent);
        ReportAlert();

    }

    public void ReportBack(ActionEvent actionEvent) throws IOException {
        //LOAD MAIN
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/mainMenu.fxml");
    }

    @FXML
    public void initialize() {
        //Set all login screen text based on default location
        //Set Customer Appointment Text
        ReportCustAptBtn.setText(rb.getString("aptByCustomer"));
        //Set Appointment by Month Text
        ReportMonAptBtn.setText(rb.getString("aptByMonth"));
        //Set Consultant Schedule Text
        ReportConsultSchBtn.setText(rb.getString("consultantSchedule"));
        //Set Generate All Text
        ReportGenerateAllBtn.setText(rb.getString("generateAll"));
        //Set Back Text
        ReportBackBtn.setText(rb.getString("Back"));

    }
}
