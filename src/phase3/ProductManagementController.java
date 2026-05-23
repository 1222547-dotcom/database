package phase3;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ProductManagementController {

    private User currentUser;
    private ProductDAO dao;
    private TableView<Product> table;
    private ObservableList<Product> productList;
    private List<String[]> cachedSuppliers;

    private TextField txtName, txtModel, txtColor, txtRam, txtStorage, txtPurchase, txtSelling;
    private ComboBox<String> cmbBrand, cmbSupplier;
    private TextField txtSearch;
    private Label lblMessage;
    private int selectedProductId = -1;

    public ProductManagementController(User user, List<Product> products) {
        this.currentUser = user;
        this.dao = new ProductDAO();
    }

    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add(Styles.BG_SUBTLE);
        header.setStyle("-fx-border-color: -color-border-default; -fx-border-width: 0 0 1 0;");

        Button btnBack = new Button("← Back");
        btnBack.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
        btnBack.setOnAction(e -> {
            OwnerDashboardController dashboard = new OwnerDashboardController(currentUser);
            dashboard.show(stage);
        });

        Label title = new Label("Product Management");
        title.getStyleClass().addAll(Styles.TITLE_3, Styles.TEXT_BOLD);
        title.setPadding(new Insets(0, 0, 0, 20));

        header.getChildren().addAll(btnBack, title);

        HBox centerBox = new HBox(20);
        centerBox.setPadding(new Insets(20));

        VBox formBox = createForm();
        formBox.setPrefWidth(380);

        VBox tableBox = createTableSection();
        HBox.setHgrow(tableBox, Priority.ALWAYS);

        centerBox.getChildren().addAll(formBox, tableBox);

        root.setTop(header);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1300, 750);
        stage.setScene(scene);
        stage.setTitle("iCell - Product Management");
        stage.show();

        loadProducts();
        initialLoadSuppliers();
    }

    private VBox createForm() {
        VBox form = new VBox(12);
        form.setPadding(new Insets(20));
        form.getStyleClass().addAll("card", "elevated-1");

        Label formTitle = new Label("Product Details");
        formTitle.getStyleClass().addAll(Styles.TITLE_4, Styles.TEXT_BOLD);

        txtName = new TextField();
        txtName.setPromptText("Product Name");

        cmbBrand = new ComboBox<>();
        cmbBrand.getItems().addAll("Samsung", "Apple", "Xiaomi", "Huawei", "Oppo", "Other");
        cmbBrand.setPromptText("Select Brand");
        cmbBrand.setMaxWidth(Double.MAX_VALUE);

        txtModel = new TextField();
        txtModel.setPromptText("Model (e.g., S24 Ultra)");

        txtColor = new TextField();
        txtColor.setPromptText("Color");

        HBox specsBox = new HBox(10);
        txtRam = new TextField();
        txtRam.setPromptText("RAM (e.g., 8GB)");
        txtStorage = new TextField();
        txtStorage.setPromptText("Storage (e.g., 256GB)");
        HBox.setHgrow(txtRam, Priority.ALWAYS);
        HBox.setHgrow(txtStorage, Priority.ALWAYS);
        specsBox.getChildren().addAll(txtRam, txtStorage);

        HBox priceBox = new HBox(10);
        txtPurchase = new TextField();
        txtPurchase.setPromptText("Purchase Price");
        txtSelling = new TextField();
        txtSelling.setPromptText("Selling Price");
        HBox.setHgrow(txtPurchase, Priority.ALWAYS);
        HBox.setHgrow(txtSelling, Priority.ALWAYS);
        priceBox.getChildren().addAll(txtPurchase, txtSelling);

        cmbSupplier = new ComboBox<>();
        cmbSupplier.setPromptText("Select Supplier");
        cmbSupplier.setMaxWidth(Double.MAX_VALUE);

        lblMessage = new Label("");
        lblMessage.getStyleClass().add(Styles.TEXT_NORMAL);

        Button btnAdd = new Button("➕ Add");
        btnAdd.getStyleClass().addAll(Styles.SUCCESS);

        Button btnUpdate = new Button("Update");
        btnUpdate.getStyleClass().addAll(Styles.WARNING);

        Button btnDelete = new Button("Delete");
        btnDelete.getStyleClass().addAll(Styles.DANGER);

        Button btnClear = new Button("Clear");
        btnClear.getStyleClass().addAll(Styles.BUTTON_OUTLINED);

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
        section.getStyleClass().addAll("card", "elevated-1");

        Label tableTitle = new Label("All Products");
        tableTitle.getStyleClass().addAll(Styles.TITLE_4, Styles.TEXT_BOLD);

        HBox searchBox = new HBox(10);
        txtSearch = new TextField();
        txtSearch.setPromptText("Search by name, brand, or model...");
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        Button btnSearch = new Button("Search");
        btnSearch.getStyleClass().addAll(Styles.ACCENT);

        Button btnRefresh = new Button("Refresh");
        btnRefresh.getStyleClass().addAll(Styles.BUTTON_OUTLINED);

        btnSearch.setOnAction(e -> handleSearch());
        btnRefresh.setOnAction(e -> {
            txtSearch.clear();
            loadProducts();
        });

        searchBox.getChildren().addAll(txtSearch, btnSearch, btnRefresh);

        table = new TableView<>();
        productList = FXCollections.observableArrayList();
        table.setItems(productList);
        table.getStyleClass().add(Styles.STRIPED);

        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colId.setPrefWidth(60);

        TableColumn<Product, String> colName = new TableColumn<>("Product Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colName.setPrefWidth(200);

        TableColumn<Product, String> colBrand = new TableColumn<>("Brand");
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colBrand.setPrefWidth(100);

        TableColumn<Product, String> colModel = new TableColumn<>("Model");
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colModel.setPrefWidth(120);

        TableColumn<Product, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colColor.setPrefWidth(90);

        TableColumn<Product, Double> colPrice = new TableColumn<>("Selling Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        colPrice.setPrefWidth(120);

        TableColumn<Product, String> colSupplier = new TableColumn<>("Supplier");
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colSupplier.setPrefWidth(130);

        table.getColumns().addAll(colId, colName, colBrand, colModel, colColor, colPrice, colSupplier);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fillForm(newVal);
            }
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        section.getChildren().addAll(tableTitle, searchBox, table);

        return section;
    }

    private void loadProducts() {
        new Thread(() -> {
            List<Product> products = dao.getAllProducts();
            javafx.application.Platform.runLater(() -> productList.setAll(products));
        }).start();
    }

    private void initialLoadSuppliers() {
        if (cachedSuppliers != null) return;
        new Thread(() -> {
            List<String[]> loadedSuppliers = dao.getSuppliers();
            javafx.application.Platform.runLater(() -> {
                cachedSuppliers = loadedSuppliers;
                cmbSupplier.getItems().clear();
                for (String[] s : cachedSuppliers) {
                    cmbSupplier.getItems().add(s[1]);
                }
            });
        }).start();
    }

    private int getSupplierIdByName(String name) {
        if (name == null || cachedSuppliers == null) return 0;
        for (String[] s : cachedSuppliers) {
            if (s[1].equals(name)) return Integer.parseInt(s[0]);
        }
        return 0;
    }

    private void handleAdd() {
        if (!validateForm()) return;
        Product p = buildProductFromForm();
        new Thread(() -> {
            boolean success = dao.addProduct(p);
            javafx.application.Platform.runLater(() -> {
                if (success) {
                    showMessage("✓ Product added successfully!", Styles.SUCCESS);
                    clearForm();
                    loadProducts();
                } else {
                    showMessage("✗ Failed to add product", Styles.DANGER);
                }
            });
        }).start();
    }

    private void handleUpdate() {
        if (selectedProductId == -1) {
            showMessage("⚠ Select a product from the table first", Styles.WARNING);
            return;
        }
        if (!validateForm()) return;
        Product p = buildProductFromForm();
        p.setProductId(selectedProductId);
        new Thread(() -> {
            boolean success = dao.updateProduct(p);
            javafx.application.Platform.runLater(() -> {
                if (success) {
                    showMessage("✓ Product updated successfully!", Styles.SUCCESS);
                    clearForm();
                    loadProducts();
                } else {
                    showMessage("✗ Failed to update product", Styles.DANGER);
                }
            });
        }).start();
    }

    private void handleDelete() {
        if (selectedProductId == -1) {
            showMessage("⚠ Select a product from the table first", Styles.WARNING);
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Product");
        confirm.setContentText("Are you sure you want to delete this product?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            new Thread(() -> {
                boolean success = dao.deleteProduct(selectedProductId);
                javafx.application.Platform.runLater(() -> {
                    if (success) {
                        showMessage("✓ Product deleted successfully!", Styles.SUCCESS);
                        clearForm();
                        loadProducts();
                    } else {
                        showMessage("✗ Failed to delete (may be referenced)", Styles.DANGER);
                    }
                });
            }).start();
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadProducts();
            return;
        }
        new Thread(() -> {
            List<Product> results = dao.searchProducts(keyword);
            javafx.application.Platform.runLater(() -> productList.setAll(results));
        }).start();
    }

    private boolean validateForm() {
        if (txtName.getText().isEmpty() || cmbBrand.getValue() == null || txtSelling.getText().isEmpty()) {
            showMessage("⚠ Name, Brand, and Selling Price are required", Styles.WARNING);
            return false;
        }
        try {
            if (!txtPurchase.getText().isEmpty()) Double.parseDouble(txtPurchase.getText());
            Double.parseDouble(txtSelling.getText());
        } catch (NumberFormatException e) {
            showMessage("⚠ Prices must be valid numbers", Styles.WARNING);
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

    private void showMessage(String msg, String styleClass) {
        lblMessage.setText(msg);
        lblMessage.getStyleClass().removeAll(Styles.SUCCESS, Styles.WARNING, Styles.DANGER);
        lblMessage.getStyleClass().add(styleClass);
    }
}