package Controller;

import DAO.DBCustomer;
import Model.Customer;
import Utility.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustInfoController {
    public Label CustInfoNameLbl;
    public Label CustInfoAddressLbl;
    public Label CustInfoCityLbl;
    public Label CustInfoZipLbl;
    public Label CustInfoPhoneLbl;
    public Label CustInfoAddress2Lbl;
    public Label CustInfoActiveLbl;
    public Button CustInfoBackBtn;
    public Label CustInfoActive;
    public Label CustInfoAddress2;
    public Label CustInfoPhone;
    public Label CustInfoZip;
    public Label CustInfoCity;
    public Label CustInfoAddress;
    public Label CustInfoName;

    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());

    public void receiveSelectedCustomer(Customer customer){
        CustInfoNameLbl.setText(customer.getCustomer());
        CustInfoAddressLbl.setText(customer.getAddress());
        CustInfoAddress2Lbl.setText(customer.getAddress2());
        CustInfoCityLbl.setText(customer.getCity());
        CustInfoZipLbl.setText(String.valueOf(customer.getPostalCode()));
        CustInfoPhoneLbl.setText(customer.getPhone());
        CustInfoActiveLbl.setText(String.valueOf(customer.getActive()));
    }
    public void receiveSelectedCustomer(String customerName) throws SQLException {
        ObservableList<Customer> customerList = DBCustomer.getFiltered("customerName", customerName);

        CustInfoNameLbl.setText(customerList.get(0).getCustomer());
        CustInfoAddressLbl.setText(customerList.get(0).getAddress());
        CustInfoAddress2Lbl.setText(customerList.get(0).getAddress2());
        CustInfoCityLbl.setText(customerList.get(0).getCity());
        CustInfoZipLbl.setText(String.valueOf(customerList.get(0).getPostalCode()));
        CustInfoPhoneLbl.setText(customerList.get(0).getPhone());
        CustInfoActiveLbl.setText(String.valueOf(customerList.get(0).getActive()));
    }

    public void CustInfoBack(ActionEvent actionEvent) throws IOException {
        //LOAD CUSTOMER LIST SCREEN
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/mainMenu.fxml");
    }

    @FXML
    public void initialize() throws SQLException {
        CustInfoBackBtn.setText(rb.getString("Back"));
        CustInfoName.setText(rb.getString("name"));
        CustInfoPhone.setText(rb.getString("phone"));
        CustInfoCity.setText(rb.getString("city"));
        CustInfoAddress.setText(rb.getString("address"));
        CustInfoAddress2.setText(rb.getString("address") + " 2");
        CustInfoActive.setText(rb.getString("active"));
        CustInfoZip.setText(rb.getString("postalCode"));
    }


}
