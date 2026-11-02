# Swiss Loan Fairness Auditor

A JavaFX desktop demo that compares two rule-based loan approval models using synthetic applicant records. One model includes intentionally biased rules; the reference model uses income and credit score only. The app stores audit results in SQLite and displays a summary in the dashboard.

![Java](https://img.shields.io/badge/Java-24-007396?style=flat-square&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/UI-JavaFX-FF0000?style=flat-square)
![SQLite](https://img.shields.io/badge/Database-SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white)

## What it shows

- Applicants are generated with random income, credit scores, ages, cantons, and nationality categories
- `LoanOfficer` applies the example rules, including the intentional penalties
- `FairLoanOfficer` applies the income and credit-score rules without those attributes
- The audit results are saved to `database/audit_records.db` and shown in the JavaFX interface

The applicant data is synthetic and is not intended to represent the Swiss population. The models are simple examples for comparing rule sets; their results are not evidence about real lenders or real-world discrimination.

## Run in VS Code

The project uses a lightweight Java setup without Maven or Gradle.

1. Install JDK 24 and JavaFX SDK 24
2. Set `JAVAFX_HOME` to the JavaFX SDK directory
3. Open the repository in VS Code with the Java Extension Pack
4. Select **Run Database Audit** in the Run and Debug panel

The launch configuration uses `JAVAFX_HOME` for the JavaFX module path. The project also includes its SQLite and JavaFX JAR files in `lib`.

## Project structure

```text
src/backend/   Applicant data, rule sets, and SQLite persistence
src/ui/        JavaFX controller and FXML view
bin/           Compiled classes and view files
lib/           JavaFX and SQLite libraries
database/      SQLite audit records
.vscode/       VS Code launch configuration
```

## Development note

GitHub Copilot assisted with boilerplate and SQL syntax. The audit rules and project structure were developed by the author.

## Author

Héctor Zamorano García

## License

MIT. See [LICENSE.md](LICENSE.md).
