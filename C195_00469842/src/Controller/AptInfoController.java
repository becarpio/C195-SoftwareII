package Controller;

import Model.Appointment;
import Utility.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AptInfoController {
    public Label AptInfoDate;
    public Label AptInfoStart;
    public Label AptInfoEnd;
    public Label AptInfoCustomer;
    public Label AptInfoContact;
    public Label AptInfoType;
    public Label AptInfoDescription;
    public Label AptInfoLocation;
    public Label AptInfoTitle;

    public Label AptInfoDateLbl;
    public Label AptInfoStartLbl;
    public Label AptInfoEndLbl;
    public Label AptInfoCustLbl;
    public Label AptInfoContactLbl;
    public Label AptInfoTypeLbl;
    public Label AptInfoDescLbl;
    public Label AptInfoLocLbl;
    public Label AptInfoTitleLbl;

    public Button AptInfoBackBtn;

    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());


    public void receiveSelectedAppointment(Appointment appointment){
        AptInfoDateLbl.setText(String.valueOf(appointment.getStartDateTime().toLocalDate()));
        AptInfoStartLbl.setText(String.valueOf(appointment.getStartDateTime().toLocalTime()));
        AptInfoEndLbl.setText(String.valueOf(appointment.getEndDateTime().toLocalDate()));
        AptInfoCustLbl.setText(appointment.getCustomer());
        AptInfoContactLbl.setText(appointment.getContact());
        AptInfoTypeLbl.setText(appointment.getType());
        AptInfoDescLbl.setText(appointment.getDescription());
        AptInfoLocLbl.setText(appointment.getLocation());
        AptInfoTitleLbl.setText(appointment.getTitle());

    }

    public void AptInfoBack(ActionEvent actionEvent) throws IOException {
        //LOAD CUSTOMER LIST SCREEN
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Main.loadNextStage(stage, "../View/mainMenu.fxml");
    }

    public void initialize() throws SQLException {
        AptInfoDate.setText(rb.getString("date"));
        AptInfoStart.setText(rb.getString("start"));
        AptInfoEnd.setText(rb.getString("end"));
        AptInfoType.setText(rb.getString("type"));
        AptInfoDescription.setText(rb.getString("description"));
        AptInfoLocation.setText(rb.getString("location"));
        AptInfoTitle.setText(rb.getString("title"));
        AptInfoBackBtn.setText(rb.getString("Back"));
        AptInfoCustomer.setText(rb.getString("name"));
        AptInfoContact.setText(rb.getString("contact"));
    }

    public void aptInfoExpandCustomer(MouseEvent mouseEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View/customerInfo.fxml"));
        loader.load();

        CustInfoController CIController = loader.getController();
        CIController.receiveSelectedCustomer(AptInfoCustLbl.getText());


        stage = (Stage) ((Node)mouseEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setResizable(false);
        stage.setScene(new Scene(scene));
    }
}
