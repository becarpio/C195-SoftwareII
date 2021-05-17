package Controller;

import DAO.DBAppointment;
import Model.Appointment;
import Utility.Main;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainMenuController {

    //Radio Toggle Group
    public RadioButton MainWkRdio;
    public RadioButton MainAllRdio;
    public RadioButton MainMthRdio;
    public RadioButton MainDailyRdio;
    public ToggleGroup MainAppointmentGrp;

    public TableView<Appointment> MainAptTbl;
    public TableColumn<Appointment, ZonedDateTime> MainEndCol;
    public TableColumn<Appointment, ZonedDateTime> MainStartCol;
    public TableColumn<Appointment, String> MainCustCol;
    public TableColumn<Appointment, String> MainContactCol;
    public TableColumn<Appointment, String> MainTypeCol;
    public TableColumn<Appointment, String> MainDescriptionCol;
    public TableColumn<Appointment, String> MainLocCol;

    //fxid's related to Actions, Delete if unused
    public Button MainMngCustBtn;
    public Button MainAddAptBtn;
    public Button MainModAptBtn;
    public Button MainDelAptBtn;
    public Button MainExitBtn;
    public Button MainReportBtn;
    public Label MainMenuTitle;


    //Other Variables
    Stage stage = new Stage();
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());

    public void MainMngCust(ActionEvent actionEvent) throws IOException {
        //LOAD CUSTOMER LIST
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/CustomerList.fxml");
    }

    public void MainAddApt(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/appointmentAddModify.fxml"));
        loader.load();

        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setResizable(false);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void MainModApt(ActionEvent actionEvent) throws IOException, SQLException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/appointmentAddModify.fxml"));
            loader.load();

            AptAddModController AAMController = loader.getController();
            AAMController.receiveSelectedAppointment(MainAptTbl.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setResizable(false);
            stage.setScene(new Scene(scene));
            stage.show();
        }
            catch (NullPointerException n){
            Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
            appointmentWarning.setTitle(rb.getString("titleBar"));
            appointmentWarning.setHeaderText(null);
            appointmentWarning.setContentText(rb.getString("selectAppointment"));
            appointmentWarning.showAndWait();
        }
    }

    public void MainDelApt(ActionEvent actionEvent) throws SQLException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    rb.getString("confirmDelete"));
            alert.setHeaderText(null);
            java.util.Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DBAppointment.delete(MainAptTbl.getSelectionModel().getSelectedItem());
            }

            ObservableList<Appointment> allAppointments = DBAppointment.getAll();
            MainAptTbl.setItems(allAppointments);
        }
        catch (NullPointerException n){
            Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
            appointmentWarning.setTitle(rb.getString("titleBar"));
            appointmentWarning.setHeaderText(null);
            appointmentWarning.setContentText(rb.getString("deleteAppointment"));
            appointmentWarning.showAndWait();
        }
    }

    public void MainReport(ActionEvent actionEvent) throws IOException {
        //LOAD REPORT
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/report.fxml");
    }

    public void MainExit(ActionEvent actionEvent) {
        Main.exitApplication();
    }

    public void MainChngButton(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> newDisplay = FXCollections.observableArrayList();
        LocalDate today = ZonedDateTime.now().toLocalDate();

        if(!newDisplay.isEmpty()){
            newDisplay.clear();
        }
        if(MainDailyRdio.isSelected()){
            ObservableList<Appointment> daily = DBAppointment.getFiltered("start", String.valueOf(today).substring(0,10));
            for(Appointment a: daily){
                newDisplay.add(a);
            }
        } else if(MainWkRdio.isSelected()){

            for(int i = 0; i < 7; i++){
                ObservableList<Appointment> daily = DBAppointment.getFiltered("start", String.valueOf(today.plusDays(i)));

                for(Appointment a: daily){
                    newDisplay.add(a);
                }
            }

        } else if(MainMthRdio.isSelected()){
            newDisplay = DBAppointment.getFiltered("start", String.valueOf(today).substring(0, 7));
        } else {
            newDisplay = DBAppointment.getAll();
        }

        MainAptTbl.setItems(newDisplay);

    }


    public void handleMouseClick(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount() == 2) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/appointmentInfo.fxml"));
            loader.load();

            AptInfoController AIController = loader.getController();
            AIController.receiveSelectedAppointment(MainAptTbl.getSelectionModel().getSelectedItem());


            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setResizable(false);
            stage.setScene(new Scene(scene));
        }
    }

    @FXML
    public void initialize() throws SQLException {
        ObservableList<Appointment> allAppointments = DBAppointment.getAll();
        MainAptTbl.setItems(allAppointments);
        MainStartCol.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        MainEndCol.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));

        //Lambda used instead of PropertyValueFactory to pull remaining information for MainAptTbl columns
        //Cycles through each of the entries in allAppointments to populate the table
        MainCustCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getCustomer());
            });
        MainContactCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getContact());
        });
        MainTypeCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getType());
        });
        MainDescriptionCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getDescription());
        });
        MainLocCol.setCellValueFactory(cellData -> {
            return new ReadOnlyStringWrapper(cellData.getValue().getLocation());
        });



        //Set text based on location
        MainMenuTitle.setText(rb.getString("mainTitle"));

        MainWkRdio.setText(rb.getString("weekly"));
        MainAllRdio.setText(rb.getString("all"));
        MainAllRdio.setSelected(true);
        MainMthRdio.setText(rb.getString("monthly"));
        MainDailyRdio.setText(rb.getString("daily"));

        MainEndCol.setText(rb.getString("end"));
        MainStartCol.setText(rb.getString("start"));
        MainCustCol.setText(rb.getString("name"));
        MainContactCol.setText(rb.getString("contact"));
        MainTypeCol.setText(rb.getString("type"));
        MainDescriptionCol.setText(rb.getString("description"));
        MainLocCol.setText(rb.getString("location"));

        MainMngCustBtn.setText(rb.getString("mngCustomer"));
        MainAddAptBtn.setText(rb.getString("addAppointment"));
        MainModAptBtn.setText(rb.getString("modAppointment"));
        MainDelAptBtn.setText(rb.getString("delAppointment"));
        MainExitBtn.setText(rb.getString("loginExit"));
        MainReportBtn.setText(rb.getString("reports"));
    }

}
