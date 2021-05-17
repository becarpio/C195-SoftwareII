package Controller;

import DAO.DBAppointment;
import DAO.DBCustomer;
import Model.Appointment;
import Model.Customer;
import Utility.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;


public class AptAddModController {

    public Label AptDate;
    public Label AptStart;
    public Label AptCustomer;
    public Label AptContact;
    public Label AptType;
    public Label AptDesc;
    public Label AptEnd;
    public Label AptLoc;
    public Label AptTitle;

    public TextField AptTitleTxt;
    public TextField AptContactTxt;
    public TextField AptCustTxt;
    public TextField AptTypeTxt;
    public TextField AptDescTxt;
    public TextField AptLocTxt;
    public Button AptBackTxt;
    public Button AptSaveBtn;
    public ComboBox AptCustomerBox;
    public DatePicker AptAddDatePicker;
    public ComboBox AptAddModStartHours;
    public ComboBox AptAddModStartMins;
    public ComboBox AptAddModEndHours;
    public ComboBox AptAddModEndMins;

    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
    Appointment updateAppointment = new Appointment();

    public void receiveSelectedAppointment(Appointment appointment) throws SQLException {
        ObservableList<Customer> allCustomers = DBCustomer.getAll();
        updateAppointment = appointment;

        for(Customer c: allCustomers){
            if(c.getCustomer().equals(appointment.getCustomer())){
                AptCustomerBox.getSelectionModel().select(c);
            }
        }


        AptAddDatePicker.setValue(appointment.getStartDateTime().toLocalDate());
        AptAddModStartHours.getSelectionModel().select(String.valueOf(appointment.getStartDateTime().getHour()));
        AptAddModStartMins.getSelectionModel().select(String.valueOf(appointment.getStartDateTime().getMinute()));

        AptAddModEndHours.getSelectionModel().select(String.valueOf(appointment.getEndDateTime().getHour()));
        AptAddModEndMins.getSelectionModel().select(String.valueOf(appointment.getEndDateTime().getMinute()));

        AptTitleTxt.setText(appointment.getTitle());
        AptContactTxt.setText(appointment.getContact());
        AptTypeTxt.setText(appointment.getType());
        AptDescTxt.setText(appointment.getDescription());
        AptLocTxt.setText(appointment.getLocation());
    }

    public void AptSave(ActionEvent actionEvent) throws IOException, SQLException {
        try {
            if (AptAddDatePicker.getValue() == null || AptTitleTxt.getText().equals("") || AptCustomerBox.getValue().equals("") ||
                    AptContactTxt.getText().equals("") || AptTypeTxt.getText().equals("") || AptDescTxt.getText().equals("") ||
                    AptLocTxt.getText().equals("") || AptAddModStartHours.getValue() == null || AptAddModStartMins.getValue() == null ||
                    AptAddModEndHours.getValue() == null || AptAddModEndMins.getValue() == null) {
                Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
                appointmentWarning.setTitle(rb.getString("titleBar"));
                appointmentWarning.setHeaderText(null);
                appointmentWarning.setContentText(rb.getString("nullInfo"));
                appointmentWarning.showAndWait();

            } else {
                ObservableList<Appointment> allAppointments = DBAppointment.getFiltered("start", String.valueOf(AptAddDatePicker.getValue()));


                ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(AptAddDatePicker.getValue().getYear(), AptAddDatePicker.getValue().getMonthValue(),
                        AptAddDatePicker.getValue().getDayOfMonth(), Integer.parseInt(String.valueOf(AptAddModStartHours.getValue())), Integer.parseInt(String.valueOf(AptAddModStartMins.getValue()))), ZoneId.of(TimeZone.getDefault().getID()));
                ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(AptAddDatePicker.getValue().getYear(), AptAddDatePicker.getValue().getMonthValue(),
                        AptAddDatePicker.getValue().getDayOfMonth(), Integer.parseInt(String.valueOf(AptAddModEndHours.getValue())), Integer.parseInt(String.valueOf(AptAddModEndMins.getValue()))), ZoneId.of(TimeZone.getDefault().getID()));

                String title = AptTitleTxt.getText();
                String customer = ((Customer) AptCustomerBox.getValue()).getCustomer();
                String contact = AptContactTxt.getText();
                String type = AptTypeTxt.getText();
                String description = AptDescTxt.getText();
                String location = AptLocTxt.getText();

                boolean appointmentCheck = true;
                for (Appointment a : allAppointments) {
                    if ((updateAppointment.getAppointmentID() != a.getAppointmentID()) &&
                            (!(end.toLocalTime().isBefore(a.getStartDateTime().toLocalTime())
                                    || start.toLocalTime().isAfter(a.getEndDateTime().toLocalTime())))) {
                        appointmentCheck = false;
                        break;
                    }
                }

                //Ensure the end time is not after 5PM and start is not before 8AM
                if (end.toLocalTime().isAfter(LocalTime.parse("17:00:00")) || start.toLocalTime().isBefore(LocalTime.parse("08:00:00"))) {
                    Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
                    appointmentWarning.setTitle(rb.getString("titleBar"));
                    appointmentWarning.setHeaderText(null);
                    appointmentWarning.setContentText(rb.getString("aptWorkingHours"));
                    appointmentWarning.showAndWait();

                    //Ensure that the end time comes after the start time
                } else if (end.toLocalTime().isBefore(start.toLocalTime())) {
                    Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
                    appointmentWarning.setTitle(rb.getString("titleBar"));
                    appointmentWarning.setHeaderText(null);
                    appointmentWarning.setContentText(rb.getString("aptEndBeforeStart"));
                    appointmentWarning.showAndWait();

                    //If there is an appointment overlap (found above) throw an error
                } else if (!appointmentCheck) {
                    Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
                    appointmentWarning.setTitle(rb.getString("titleBar"));
                    appointmentWarning.setHeaderText(null);
                    appointmentWarning.setContentText(rb.getString("aptOverlap"));
                    appointmentWarning.showAndWait();

                    //Check if the appointment is new
                } else if (updateAppointment.getCustomer() == null) {
                    Appointment newAppointment = new Appointment(0, customer, Main.getCurrentUser().getUser(), title, description, location, contact, type, start, end);
                    DBAppointment.add(newAppointment);
                    //LOAD CUSTOMER LIST SCREEN
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Main.loadNextStage(stage, "../View/mainMenu.fxml");

                    //Update the existing appointment
                } else {
                    updateAppointment.setCustomer(customer);
                    updateAppointment.setTitle(title);
                    updateAppointment.setDescription(description);
                    updateAppointment.setLocation(location);
                    updateAppointment.setStartDateTime(start);
                    updateAppointment.setEndDateTime(end);
                    updateAppointment.setContact(contact);
                    updateAppointment.setType(type);

                    DBAppointment.update(updateAppointment);

                    //LOAD CUSTOMER LIST SCREEN
                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Main.loadNextStage(stage, "../View/mainMenu.fxml");
                }
            }
        }
            catch (NullPointerException n){
            Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
            appointmentWarning.setTitle(rb.getString("titleBar"));
            appointmentWarning.setHeaderText(null);
            appointmentWarning.setContentText(rb.getString("nullInfo"));
            appointmentWarning.showAndWait();
        }
    }

    public void AptBack(ActionEvent actionEvent) throws IOException {
        //LOAD CUSTOMER LIST SCREEN
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/mainMenu.fxml");
    }

    public void initialize() throws SQLException {
        AptDate.setText(rb.getString("date"));
        AptStart.setText(rb.getString("start"));
        AptEnd.setText(rb.getString("end"));
        AptType.setText(rb.getString("type"));
        AptDesc.setText(rb.getString("description"));
        AptLoc.setText(rb.getString("location"));
        AptTitle.setText(rb.getString("title"));
        AptBackTxt.setText(rb.getString("Back"));
        AptSaveBtn.setText(rb.getString("save"));
        AptCustomer.setText(rb.getString("name"));
        AptContact.setText(rb.getString("contact"));


        ObservableList<String> hours = FXCollections.observableArrayList();
        hours.addAll("08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17");

        ObservableList<String> minutes = FXCollections.observableArrayList();
        minutes.addAll("00", "15", "30", "45");

        AptAddModStartHours.setItems(hours);
        AptAddModStartMins.setItems(minutes);

        AptAddModEndHours.setItems(hours);
        AptAddModEndMins.setItems(minutes);


        AptCustomerBox.setItems(DBCustomer.getAll());
        AptCustomerBox.setConverter(new StringConverter<Customer>(){
            @Override
            public String toString(Customer customer){
                return customer.getCustomer();
            }
            @Override
            public Customer fromString(String string) {
                return null;
            }
        });
    }
}
