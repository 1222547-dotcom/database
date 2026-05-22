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
import phase3.User;

public class OwnerDashboardController {
    
    private User currentUser;
    
    public OwnerDashboardController(User user) {
        this.currentUser = user;
    }
    
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f6fa;");
        
        // ===== Top Header =====
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2);");
        
        Label headerTitle = new Label("📱 iCell Management System");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        headerTitle.setTextFill(Color.WHITE);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label userInfo = new Label("👤 " + currentUser.getUsername() + " (" + currentUser.getUserType() + ")");
        userInfo.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        userInfo.setTextFill(Color.WHITE);
        
        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; "
                        + "-fx-padding: 8 20; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        btnLogout.setOnAction(e -> {
            LoginController login = new LoginController();
            login.show(stage);
        });
        
        header.getChildren().addAll(headerTitle, spacer, userInfo, new Label("  "), btnLogout);
        
        // ===== Main Content =====
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f5f6fa; -fx-background-color: #f5f6fa;");
        
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        
        // Welcome
        Label welcome = new Label("Welcome back, " + 
            (currentUser.getFullName().trim().isEmpty() ? currentUser.getUsername() : currentUser.getFullName()) + "! 👋");
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        welcome.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Here's what's happening in your system today");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));
        
        // ===== Statistics Cards =====
        Label statsTitle = new Label("📊 Statistics Overview");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        statsTitle.setTextFill(Color.web("#2c3e50"));
        
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
            createStatCard("💰", "Total Revenue", "425,000 ILS", "#3498db"),
            createStatCard("📦", "Stock Value", "150,000 ILS", "#2ecc71"),
            createStatCard("📊", "Monthly Sales", "95,000 ILS", "#e67e22"),
            createStatCard("⚠️", "Low Stock", "3 Items", "#e74c3c")
        );
        
        // ===== Main Menu =====
        Label menuTitle = new Label("🎯 Main Menu");
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        menuTitle.setTextFill(Color.web("#2c3e50"));
        
        GridPane menu = new GridPane();
        menu.setHgap(20);
        menu.setVgap(20);
        
        menu.add(createMenuButton("📦", "Products", "Manage products inventory", "#3498db", () -> openProducts(stage)), 0, 0);
        menu.add(createMenuButton("👥", "Staff", "Manage employees", "#9b59b6", () -> showAlert("Coming Soon", "Staff Management")), 1, 0);
        menu.add(createMenuButton("📄", "Invoices", "View & manage invoices", "#e67e22", () -> showAlert("Coming Soon", "Invoices")), 2, 0);
        menu.add(createMenuButton("🏢", "Warehouses", "Manage warehouses", "#1abc9c", () -> showAlert("Coming Soon", "Warehouses")), 3, 0);
        
        menu.add(createMenuButton("💾", "Suppliers", "Manage suppliers", "#34495e", () -> showAlert("Coming Soon", "Suppliers")), 0, 1);
        menu.add(createMenuButton("🏪", "Shops", "Client shops", "#16a085", () -> showAlert("Coming Soon", "Shops")), 1, 1);
        menu.add(createMenuButton("📊", "Reports", "View analytics", "#2980b9", () -> showAlert("Coming Soon", "Reports")), 2, 1);
        menu.add(createMenuButton("⚙️", "Settings", "System settings", "#7f8c8d", () -> showAlert("Coming Soon", "Settings")), 3, 1);
        
        content.getChildren().addAll(welcome, subtitle, statsTitle, statsRow, menuTitle, menu);
        scroll.setContent(content);
        
        root.setTop(header);
        root.setCenter(scroll);
        
        Scene scene = new Scene(root, 1200, 750);
        stage.setScene(scene);
        stage.setTitle("iCell - " + currentUser.getUserType() + " Dashboard");
        stage.show();
    }
    
    private VBox createStatCard(String icon, String title, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4); "
                    + "-fx-border-color: " + color + "; -fx-border-width: 0 0 0 4; -fx-border-radius: 12;");
        HBox.setHgrow(card, Priority.ALWAYS);
        
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(24));
        
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Arial", 12));
        titleLbl.setTextFill(Color.web("#7f8c8d"));
        
        Label valueLbl = new Label(value);
        valueLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        valueLbl.setTextFill(Color.web(color));
        
        card.getChildren().addAll(iconLbl, titleLbl, valueLbl);
        return card;
    }
    
    private VBox createMenuButton(String icon, String title, String desc, String color, Runnable action) {
        VBox btn = new VBox(8);
        btn.setAlignment(Pos.CENTER);
        btn.setPadding(new Insets(25));
        btn.setPrefSize(220, 130);
        btn.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                  + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4); "
                  + "-fx-cursor: hand;");
        
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font(40));
        
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLbl.setTextFill(Color.web(color));
        
        Label descLbl = new Label(desc);
        descLbl.setFont(Font.font("Arial", 11));
        descLbl.setTextFill(Color.web("#7f8c8d"));
        
        btn.getChildren().addAll(iconLbl, titleLbl, descLbl);
        
        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 12; "
                                              + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 6); -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                                             + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4); -fx-cursor: hand;"));
        
        btn.setOnMouseClicked(e -> action.run());
        
        return btn;
    }
    
    private void openProducts(Stage stage) {
        ProductManagementController pm = new ProductManagementController(currentUser);
        pm.show(stage);
    }
    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg + "\n\nThis feature will be available in Phase 4.");
        alert.showAndWait();
    }
}