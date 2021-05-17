package Controller;

import DAO.DBCity;
import DAO.DBCustomer;
import Model.City;
import Model.Customer;
import Utility.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class CustAddModController {

    public Button CustBackBtn;
    public RadioButton CustStatusInactive;
    public ToggleGroup CustStatus;
    public RadioButton CustStatusActive;
    public Label CustNameLbl;
    public Label CustAddressLbl;
    public Label CustCityLbl;
    public Label CustZipLbl;
    public Label CustPhoneLbl;
    public Label CustAddress2Lbl;
    public TextField CustNameTxt;
    public TextField CustAddressTxt;
    public TextField CustAddress2Txt;
    public TextField CustZipTxt;
    public TextField CustPhoneTxt;
    public Button CustSaveBtn;
    public ComboBox CustCityComboBox;

    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
    Customer updateCustomer = new Customer();

    public void receiveSelectedCustomer(Customer customer) throws SQLException {
        ObservableList<City> allCities = DBCity.getAll();


        updateCustomer = customer;

        for(City c: allCities){
            if(c.getCity().equals(customer.getCity())){
                CustCityComboBox.getSelectionModel().select(c);
            }
        }

        CustNameTxt.setText(customer.getCustomer());
        CustAddressTxt.setText(customer.getAddress());
        CustAddress2Txt.setText(customer.getAddress2());
        CustZipTxt.setText(String.valueOf(customer.getPostalCode()));
        CustPhoneTxt.setText(customer.getPhone());

        if(customer.getActive()){
            CustStatusActive.setSelected(true);
        } else{
            CustStatusInactive.setSelected(true);
        }
    }

    public void CustSave(ActionEvent actionEvent) throws IOException {
        try {

            String name;
            String address;
            String address2;
            String city;
            int zip;
            String phone;
            boolean active;

            if(CustNameTxt.getText().equals("") || CustAddressTxt.getText().equals("") || CustCityComboBox.getValue().equals("")
            || CustZipTxt.getText().equals("") ||  CustPhoneTxt.getText().equals("")) {
                Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
                appointmentWarning.setTitle(rb.getString("titleBar"));
                appointmentWarning.setHeaderText(null);
                appointmentWarning.setContentText(rb.getString("nullInfo"));
                appointmentWarning.showAndWait();
            }
            else {
                name = CustNameTxt.getText();
                address = CustAddressTxt.getText();
                address2 = CustAddress2Txt.getText();
                city = ((City) CustCityComboBox.getValue()).getCity();
                zip = Integer.parseInt(CustZipTxt.getText());
                phone = CustPhoneTxt.getText();

                if (CustStatusActive.isSelected()) {
                    active = true;
                } else {
                    active = false;
                }

                if (updateCustomer.getCustomer() == null) {
                    Customer newCustomer = new Customer(0, name, active, 0, address, address2, zip, phone, city);
                    DBCustomer.add(newCustomer);
                } else {
                    updateCustomer.setCustomer(name);
                    updateCustomer.setActive(active);
                    updateCustomer.setAddress(address);
                    updateCustomer.setAddress2(address2);
                    updateCustomer.setPostalCode(zip);
                    updateCustomer.setPhone(phone);
                    updateCustomer.setCity(city);

                    DBCustomer.update(updateCustomer);
                }

                //LOAD CUSTOMER LIST SCREEN
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Main.loadNextStage(stage, "../View/customerList.fxml");
            }
        }
        catch (NullPointerException n){
            Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
            appointmentWarning.setTitle(rb.getString("titleBar"));
            appointmentWarning.setHeaderText(null);
            appointmentWarning.setContentText(rb.getString("nullInfo"));
            appointmentWarning.showAndWait();
        } catch (NumberFormatException i){
            Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
            appointmentWarning.setTitle(rb.getString("titleBar"));
            appointmentWarning.setHeaderText(null);
            appointmentWarning.setContentText(rb.getString("nullInfo"));
            appointmentWarning.showAndWait();
        }
    }

    public void CustBack(ActionEvent actionEvent) throws IOException {
        //LOAD CUSTOMER LIST SCREEN
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/customerList.fxml");
    }

    @FXML
    public void initialize() throws SQLException {
        CustBackBtn.setText(rb.getString("Back"));
        CustNameLbl.setText(rb.getString("name"));
        CustPhoneLbl.setText(rb.getString("phone"));
        CustCityLbl.setText(rb.getString("city"));
        CustStatusActive.setText(rb.getString("active"));
        CustStatusInactive.setText(rb.getString("inactive"));
        CustAddressLbl.setText(rb.getString("address"));
        CustCityLbl.setText(rb.getString("city"));
        CustZipLbl.setText(rb.getString("postalCode"));
        CustAddress2Lbl.setText(rb.getString("address") + " 2");
        CustSaveBtn.setText(rb.getString("save"));
        CustCityComboBox.setItems(DBCity.getAll());
        CustCityComboBox.setConverter(new StringConverter<City> (){
            @Override
            public String toString(City city){
                return city.getCity();
            }
            @Override
            public City fromString(String string) {
                return null;
            }
        });
    }

}
