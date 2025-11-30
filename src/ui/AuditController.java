package ui;

import backend.Applicant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AuditController {

    // Interface Elements
    @FXML
    private TableView<?> auditTable; // Table
    @FXML
    private Button btnRunAudit; // Red Button
    @FXML
    private Label statusLabel; // State text

    // Table Columns
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colIncome;
    @FXML
    private TableColumn<?, ?> colCanton;
    @FXML
    private TableColumn<?, ?> colScore;
    @FXML
    private TableColumn<?, ?> colBiased;
    @FXML
    private TableColumn<?, ?> colFair;

    // Button Action Handler
    @FXML
    void handleRunAudit(ActionEvent event) {
        System.out.println("¡Botón presionado! Iniciando simulación...");
        statusLabel.setText("Status: Running Audit... Check Console.");

    }

    // Initialization Method
    @FXML
    public void initialize() {
        System.out.println("Interfaz Gráfica cargada y lista.");
    }
}
