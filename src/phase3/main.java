package phase3;

import javafx.application.Application;
import javafx.stage.Stage;
import phase3.LoginController;

public class main extends Application {
    
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("iCell Management System");
        
        LoginController login = new LoginController();
        login.show(primaryStage);
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}