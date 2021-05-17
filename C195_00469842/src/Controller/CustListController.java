package Controller;

import DAO.DBAppointment;
import DAO.DBCustomer;
import Model.Appointment;
import Model.Customer;
import Utility.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustListController {
    public TableView <Customer> CustListTbl;
    public TableColumn<Customer, String> CustListNameCol;
    public TableColumn<Customer, String> CustListPhoneCol;
    public TableColumn<Customer, String> CustListCityCol;
    public Button CustListMoreInfoBtn;
    public Button CustListAddBtn;
    public Button CustListModBtn;
    public Button CustListDelBtn;
    public Button CustListBackBtn;


    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());

    public void CustListMoreInfo(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/customerInfo.fxml"));
            loader.load();

            CustInfoController CIController = loader.getController();
            CIController.receiveSelectedCustomer(CustListTbl.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setResizable(false);
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException n){
                Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
                appointmentWarning.setTitle(rb.getString("titleBar"));
                appointmentWarning.setHeaderText(null);
                appointmentWarning.setContentText(rb.getString("selectAppointment"));
                appointmentWarning.showAndWait();
        }
    }

    public void CustListAdd(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/customerAddModify.fxml"));
        loader.load();

        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setResizable(false);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void CustListMod(ActionEvent actionEvent) throws IOException, SQLException {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/customerAddModify.fxml"));
            loader.load();

            CustAddModController CAMController = loader.getController();
            CAMController.receiveSelectedCustomer(CustListTbl.getSelectionModel().getSelectedItem());

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
            appointmentWarning.setContentText(rb.getString("selectCustomer"));
            appointmentWarning.showAndWait();
        }
    }



    public void CustListDel(ActionEvent actionEvent) throws SQLException {
        try{
            ObservableList<Appointment> filteredCustomers = DBAppointment.getFiltered("customerId", String.valueOf(CustListTbl.getSelectionModel().getSelectedItem().getCustomerID()));

            if(filteredCustomers.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        rb.getString("confirmDelete"));
                alert.setHeaderText(null);
                java.util.Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DBCustomer.delete(CustListTbl.getSelectionModel().getSelectedItem());
                }
                CustListTbl.setItems(DBCustomer.getAll());
            } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        rb.getString("denyCustomerDelete"));
                alert.setHeaderText(null);
                java.util.Optional<ButtonType> result = alert.showAndWait();
            }
        }
            catch (NullPointerException n){
            Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
            appointmentWarning.setTitle(rb.getString("titleBar"));
            appointmentWarning.setHeaderText(null);
            appointmentWarning.setContentText(rb.getString("deleteCustomer"));
            appointmentWarning.showAndWait();
        }
    }

    public void CustListBack(ActionEvent actionEvent) throws IOException {
        //LOAD MAIN
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/mainMenu.fxml");
    }
    @FXML
    public void initialize() throws SQLException {
        CustListMoreInfoBtn.setText(rb.getString("moreInfo"));
        CustListModBtn.setText(rb.getString("modify"));
        CustListAddBtn.setText(rb.getString("add"));
        CustListDelBtn.setText(rb.getString("delete"));
        CustListBackBtn.setText(rb.getString("Back"));
        CustListNameCol.setText(rb.getString("name"));
        CustListPhoneCol.setText(rb.getString("phone"));
        CustListCityCol.setText(rb.getString("city"));

        CustListTbl.setItems(DBCustomer.getAll());
        CustListNameCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        CustListPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        CustListCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));

    }
}
