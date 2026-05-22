package phase3;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import phase3.UserDAO;
import phase3.User;

public class LoginController {
    
    private Label lblMessage;
    
    public void show(Stage stage) {
        // الخلفية المتدرجة
        StackPane background = new StackPane();
        background.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");
        
        // البطاقة البيضاء
        VBox card = new VBox(20);
        card.setMaxWidth(400);
        card.setMaxHeight(500);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 10);");
        
        // الشعار
        Label logo = new Label("📱");
        logo.setFont(Font.font(60));
        
        // العنوان
        Label title = new Label("iCell System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#333"));
        
        Label subtitle = new Label("Management System Login");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#888"));
        
        // حقل Username
        Label lblUser = new Label("Username");
        lblUser.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        lblUser.setTextFill(Color.web("#555"));
        
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Enter your username");
        txtUsername.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; "
                          + "-fx-border-color: #ddd; -fx-padding: 10; -fx-font-size: 14px;");
        
        // حقل Password
        Label lblPass = new Label("Password");
        lblPass.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        lblPass.setTextFill(Color.web("#555"));
        
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your password");
        txtPassword.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; "
                          + "-fx-border-color: #ddd; -fx-padding: 10; -fx-font-size: 14px;");
        
        // رسالة
        lblMessage = new Label("");
        lblMessage.setFont(Font.font("Arial", 12));
        
        // زر تسجيل الدخول
        Button btnLogin = new Button("LOGIN");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); "
                       + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; "
                       + "-fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        
        // تأثير hover
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle("-fx-background-color: linear-gradient(to right, #5568d3, #6a3f8e); "
                                                       + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; "
                                                       + "-fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); "
                                                      + "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; "
                                                      + "-fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;"));
        
        // action الزر
        btnLogin.setOnAction(e -> handleLogin(txtUsername.getText(), txtPassword.getText(), stage));
        
        // Enter key
        txtPassword.setOnAction(e -> handleLogin(txtUsername.getText(), txtPassword.getText(), stage));
        
        // Footer
        Label footer = new Label("© 2026 iCell Management System");
        footer.setFont(Font.font("Arial", 10));
        footer.setTextFill(Color.web("#aaa"));
        
        // VBox للحقول
        VBox userBox = new VBox(5, lblUser, txtUsername);
        VBox passBox = new VBox(5, lblPass, txtPassword);
        
        card.getChildren().addAll(logo, title, subtitle, userBox, passBox, lblMessage, btnLogin, footer);
        
        background.getChildren().add(card);
        
        Scene scene = new Scene(background, 900, 650);
        stage.setScene(scene);
        stage.setTitle("iCell - Login");
        stage.show();
    }
    
    private void handleLogin(String username, String password, Stage stage) {
        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("⚠ Please fill all fields");
            lblMessage.setTextFill(Color.web("#e74c3c"));
            return;
        }
        
        UserDAO dao = new UserDAO();
        User user = dao.login(username, password);
        
        if (user != null) {
            lblMessage.setText("✓ Login successful!");
            lblMessage.setTextFill(Color.web("#27ae60"));
            
            // افتح الـ dashboard المناسب حسب الـ User_Type
            openDashboard(user, stage);
            
        } else {
            lblMessage.setText("✗ Invalid username or password");
            lblMessage.setTextFill(Color.web("#e74c3c"));
        }
    }
    
    private void openDashboard(User user, Stage stage) {
        String userType = user.getUserType();
        
        // كلهم رح يستخدموا نفس الـ Dashboard للـ Prototype
        // لكن ممكن لاحقاً يكون لكل واحد dashboard مختلف
        OwnerDashboardController dashboard = new OwnerDashboardController(user);
        dashboard.show(stage);
    }
}