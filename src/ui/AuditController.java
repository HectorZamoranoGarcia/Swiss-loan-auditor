package ui;

import java.util.List;

import backend.AuditRecord;
import backend.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    // Initialization Method (Executed automatically when the view loads)
    @FXML
    public void initialize() {
        // 1. Configure table columns
        // "PropertyValueFactory" automatically searches for Getters in AuditRecord.java
        // Example: "name" maps to the getName() method

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        colCanton.setCellValueFactory(new PropertyValueFactory<>("canton"));
        colScore.setCellValueFactory(new PropertyValueFactory<>("creditScore")); // Maps to getCreditScore()

        // Audit Results (Maps to getBiasedResult and getFairResult)
        colBiased.setCellValueFactory(new PropertyValueFactory<>("biasedResult"));
        colFair.setCellValueFactory(new PropertyValueFactory<>("fairResult"));

        System.out.println("UI Controller initialized. Column mapping done.");
    }

    // Button Action Handler
    @FXML
    void handleRunAudit(ActionEvent event) {
        statusLabel.setText("Status: Fetching data from SQLite...");

        try {
            // 2. Fetch data from the database
            // (Cambiado: ahora la variable se llama 'records' para que sea más claro)
            List<AuditRecord> records = dbManager.getAuditResults();

            // 3. Convert to JavaFX format (ObservableList)
            ObservableList<AuditRecord> dataForTable = FXCollections.observableArrayList(records);

            // 4. Populate the table
            auditTable.setItems(dataForTable);

            statusLabel.setText("Status: Audit Complete. Loaded " + records.size() + " records.");
            System.out.println("Data loaded into table successfully.");

        } catch (Exception e) {
            statusLabel.setText("Error: Could not load data.");
            e.printStackTrace();
        }
    }
}
