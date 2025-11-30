package ui;

import java.util.List;

import backend.AuditRecord;
import backend.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AuditController {

    // Database Manager Instance
    private DatabaseManager dbManager = new DatabaseManager();

    // Interface Elements
    @FXML
    private TableView<AuditRecord> auditTable; // Table
    @FXML
    private Button btnRunAudit; // Red Button
    @FXML
    private Label statusLabel; // State text

    // Charts Elements
    @FXML
    private PieChart biasedPieChart;
    @FXML
    private PieChart fairPieChart;

    // Table Columns
    @FXML
    private TableColumn<AuditRecord, String> colId;
    @FXML
    private TableColumn<AuditRecord, String> colName;
    @FXML
    private TableColumn<AuditRecord, Double> colIncome;
    @FXML
    private TableColumn<AuditRecord, String> colCanton;
    @FXML
    private TableColumn<AuditRecord, Integer> colScore;
    @FXML
    private TableColumn<AuditRecord, String> colBiased;
    @FXML
    private TableColumn<AuditRecord, String> colFair;

    // Initialization Method
    @FXML
    public void initialize() {
        // Map data properties to columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        colCanton.setCellValueFactory(new PropertyValueFactory<>("canton"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("creditScore"));
        colBiased.setCellValueFactory(new PropertyValueFactory<>("biasedResult"));
        colFair.setCellValueFactory(new PropertyValueFactory<>("fairResult"));

        // Custom Cell Factory: Format Income as Currency (CHF)
        // Lógica "inline" que funcionaba correctamente
        colIncome.setCellFactory(tc -> new TableCell<AuditRecord, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Format: Thousands separator + 2 decimals
                    setText(String.format("%,.2f CHF", item));
                }
            }
        });

        System.out.println("UI Controller initialized. Column mapping & Formatting done.");
    }

    // Button Action Handler
    @FXML
    void handleRunAudit(ActionEvent event) {
        statusLabel.setText("Status: Fetching data from SQLite...");

        try {
            // Fetch data from the database
            List<AuditRecord> records = dbManager.getAuditResults();

            ObservableList<AuditRecord> dataForTable = FXCollections.observableArrayList(records);

            // Populate the table
            auditTable.setItems(dataForTable);

            // Statistics
            updateCharts(records);

            statusLabel.setText("Status: Audit Complete. Loaded " + records.size() + " records.");
            System.out.println("Data loaded into table successfully.");

        } catch (Exception e) {
            statusLabel.setText("Error: Could not load data.");
            e.printStackTrace();
        }
    }

    // Helper method to calculate statistics
    private void updateCharts(List<AuditRecord> records) {
        int biasedApproved = 0;
        int fairApproved = 0;

        // Count approvals
        for (AuditRecord r : records) {
            if (r.getBiasedResult().contains("Approved")) {
                biasedApproved++;
            }
            if (r.getFairResult().contains("Approved")) {
                fairApproved++;
            }
        }

        int total = records.size();
        int biasedRejected = total - biasedApproved;
        int fairRejected = total - fairApproved;

        // Populate Biased Chart data
        ObservableList<PieChart.Data> biasedData = FXCollections.observableArrayList(
                new PieChart.Data("Approved (" + biasedApproved + ")", biasedApproved),
                new PieChart.Data("Rejected (" + biasedRejected + ")", biasedRejected));
        biasedPieChart.setData(biasedData);

        // Populate Fair Chart data
        ObservableList<PieChart.Data> fairData = FXCollections.observableArrayList(
                new PieChart.Data("Approved (" + fairApproved + ")", fairApproved),
                new PieChart.Data("Rejected (" + fairRejected + ")", fairRejected));
        fairPieChart.setData(fairData);

        System.out.println("Charts updated: Biased Approved=" + biasedApproved + " vs Fair Approved=" + fairApproved);
    }
}
