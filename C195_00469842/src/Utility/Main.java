package Utility;

import DAO.DBConnection;
import Model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
    //Current User information
    //Added once user has been verified via login
    private static User currentUser = new User();

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }


    //Function to change Stages
    public static void loadNextStage(Stage stage, String sceneName) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
        Parent scene;

        scene = FXMLLoader.load(Main.class.getResource(sceneName));
        stage.setTitle(rb.getString("titleBar"));
        stage.setResizable(false);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    //Confirms application exit prior before closing
    public static void exitApplication() {
        ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
        //Confirmation box that the user wishes to exit
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                rb.getString("confirmExit"));
        alert.setHeaderText(null);
        java.util.Optional<ButtonType> result = alert.showAndWait();
        //If they select OK, then exit the application
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    //Loads main screen
    @Override
    public void start(Stage primaryStage) throws Exception{
            ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
            Parent root = FXMLLoader.load(getClass().getResource("../View/login.fxml"));
            primaryStage.setTitle(rb.getString("titleBar"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(true);
            primaryStage.show();
    }

    //Checks default language for application and closes if the language is not supported
    public static void main(String[] args) {
        if(Locale.getDefault().getLanguage().equals("en") ||
                Locale.getDefault().getLanguage().equals("es") ||
                Locale.getDefault().getLanguage().equals("ja")){

        DBConnection.startConnection();
        launch(args);
        DBConnection.endConnection();
        } else {
            System.out.println("Language not supported.");
            System.out.println("Please change your default Windows language");
            System.exit(-1);
        }
    }
}
