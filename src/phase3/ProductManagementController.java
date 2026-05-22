package phase3;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import phase3.ProductDAO;
import phase3.Product;
import phase3.User;

import java.util.List;

public class ProductManagementController {
    
    private User currentUser;
    private ProductDAO dao;
    private TableView<Product> table;
    private ObservableList<Product> productList;
    private List<String[]> suppliers;
    
    // Form fields
    private TextField txtName, txtModel, txtColor, txtRam, txtStorage, txtPurchase, txtSelling;
    private ComboBox<String> cmbBrand, cmbSupplier;
    private TextField txtSearch;
    private Label lblMessage;
    private int selectedProductId = -1;
    
    public ProductManagementController(User user) {
        this.currentUser = user;
        this.dao = new ProductDAO();
    }
    
    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f6fa;");
        
        // ===== Header =====
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2);");
        
        Button btnBack = new Button("← Back");
        btnBack.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; "
                      + "-fx-padding: 8 18; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        btnBack.setOnAction(e -> {
            OwnerDashboardController dashboard = new OwnerDashboardController(currentUser);
            dashboard.show(stage);
        });
        
        Label title = new Label(" Product Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 0, 20));
        
        header.getChildren().addAll(btnBack, title);
        
        // ===== Center: Split into Form (Left) + Table (Right) =====
        HBox centerBox = new HBox(20);
        centerBox.setPadding(new Insets(20));
        
        // ----- Left: Form -----
        VBox formBox = createForm();
        formBox.setPrefWidth(350);
        
        // ----- Right: Table + Search -----
        VBox tableBox = createTableSection();
        HBox.setHgrow(tableBox, Priority.ALWAYS);
        
        centerBox.getChildren().addAll(formBox, tableBox);
        
        root.setTop(header);
        root.setCenter(centerBox);
        
        Scene scene = new Scene(root, 1300, 750);
        stage.setScene(scene);
        stage.setTitle("iCell - Product Management");
        stage.show();
        
        // تحميل المنتجات
        loadProducts();
        loadSuppliers();
    }
    
    private VBox createForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(25));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                   + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");
        
        Label formTitle = new Label("Product Details");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        formTitle.setTextFill(Color.web("#2c3e50"));
        
        txtName = createTextField("Product Name");
        cmbBrand = new ComboBox<>();
        cmbBrand.getItems().addAll("Samsung", "Apple", "Xiaomi", "Huawei", "Oppo", "Other");
        cmbBrand.setPromptText("Select Brand");
        cmbBrand.setMaxWidth(Double.MAX_VALUE);
        cmbBrand.setStyle("-fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #ddd;");
        
        txtModel = createTextField("Model (e.g., S24 Ultra)");
        txtColor = createTextField("Color");
        
        HBox specsBox = new HBox(10);
        txtRam = createTextField("RAM (e.g., 8GB)");
        txtStorage = createTextField("Storage (e.g., 256GB)");
        HBox.setHgrow(txtRam, Priority.ALWAYS);
        HBox.setHgrow(txtStorage, Priority.ALWAYS);
        specsBox.getChildren().addAll(txtRam, txtStorage);
        
        HBox priceBox = new HBox(10);
        txtPurchase = createTextField("Purchase Price");
        txtSelling = createTextField("Selling Price");
        HBox.setHgrow(txtPurchase, Priority.ALWAYS);
        HBox.setHgrow(txtSelling, Priority.ALWAYS);
        priceBox.getChildren().addAll(txtPurchase, txtSelling);
        
        cmbSupplier = new ComboBox<>();
        cmbSupplier.setPromptText("Select Supplier");
        cmbSupplier.setMaxWidth(Double.MAX_VALUE);
        cmbSupplier.setStyle("-fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #ddd;");
        
        lblMessage = new Label("");
        lblMessage.setFont(Font.font("Arial", 12));
        
        // Buttons
        Button btnAdd = createActionButton("➕ Add", "#27ae60");
        Button btnUpdate = createActionButton(" Update", "#f39c12");
        Button btnDelete = createActionButton(" Delete", "#e74c3c");
        Button btnClear = createActionButton(" Clear", "#95a5a6");
        
        btnAdd.setOnAction(e -> handleAdd());
        btnUpdate.setOnAction(e -> handleUpdate());
        btnDelete.setOnAction(e -> handleDelete());
        btnClear.setOnAction(e -> clearForm());
        
        HBox btnRow1 = new HBox(10, btnAdd, btnUpdate);
        HBox btnRow2 = new HBox(10, btnDelete, btnClear);
        HBox.setHgrow(btnAdd, Priority.ALWAYS);
        HBox.setHgrow(btnUpdate, Priority.ALWAYS);
        HBox.setHgrow(btnDelete, Priority.ALWAYS);
        HBox.setHgrow(btnClear, Priority.ALWAYS);
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnUpdate.setMaxWidth(Double.MAX_VALUE);
        btnDelete.setMaxWidth(Double.MAX_VALUE);
        btnClear.setMaxWidth(Double.MAX_VALUE);
        
        form.getChildren().addAll(formTitle, txtName, cmbBrand, txtModel, txtColor, 
                                  specsBox, priceBox, cmbSupplier, lblMessage, btnRow1, btnRow2);
        
        return form;
    }
    
    private VBox createTableSection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                      + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");
        
        Label tableTitle = new Label("All Products");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tableTitle.setTextFill(Color.web("#2c3e50"));
        
        // Search bar
        HBox searchBox = new HBox(10);
        txtSearch = new TextField();
        txtSearch.setPromptText(" Search by name, brand, or model...");
        txtSearch.setStyle("-fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #ddd; -fx-padding: 8;");
        HBox.setHgrow(txtSearch, Priority.ALWAYS);
        
        Button btnSearch = createActionButton("Search", "#3498db");
        Button btnRefresh = createActionButton(" Refresh", "#16a085");
        
        btnSearch.setOnAction(e -> handleSearch());
        btnRefresh.setOnAction(e -> {
            txtSearch.clear();
            loadProducts();
        });
        
        searchBox.getChildren().addAll(txtSearch, btnSearch, btnRefresh);
        
        // Table
        table = new TableView<>();
        productList = FXCollections.observableArrayList();
        table.setItems(productList);
        table.setStyle("-fx-background-radius: 8;");
        
        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colId.setPrefWidth(50);
        
        TableColumn<Product, String> colName = new TableColumn<>("Product Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colName.setPrefWidth(180);
        
        TableColumn<Product, String> colBrand = new TableColumn<>("Brand");
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colBrand.setPrefWidth(90);
        
        TableColumn<Product, String> colModel = new TableColumn<>("Model");
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colModel.setPrefWidth(110);
        
        TableColumn<Product, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colColor.setPrefWidth(80);
        
        TableColumn<Product, Double> colPrice = new TableColumn<>("Selling Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colPrice.setPrefWidth(110);
        
        TableColumn<Product, String> colSupplier = new TableColumn<>("Supplier");
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colSupplier.setPrefWidth(110);
        
        table.getColumns().addAll(colId, colName, colBrand, colModel, colColor, colPrice, colSupplier);
        
        // Click row to fill form
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillForm(newVal);
            }
        });
        
        VBox.setVgrow(table, Priority.ALWAYS);
        section.getChildren().addAll(tableTitle, searchBox, table);
        
        return section;
    }
    
    private TextField createTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-radius: 6; -fx-border-radius: 6; -fx-border-color: #ddd; -fx-padding: 8;");
        return tf;
    }
    
    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; "
                  + "-fx-font-weight: bold; -fx-padding: 10 18; -fx-background-radius: 6; -fx-cursor: hand;");
        return btn;
    }
    
    private void loadProducts() {
        productList.clear();
        productList.addAll(dao.getAllProducts());
    }
    
    private void loadSuppliers() {
        suppliers = dao.getSuppliers();
        cmbSupplier.getItems().clear();
        for (String[] s : suppliers) {
            cmbSupplier.getItems().add(s[1]);
        }
    }
    
    private int getSupplierIdByName(String name) {
        if (name == null) return 0;
        for (String[] s : suppliers) {
            if (s[1].equals(name)) return Integer.parseInt(s[0]);
        }
        return 0;
    }
    
    private void handleAdd() {
        if (!validateForm()) return;
        
        Product p = buildProductFromForm();
        if (dao.addProduct(p)) {
            showMessage("✓ Product added successfully!", "#27ae60");
            clearForm();
            loadProducts();
        } else {
            showMessage("✗ Failed to add product", "#e74c3c");
        }
    }
    
    private void handleUpdate() {
        if (selectedProductId == -1) {
            showMessage("⚠ Select a product from the table first", "#f39c12");
            return;
        }
        if (!validateForm()) return;
        
        Product p = buildProductFromForm();
        p.setProductId(selectedProductId);
        
        if (dao.updateProduct(p)) {
            showMessage("✓ Product updated successfully!", "#27ae60");
            clearForm();
            loadProducts();
        } else {
            showMessage("✗ Failed to update product", "#e74c3c");
        }
    }
    
    private void handleDelete() {
        if (selectedProductId == -1) {
            showMessage("⚠ Select a product from the table first", "#f39c12");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Product");
        confirm.setContentText("Are you sure you want to delete this product?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            if (dao.deleteProduct(selectedProductId)) {
                showMessage("✓ Product deleted successfully!", "#27ae60");
                clearForm();
                loadProducts();
            } else {
                showMessage("✗ Failed to delete (may be referenced)", "#e74c3c");
            }
        }
    }
    
    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadProducts();
            return;
        }
        productList.clear();
        productList.addAll(dao.searchProducts(keyword));
    }
    
    private boolean validateForm() {
        if (txtName.getText().isEmpty() || cmbBrand.getValue() == null 
            || txtSelling.getText().isEmpty()) {
            showMessage("⚠ Name, Brand, and Selling Price are required", "#f39c12");
            return false;
        }
        try {
            if (!txtPurchase.getText().isEmpty()) Double.parseDouble(txtPurchase.getText());
            Double.parseDouble(txtSelling.getText());
        } catch (NumberFormatException e) {
            showMessage("⚠ Prices must be valid numbers", "#f39c12");
            return false;
        }
        return true;
    }
    
    private Product buildProductFromForm() {
        Product p = new Product();
        p.setProductName(txtName.getText());
        p.setBrand(cmbBrand.getValue());
        p.setModel(txtModel.getText());
        p.setColor(txtColor.getText());
        p.setRam(txtRam.getText());
        p.setStorage(txtStorage.getText());
        p.setLastPurchasePrice(txtPurchase.getText().isEmpty() ? 0 : Double.parseDouble(txtPurchase.getText()));
        p.setSellingPrice(Double.parseDouble(txtSelling.getText()));
        p.setSupplierId(getSupplierIdByName(cmbSupplier.getValue()));
        return p;
    }
    
    private void fillForm(Product p) {
        selectedProductId = p.getProductId();
        txtName.setText(p.getProductName());
        cmbBrand.setValue(p.getBrand());
        txtModel.setText(p.getModel());
        txtColor.setText(p.getColor());
        txtRam.setText(p.getRam());
        txtStorage.setText(p.getStorage());
        txtPurchase.setText(String.valueOf(p.getLastPurchasePrice()));
        txtSelling.setText(String.valueOf(p.getSellingPrice()));
        cmbSupplier.setValue(p.getSupplierName());
    }
    
    private void clearForm() {
        selectedProductId = -1;
        txtName.clear(); txtModel.clear(); txtColor.clear();
        txtRam.clear(); txtStorage.clear();
        txtPurchase.clear(); txtSelling.clear();
        cmbBrand.setValue(null);
        cmbSupplier.setValue(null);
        lblMessage.setText("");
        table.getSelectionModel().clearSelection();
    }
    
    private void showMessage(String msg, String color) {
        lblMessage.setText(msg);
        lblMessage.setTextFill(Color.web(color));
    }
}
