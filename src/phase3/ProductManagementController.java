package phase3;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.List;

public class ProductManagementController {

    private User currentUser;
    private List<Product> productDataSource;

    public ProductManagementController(User user, List<Product> products) {
        this.currentUser = user;
        this.productDataSource = products;
    }

    public void show(Stage stage) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        BorderPane rootStructure = new BorderPane();
        rootStructure.setStyle("-fx-background-color: #f4f5fa;");

        // --- TOP APPLICATION HEADER ---
        HBox topNavbar = new HBox();
        topNavbar.setPadding(new Insets(12, 30, 12, 24));
        topNavbar.setAlignment(Pos.CENTER_LEFT);
        topNavbar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ebedf2; -fx-border-width: 0 0 1 0;");

        Button btnBack = new Button();
        FontIcon backIcon = new FontIcon("fth-arrow-left");
        backIcon.setIconSize(16);
        backIcon.setIconColor(Color.web("#4c4e52"));
        btnBack.setGraphic(backIcon);
        btnBack.setStyle("-fx-background-color: #f4f5fa; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnBack.setOnAction(e -> new OwnerDashboardController(currentUser).show(stage));

        StackPane brandLogoWrapper = Logo.createRealLogoHeader(95);

        Label lblViewHeader = new Label("System Repository Log");
        lblViewHeader.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 16));
        lblViewHeader.setStyle("-fx-text-fill: #2c1c3d; -fx-padding: 0 0 0 25;");

        topNavbar.getChildren().addAll(btnBack, brandLogoWrapper, lblViewHeader);
        rootStructure.setTop(topNavbar);

        HBox contentGrid = new HBox(25);
        contentGrid.setPadding(new Insets(25, 30, 30, 30));
        HBox.setHgrow(contentGrid, Priority.ALWAYS);

        // LEFT COLUMN: INPUT SIDEBAR
        VBox controlSidebar = new VBox(14);
        controlSidebar.setMinWidth(300);
        controlSidebar.setMaxWidth(300);
        controlSidebar.setPadding(new Insets(20));
        controlSidebar.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 14px; -fx-border-color: #ebedf2; -fx-border-width: 1px;");

        Label lblSidebarTitle = new Label("PRODUCT PARAMETERS");
        lblSidebarTitle.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        lblSidebarTitle.setStyle("-fx-text-fill: #8a8d93;");

        TextField txtTitle = new TextField(); txtTitle.setPromptText("Product Title Name");
        ComboBox<String> cmbBrand = new ComboBox<>(); cmbBrand.setPromptText("Select Registered Brand");
        cmbBrand.setMaxWidth(Double.MAX_VALUE);
        TextField txtModel = new TextField(); txtModel.setPromptText("Model Specification ID");
        TextField txtColor = new TextField(); txtColor.setPromptText("Device Finish Color");

        HBox specRow = new HBox(10);
        TextField txtRam = new TextField(); txtRam.setPromptText("RAM Capacity"); txtRam.setPrefWidth(125);
        TextField txtStorage = new TextField(); txtStorage.setPromptText("Storage"); txtStorage.setPrefWidth(125);
        specRow.getChildren().addAll(txtRam, txtStorage);

        HBox costRow = new HBox(10);
        TextField txtBuyPrice = new TextField(); txtBuyPrice.setPromptText("Buy Rate Price"); txtBuyPrice.setPrefWidth(125);
        TextField txtSellPrice = new TextField(); txtSellPrice.setPromptText("Sell Revenue Rate"); txtSellPrice.setPrefWidth(125);
        costRow.getChildren().addAll(txtBuyPrice, txtSellPrice);

        ComboBox<String> cmbSupplier = new ComboBox<>(); cmbSupplier.setPromptText("Link Active Supplier Node");
        cmbSupplier.setMaxWidth(Double.MAX_VALUE);

        HBox structuralActionBox = new HBox(12);
        Button btnCommit = new Button("Commit Product");
        btnCommit.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        btnCommit.setStyle("-fx-background-color: #662D91; -fx-text-fill: #ffffff; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnCommit.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnCommit, Priority.ALWAYS);

        Button btnModify = new Button("Modify Item");
        btnModify.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.MEDIUM, 12));
        btnModify.setStyle("-fx-background-color: #f4f5fa; -fx-text-fill: #4c4e52; -fx-background-radius: 6px;");
        structuralActionBox.getChildren().addAll(btnCommit, btnModify);

        controlSidebar.getChildren().addAll(lblSidebarTitle, txtTitle, cmbBrand, txtModel, txtColor, specRow, costRow, cmbSupplier, new Separator(), structuralActionBox);

        // RIGHT COLUMN: SUB-CANVAS DISPLAY OVERVIEW
        VBox dataDisplayContainer = new VBox(15);
        HBox.setHgrow(dataDisplayContainer, Priority.ALWAYS);
        dataDisplayContainer.setPadding(new Insets(24));
        // RESTORED: Unified deep purple display styling matrix
        dataDisplayContainer.setStyle("-fx-background-color: " + Logo.PURPLE_GRADIENT + " -fx-background-radius: 18px;");

        Label lblTableHeading = new Label("ALL INVENTORY ENTRIES LOG");
        lblTableHeading.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        lblTableHeading.setStyle("-fx-text-fill: #ffffff;");

        TableView<Product> ledgerTableView = new TableView<>();
        ledgerTableView.setStyle("-fx-background-radius: 12px; -fx-border-radius: 12px; -fx-background-color: #faf9f6; -fx-font-family: '" + Logo.GLOBAL_FONT + "';");
        VBox.setVgrow(ledgerTableView, Priority.ALWAYS);

        // RESTORED: Every single one of your 11 relational schema columns mapped out explicitly
        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colId.setMaxWidth(60);

        TableColumn<Product, String> colName = new TableColumn<>("Product Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colName.setPrefWidth(150);

        TableColumn<Product, String> colBrand = new TableColumn<>("Brand");
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Product, String> colModel = new TableColumn<>("Model");
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Product, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));

        TableColumn<Product, String> colRam = new TableColumn<>("RAM");
        colRam.setCellValueFactory(new PropertyValueFactory<>("ram"));

        TableColumn<Product, String> colStorage = new TableColumn<>("Storage");
        colStorage.setCellValueFactory(new PropertyValueFactory<>("storage"));

        TableColumn<Product, Double> colBuyPrice = new TableColumn<>("Purchase Cost");
        colBuyPrice.setCellValueFactory(new PropertyValueFactory<>("lastPurchasePrice"));
        colBuyPrice.setCellFactory(c -> new TableCell<>(){
            @Override protected void updateItem(Double item, boolean empty){
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item + " ILS");
            }
        });

        TableColumn<Product, Double> colPrice = new TableColumn<>("Selling Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colPrice.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item + " ILS");
                    setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 13));
                    setStyle("-fx-text-fill: #662D91; -fx-alignment: CENTER-LEFT;");
                }
            }
        });

        TableColumn<Product, Integer> colSupId = new TableColumn<>("Supplier ID");
        colSupId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        TableColumn<Product, String> colSupName = new TableColumn<>("Supplier Vendor");
        colSupName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        ledgerTableView.getColumns().addAll(colId, colName, colBrand, colModel, colColor, colRam, colStorage, colBuyPrice, colPrice, colSupId, colSupName);
        ledgerTableView.setItems(FXCollections.observableArrayList(productDataSource));

        dataDisplayContainer.getChildren().addAll(lblTableHeading, ledgerTableView);
        contentGrid.getChildren().addAll(controlSidebar, dataDisplayContainer);
        rootStructure.setCenter(contentGrid);

        Scene scene = new Scene(rootStructure, 1420, 840);
        stage.setScene(scene);
        stage.show();
    }
}