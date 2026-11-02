package backend;

import java.io.File;
import java.net.URL;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // Start the JavaFX window

    @Override
    public void start(Stage primaryStage) throws Exception {

        new ui.AuditController();

        // Find the FXML view

        File fxmlFile = new File("src/ui/AuditView.fxml");
        URL fxmlUrl = fxmlFile.toURI().toURL();

        // Create the loader

        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        // Set the class loader

        loader.setClassLoader(getClass().getClassLoader());

        // Load the view

        Parent root = loader.load();

        primaryStage.setTitle("Swiss Loan Fairness Auditor");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Swiss Loan Auditor System Initialized.");

        // Create services

        DatabaseManager dbManager = new DatabaseManager();
        LoanOfficer biasedOfficer = new LoanOfficer();
        FairLoanOfficer fairOfficer = new FairLoanOfficer();
        DataGenerator generator = new DataGenerator();

        // Set up the database

        dbManager.initializeDatabase();

        // Generate demo applicants

        List<Applicant> applicants = generator.generateApplicants(1000);

        // Save audit results

        System.out.println("Running 1000 evaluations and saving results...");
        dbManager.saveAuditResults(applicants, biasedOfficer, fairOfficer);

        System.out.println("Audit process completed. Results stored in database/audit_records.db");

        // Launch JavaFX

        launch(args);
    }
}
