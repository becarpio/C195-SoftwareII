package Controller;

import DAO.DBAppointment;
import DAO.DBUser;
import Model.Appointment;
import Model.User;
import Utility.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginController {

    public Label LoginPasswordLbl;
    public Label LoginWelcomeLbl;
    public Label LoginUsernameLbl;
    public PasswordField LoginPassword;
    public TextField LoginUsername;
    public Label LoginFailedLbl; //Hide until login fails, then show
    public Button LoginSubmitBtn;
    public Button LoginExitBtn;
    public GridPane LoginGridPane;

    Stage stage;
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());

    public void LoginSubmit(ActionEvent actionEvent) throws IOException, SQLException {
        //Hide Login Failed text
        LoginGridPane.getChildren().remove(LoginFailedLbl);

        String username = LoginUsername.getText();
        String password = LoginPassword.getText();
        ObservableList<User> allUsers = DBUser.getAll();

        //Check allUsers for a match to username (Case sensitive)
        for(int i = 0; i < allUsers.size(); i++){
            if(allUsers.get(i).getUser().equals(username)){
                if(allUsers.get(i).getPassword().equals(password)){
                    Main.setCurrentUser(allUsers.get(i));
                    break;
                }
            }
        }

        if(Main.getCurrentUser().getUser() == null){
            LoginGridPane.add(LoginFailedLbl,0,1,1,1);
        }else{
            //LOG THE LOGIN
            //File path
            String fileName = "src/Files/userLogins.txt";
            FileWriter fileWriter = new FileWriter(fileName, true);
            PrintWriter outputFile = new PrintWriter(fileWriter);

            outputFile.println("User: " + Main.getCurrentUser().getUser() + "  Time: " + ZonedDateTime.now());
            outputFile.close();

            //CHECK FOR APPOINTMENTS WITHIN 15 MINUTES
            ObservableList<Appointment> allAppointments = DBAppointment.getAll();
            ZonedDateTime startCheck = ZonedDateTime.now();
            ZonedDateTime endCheck = ZonedDateTime.now().plusMinutes(15);

            for(int i = 0; i < allAppointments.size(); i++){
                if(allAppointments.get(i).getStartDateTime().isAfter(startCheck) && allAppointments.get(i).getStartDateTime().isBefore(endCheck)){
                    Alert appointmentWarning = new Alert(Alert.AlertType.INFORMATION);
                    appointmentWarning.setTitle(rb.getString("titleBar"));
                    appointmentWarning.setHeaderText(rb.getString("aptAlert"));
                    appointmentWarning.setContentText(rb.getString("aptText"));
                    appointmentWarning.showAndWait();
                    break;
                }
            }

            //LOAD MAIN
            stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Main.loadNextStage(stage, "../View/mainMenu.fxml");
        }
    }

    public void LoginExit(ActionEvent actionEvent) {
        Main.exitApplication();
    }

    @FXML
    public void initialize() {
        //Set all login screen text based on default location
        //Set Welcome Label Text
        LoginWelcomeLbl.setText(rb.getString("loginWelcome"));
        //Set Failed Text
        LoginFailedLbl.setText(rb.getString("loginFailed"));
        //Set Username Text
        LoginUsernameLbl.setText(rb.getString("loginUsername"));
        //Set Password Text
        LoginPasswordLbl.setText(rb.getString("loginPassword"));
        //Set Submit Text
        LoginSubmitBtn.setText(rb.getString("loginSubmit"));
        //Set Exit Text
        LoginExitBtn.setText(rb.getString("loginExit"));

        //Hide Login Failed text
        LoginGridPane.getChildren().remove(LoginFailedLbl);
    }
}
