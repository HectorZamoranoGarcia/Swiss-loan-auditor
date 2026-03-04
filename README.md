# Swiss Loan Fairness Auditor (JavaFX)

**Algorithmic auditing tool for the Swiss Fintech sector. Built with Java + JavaFX, featuring SQLite persistence and real-time statistical analysis.**

![Java 24](https://img.shields.io/badge/Java-v24-007396?style=for-the-badge&logo=java&logoColor=white)
![JavaFX 24](https://img.shields.io/badge/JavaFX-v24-FF0000?style=for-the-badge&logo=oracle&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-Latest-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![MVC Pattern](https://img.shields.io/badge/Pattern-MVC-green?style=for-the-badge)
![JDBC Batch](https://img.shields.io/badge/JDBC-Batch--Processing-orange?style=for-the-badge&logo=oracle&logoColor=white)

## Project Overview

This software simulates a compliance and auditing environment tailored for a Swiss financial institution. With AI and automated decision-making systems becoming the standard in banking, the risk of hidden algorithmic bias is a growing concern.

The goal here isn't to manage loans but to **audit the engines approving them**. I built a "Black Box" algorithm (`LoanOfficer`) configured with intentional, hidden biases—such as penalizing specific Cantons or Nationalities—and contrasted its performance against a "Fair" control model. By processing thousands of synthetic applications, the tool visualizes the disparate impact, providing clear evidence of discrimination.

This project showcases a full-stack engineering approach, covering backend logic, synthetic data generation, database optimization, and frontend visualization.

## Key Objectives

* **Detecting Algorithmic Bias:** Identifying disparate impact ratios in loan approvals based on protected attributes like Nationality, Region, and Age through parallel algorithm execution.
* **High-Volume Simulation:** Generating realistic Swiss demographic profiles at scale to ensure statistical significance.
* **Performance Optimization:** Implementing JDBC batch processing to handle high-throughput data insertion into SQLite efficiently.
* **Architectural Integrity:** Applying strict MVC patterns and Immutable Data Models to guarantee thread safety and data consistency during the audit.

## Data & Demographics

The system moves away from static datasets. Instead, it features a custom **Synthetic Data Generator** that creates unique applicants on the fly. I used weighted probabilities to mirror the actual structure of the Swiss population:

* **Financial Data:**
  * *Income:* Randomized gaussian distribution centered around typical Swiss salaries (80k–120k CHF).
  * *Credit Score:* Modeled on standard scoring systems (300–850 range).

* **Demographic Data:**
  * *Cantons:* Zurich (ZH), Geneva (GE), Bern (BE), Ticino (TI).
  * *Nationality:* Weighted split between Swiss Nationals, EU Permit Holders (B/C), and Third-Country Nationals.

## Methodology

### A. The "Black Box" Logic

The core of the simulation is the `LoanOfficer` class. It implements a decision tree mimicking real-world AI bias issues found in legacy banking systems:

* **Fair Criteria:** Approvals based on Income thresholds (>80k CHF) and robust Credit Scores (>650).
* **Hidden Bias:** Hardcoded penalties for applicants residing in Ticino (TI) or identified as Non-EU, simulating historical prejudices often embedded in training data.

### B. The Control Group

Running parallel to the biased engine, a `FairLoanOfficer` class evaluates the exact same applicant instances using **blind criteria** (financial metrics only). The difference between the two outcomes reveals the "Fairness Gap," which is then visualized in the dashboard.

### C. Data Persistence Strategy

To handle the I/O load of saving thousands of audit records:

* **Database:** I chose SQLite for its reliability and portability.
* **Optimization:** The `DatabaseManager` utilizes `conn.setAutoCommit(false)` to enable **Batch Processing**. Instead of committing 1,000 individual transactions, the system batches them into a single atomic operation, slashing execution time by ~95%.

### D. Visual Analytics (UI)

The frontend is built with **JavaFX** following the MVC pattern:

* **View:** FXML files designed in Scene Builder for a clean layout.
* **Controller:** Handles logic delegation and maps database records to the UI using a `TableView` and interactive `PieCharts` for real-time comparison.

## Development Process

Integrating SQL, Java, and a UI into a cohesive system required a structured workflow. Here’s how I approached it:

* **AI Assistance:** I used GitHub Copilot to speed up boilerplate generation (constructors, getters) and SQL syntax autocompletion. However, the core architectural decisions and the bias logic were implemented manually to ensure accuracy.
* **Technical Research:**
  * *JavaFX Lifecycle:* I dove into forum documentation to troubleshoot specific issues regarding `FXML Controller` injection and the execution order of the `initialize()` method versus the constructor.
  * *Integration:* I referenced technical tutorials to set up the correct JDBC connection string protocols for SQLite and optimize the VS Code + Scene Builder workflow.

## Repository Structure

```text
SwissLoanAuditor/
├── src/
│   ├── backend/           # Core Logic & Domain Models
│   │   ├── Applicant.java       # Immutable DTO
│   │   ├── LoanOfficer.java     # The Biased Algorithm
│   │   ├── FairLoanOfficer.java # The Control Algorithm
│   │   ├── DatabaseManager.java # JDBC & Batch Logic
│   │   └── DataGenerator.java   # Synthetic Factory
│   └── ui/                # JavaFX Controllers
│       ├── AuditController.java # Event Handling & Charts
│       └── ...
├── resources/             # FXML Views & Assets
│   ├── AuditView.fxml     # Dashboard UI Layout
│   └── ...
├── lib/                   # Dependencies (SQLite, SLF4J)
├── database/              # SQLite DB File (Auto-generated)
└── README.md              # Documentation
```

## Technologies

* **Language:** Java (JDK 24) - Leveraging modern features and strong typing.
* **GUI:** JavaFX 24 + Scene Builder - For a native desktop experience.
* **Database:** SQLite (JDBC) - Embedded SQL engine.
* **IDE:** Visual Studio Code - With a customized `launch.json` for dependency management.
* **Concepts:** OOP, MVC Pattern, Algorithmic Fairness, SQL Optimization.

## How to Run

I built this using a lightweight **VS Code** structure (no Maven/Gradle) to demonstrate core Java concepts without the overhead of heavy frameworks.

> Note on Distribution: I intentionally omitted a standalone `.jar` executable. Modern JavaFX architecture requires platform-specific native libraries, complicating cross-platform distribution without tools like JLink. To ensure stability and transparency, the project is delivered as source code.

1. Clone the repository.
2. Open in VS Code (Make sure you have the *Java Extension Pack* installed).
3. Verify Configuration: Check that the `lib/` folder contains the SQLite driver and is referenced in "Referenced Libraries".
4. Launch: Go to the "Run and Debug" panel and select "Run Database Audit". The system will first run the backend simulation and then launch the dashboard.

## Author

**Héctor Zamorano García**

Project developed individually as a practical application of concepts from the *Elements of AI* certification (University of Helsinki). License: MIT License.
