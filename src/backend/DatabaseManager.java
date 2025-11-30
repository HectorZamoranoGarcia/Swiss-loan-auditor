package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    // URL points to the database file inside the 'database/' folder
    private static final String URL = "jdbc:sqlite:database/audit_records.db";

    /**
     * Initializes the database and creates the table if it does not exist.
     * The table stores both the applicant's profile and the audit result.
     */
    public void initializeDatabase() {
        // SQL to create the 'applicants' table
        String sql = "CREATE TABLE IF NOT EXISTS applicants (" +
                "id TEXT PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "income REAL, " +
                "credit_score INTEGER, " +
                "canton TEXT, " +
                "nationality TEXT, " +
                "age INTEGER, " +
                "approved_biased BOOLEAN, " + // Column for the Biased Officer
                "approved_fair BOOLEAN)"; // Column for the Fair Officer

        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(URL);
                    Statement stmt = conn.createStatement()) {

                stmt.execute(sql);
                System.out.println("Database connected and table schema checked.");
            }

        } catch (Exception e) {
            System.err.println("Database Initialization Error: " + e.getMessage());
        }
    }

    // Saves a list of applicants and the result of their evaluation.
    public void saveAuditResults(List<Applicant> applicants, LoanOfficer officer, FairLoanOfficer fairOfficer) {

        // Inserting audit results
        String sql = "INSERT INTO applicants(id, name, income, credit_score, canton, nationality, age, approved_biased, approved_fair) VALUES(?,?,?,?,?,?,?,?,?)";

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL);
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Disable auto commit to speed up the process
                conn.setAutoCommit(false);

                // Traditional FOR loop structure
                for (int i = 0; i < applicants.size(); i++) {
                    Applicant app = applicants.get(i); // Create 'app' variable by manual retrieval

                    // Evaluate with both officers
                    boolean isApprovedBiased = officer.evaluateLoan(app);
                    boolean isApprovedFair = fairOfficer.evaluateLoan(app);

                    // Setting parameters for the PreparedStatement
                    pstmt.setString(1, "ID:" + System.currentTimeMillis() + "-" + app.getName().hashCode());
                    pstmt.setString(2, app.getName());
                    pstmt.setDouble(3, app.getIncome());
                    pstmt.setInt(4, app.getCreditScore());
                    pstmt.setString(5, app.getCanton());
                    pstmt.setString(6, app.getNationality());
                    pstmt.setInt(7, app.getAge());
                    pstmt.setBoolean(8, isApprovedBiased); // Biased result
                    pstmt.setBoolean(9, isApprovedFair); // Fair result

                    pstmt.addBatch(); // Add to batch
                }

                pstmt.executeBatch(); // Send the entire batch
                conn.commit(); // Commit changes
                System.out.println("Saved " + applicants.size() + " audit records to SQLite.");
            }

        } catch (Exception e) {
            System.err.println("Save Error: " + e.getMessage());
        }
    }

    // Reads all audit records from the SQLite database.
    public List<AuditRecord> getAuditResults() {
        List<AuditRecord> results = new ArrayList<>();
        String sql = "SELECT * FROM applicants";

        try {
            // Force driver load
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // Loop through the database rows
                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    double income = rs.getDouble("income");
                    int score = rs.getInt("credit_score");
                    String canton = rs.getString("canton");
                    String nation = rs.getString("nationality");

                    // Retrieve boolean results
                    boolean biased = rs.getBoolean("approved_biased");
                    boolean fair = rs.getBoolean("approved_fair");

                    // Create the object and add to list
                    results.add(new AuditRecord(id, name, income, score, canton, nation, biased, fair));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading DB: " + e.getMessage());
        }

        return results;
    }
}
