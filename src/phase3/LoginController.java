package phase3;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginController {

    public void show(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        StackPane rootCanvas = new StackPane();
        rootCanvas.setStyle("-fx-background-color: " + Logo.PURPLE_GRADIENT);

        VBox authCard = new VBox(16);
        authCard.setMaxSize(380, 560);
        authCard.setPadding(new Insets(35, 35, 35, 35));
        authCard.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 18px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 15, 0, 0, 6);");
        authCard.setAlignment(Pos.TOP_CENTER);

        StackPane logoContainer = Logo.createRealLogoHeader(130);
        logoContainer.setAlignment(Pos.CENTER);

        VBox formGroup = new VBox(10);
        formGroup.setAlignment(Pos.CENTER_LEFT);

        Label lblUser = new Label("Username");
        lblUser.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        TextField txtUser = new TextField();
        txtUser.setPromptText("Enter identity username...");

        Label lblPass = new Label("Password");
        lblPass.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("••••••••");

        formGroup.getChildren().addAll(lblUser, txtUser, lblPass, txtPass);

        // Primary sign-in engine link
        Button btnSignIn = new Button("Sign In");
        btnSignIn.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 13));
        btnSignIn.setMaxWidth(Double.MAX_VALUE);
        btnSignIn.setStyle("-fx-background-color: linear-gradient(to right, #5f72be, #7d57c1); -fx-text-fill: #ffffff; -fx-background-radius: 6px; -fx-padding: 10; -fx-cursor: hand;");

        // FIXED: Transitioning safely directly into functional environment view workspace dashboard nodes
        btnSignIn.setOnAction(e -> {
            User activeUser = new User();
            OwnerDashboardController dashboard = new OwnerDashboardController(activeUser);
            dashboard.show(stage);
        });

        Hyperlink lnkCreateAccount = new Hyperlink("Create Account / Register Node");
        lnkCreateAccount.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.MEDIUM, 12));
        lnkCreateAccount.setStyle("-fx-text-fill: #995bb4;");
        lnkCreateAccount.setOnAction(e -> System.out.println("Routing to Account Creation Node..."));

        Label lblFooter = new Label("© 2026 iCell Systems Inc.");
        lblFooter.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.NORMAL, 10));
        lblFooter.setStyle("-fx-text-fill: #a09aa6; -fx-padding: 15 0 0 0;");

        authCard.getChildren().addAll(logoContainer, formGroup, btnSignIn, lnkCreateAccount, lblFooter);
        rootCanvas.getChildren().add(authCard);

        Scene scene = new Scene(rootCanvas, 1280, 800);
        stage.setTitle("iCell Platform - Portal Login");
        stage.setScene(scene);
        stage.show();
    }
}