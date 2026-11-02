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

    // Database file path

    private static final String URL = "jdbc:sqlite:database/audit_records.db";

    /** Create the audit table if needed */
    public void initializeDatabase() {
        // Create the applicants table

        String sql = "CREATE TABLE IF NOT EXISTS applicants (" +
                "id TEXT PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "income REAL, " +
                "credit_score INTEGER, " +
                "canton TEXT, " +
                "nationality TEXT, " +
                "age INTEGER, " +
                "approved_biased BOOLEAN, " + // Biased approval
                "approved_fair BOOLEAN)"; // Reference approval

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

    // Save applicant results

    public void saveAuditResults(List<Applicant> applicants, LoanOfficer officer, FairLoanOfficer fairOfficer) {

        // Insert audit results

        String sql = "INSERT INTO applicants(id, name, income, credit_score, canton, nationality, age, approved_biased, approved_fair) VALUES(?,?,?,?,?,?,?,?,?)";

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL);
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Disable auto-commit

                conn.setAutoCommit(false);

                // Evaluate each applicant

                for (int i = 0; i < applicants.size(); i++) {
                    Applicant app = applicants.get(i); // Current applicant

                    // Run both rule sets

                    boolean isApprovedBiased = officer.evaluateLoan(app);
                    boolean isApprovedFair = fairOfficer.evaluateLoan(app);

                    // Set the statement values

                    pstmt.setString(1, "ID:" + System.currentTimeMillis() + "-" + app.getName().hashCode());
                    pstmt.setString(2, app.getName());
                    pstmt.setDouble(3, app.getIncome());
                    pstmt.setInt(4, app.getCreditScore());
                    pstmt.setString(5, app.getCanton());
                    pstmt.setString(6, app.getNationality());
                    pstmt.setInt(7, app.getAge());
                    pstmt.setBoolean(8, isApprovedBiased); // Biased result
                    pstmt.setBoolean(9, isApprovedFair); // Reference result

                    pstmt.addBatch(); // Add row to batch
                }

                pstmt.executeBatch(); // Insert all rows
                conn.commit(); // Save changes
                System.out.println("Saved " + applicants.size() + " audit records to SQLite.");
            }

        } catch (Exception e) {
            System.err.println("Save Error: " + e.getMessage());
        }
    }

    // Load audit records

    public List<AuditRecord> getAuditResults() {
        List<AuditRecord> results = new ArrayList<>();
        String sql = "SELECT * FROM applicants";

        try {
            // Load the SQLite driver

            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(URL);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql)) {

                // Read database rows

                while (rs.next()) {
                    String id = rs.getString("id");
                    String name = rs.getString("name");
                    double income = rs.getDouble("income");
                    int score = rs.getInt("credit_score");
                    String canton = rs.getString("canton");
                    String nation = rs.getString("nationality");

                    // Read model results

                    boolean biased = rs.getBoolean("approved_biased");
                    boolean fair = rs.getBoolean("approved_fair");

                    // Add an audit record

                    results.add(new AuditRecord(id, name, income, score, canton, nation, biased, fair));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading DB: " + e.getMessage());
        }

        return results;
    }
}
