package phase3;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class OwnerDashboardController {

    private User currentSessionUser;

    public OwnerDashboardController(User user) {
        this.currentSessionUser = user;
    }

    public void show(Stage stage) {
        BorderPane coreLayout = new BorderPane();
        coreLayout.setStyle("-fx-background-color: #f4f5fa;");

        HBox topNavbar = new HBox();
        topNavbar.setPadding(new Insets(12, 30, 12, 24));
        topNavbar.setAlignment(Pos.CENTER_LEFT);
        topNavbar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ebedf2; -fx-border-width: 0 0 1 0;");

        StackPane logoAnchor = Logo.createRealLogoHeader(100);

        Label lblGreeting = new Label("System Workspace Dashboard");
        lblGreeting.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 15));
        lblGreeting.setStyle("-fx-text-fill: #2c1c3d; -fx-padding: 0 0 0 25;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("Sign Out");
        btnLogout.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        btnLogout.setStyle("-fx-background-color: #ea5455; -fx-text-fill: white; -fx-background-radius: 6px; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> new LoginController().show(stage));

        topNavbar.getChildren().addAll(logoAnchor, lblGreeting, spacer, btnLogout);
        coreLayout.setTop(topNavbar);

        ScrollPane mainScrollPane = new ScrollPane();
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox scrollContainer = new VBox(30);
        scrollContainer.setPadding(new Insets(30));

        // RESTORED: Main dashboard summary envelope color configuration matches login view
        VBox dataAnalysisEnvelope = new VBox(15);
        dataAnalysisEnvelope.setPadding(new Insets(24));
        dataAnalysisEnvelope.setStyle("-fx-background-color: " + Logo.PURPLE_GRADIENT + " -fx-background-radius: 18px;");

        Label lblMetricsHeading = new Label("REALTIME PERFORMANCE & DATA OVERVIEW");
        lblMetricsHeading.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        lblMetricsHeading.setStyle("-fx-text-fill: #ffffff; -fx-letter-spacing: 0.8px;");

        GridPane analyticsGrid = new GridPane();
        analyticsGrid.setHgap(20);
        analyticsGrid.setVgap(20);

        VBox analysisBulletsBox = new VBox(12);
        analysisBulletsBox.getChildren().addAll(
                createSummaryBullet("TOTAL WHOLESALE REVENUE", "425,000 ILS", "+14% Dynamic Growth"),
                createSummaryBullet("LIVE CATALOG STOCK EVALUATION", "150,000 ILS", "Warehouse Node Balanced"),
                createSummaryBullet("MONTHLY WHOLESALE TRADE VOLUME", "95,000 ILS", "Stable Sector Target"),
                createSummaryBullet("CRITICAL STOCK FLAG ALERTS", "3 Items Flagged", "Immediate Restock Required")
        );

        VBox chartContainerBox = new VBox(10);
        chartContainerBox.setPadding(new Insets(15));
        chartContainerBox.setStyle("-fx-background-color: #faf9f6; -fx-background-radius: 14px;");

        Label lblChartTitle = new Label("ANNUAL WHOLESALE REVENUE TREND ANALYSIS");
        lblChartTitle.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 11));
        lblChartTitle.setStyle("-fx-text-fill: #4c4e52;");

        AreaChart<Number, Number> performanceChart = buildBusinessChart();
        chartContainerBox.getChildren().addAll(lblChartTitle, performanceChart);

        ColumnConstraints col1 = new ColumnConstraints(); col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints(); col2.setPercentWidth(65);
        analyticsGrid.getColumnConstraints().addAll(col1, col2);
        analyticsGrid.add(analysisBulletsBox, 0, 0);
        analyticsGrid.add(chartContainerBox, 1, 0);

        dataAnalysisEnvelope.getChildren().addAll(lblMetricsHeading, analyticsGrid);

        VBox interfaceEnvelope = new VBox(15);
        Label lblMenuHeading = new Label("ENTERPRISE ARCHITECTURE INTERFACES");
        lblMenuHeading.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 12));
        lblMenuHeading.setStyle("-fx-text-fill: #8a8d93; -fx-letter-spacing: 0.8px;");

        GridPane routingGrid = new GridPane();
        routingGrid.setHgap(15);
        routingGrid.setVgap(15);

        Button btnProductCatalog = createModuleMenuButton("Product Catalog", "Trace stock updates and ledger metrics");

        // FIXED: Click behavior immediately loads live database collections upon moving views
        btnProductCatalog.setOnAction(e -> {
            ProductDAO dao = new ProductDAO();
            ProductManagementController prodView = new ProductManagementController(currentSessionUser, dao.getAllProducts());
            prodView.show(stage);
        });

        routingGrid.add(btnProductCatalog, 0, 0);
        routingGrid.add(createModuleMenuButton("Staff Clearances", "Manage secure profile access parameters"), 1, 0);
        routingGrid.add(createModuleMenuButton("Billing Sheets", "Generate wholesale invoice distribution logs"), 2, 0);
        routingGrid.add(createModuleMenuButton("Depot Dependencies", "Monitor storage network nodes and locations"), 3, 0);

        for(int i=0; i<4; i++) {
            ColumnConstraints cc = new ColumnConstraints(); cc.setPercentWidth(25);
            routingGrid.getColumnConstraints().add(cc);
        }

        interfaceEnvelope.getChildren().addAll(lblMenuHeading, routingGrid);
        scrollContainer.getChildren().addAll(dataAnalysisEnvelope, interfaceEnvelope);

        mainScrollPane.setContent(scrollContainer);
        coreLayout.setCenter(mainScrollPane);

        Scene scene = new Scene(coreLayout, 1400, 880);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSummaryBullet(String topHeader, String mainValue, String subIndicator) {
        VBox box = new VBox(4);
        box.setPadding(new Insets(14, 18, 14, 18));
        box.setStyle("-fx-background-color: #faf9f6; -fx-background-radius: 12px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.02), 8, 0, 0, 3);");

        Label h = new Label(topHeader); h.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 10)); h.setStyle("-fx-text-fill: #726384;");
        Label v = new Label(mainValue); v.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 18)); v.setStyle("-fx-text-fill: #662D91;");
        Label s = new Label(subIndicator); s.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.NORMAL, 11)); s.setStyle("-fx-text-fill: " + (topHeader.contains("ALERT") ? "#ea5455" : "#1c7a43") + ";");

        box.getChildren().addAll(h, v, s);
        return box;
    }

    private Button createModuleMenuButton(String title, String subtitle) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(95);
        btn.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ebedf2; -fx-border-width: 1px; -fx-border-radius: 12px; -fx-background-radius: 12px; -fx-cursor: hand; -fx-alignment: CENTER_LEFT; -fx-padding: 16;");

        VBox layout = new VBox(4);
        Label mainConstraints = new Label(title); mainConstraints.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.BOLD, 14)); mainConstraints.setStyle("-fx-text-fill: #2c1c3d;");
        Label subConstraints = new Label(subtitle); subConstraints.setFont(Font.font(Logo.GLOBAL_FONT, FontWeight.NORMAL, 12)); subConstraints.setStyle("-fx-text-fill: #8a8d93;");
        layout.getChildren().addAll(mainConstraints, subConstraints);

        btn.setGraphic(layout);
        return btn;
    }

    private AreaChart<Number, Number> buildBusinessChart() {
        NumberAxis xAxis = new NumberAxis(1, 12, 1);
        NumberAxis yAxis = new NumberAxis(10000, 120000, 20000);
        AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setPrefHeight(260);
        chart.setLegendVisible(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 30000));
        series.getData().add(new XYChart.Data<>(4, 65000));
        series.getData().add(new XYChart.Data<>(8, 50000));
        series.getData().add(new XYChart.Data<>(12, 110000));

        chart.getData().add(series);
        return chart;
    }
}