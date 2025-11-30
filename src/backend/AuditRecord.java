package backend;

public class AuditRecord {
  private String id;
  private String name;
  private double income;
  private int creditScore;
  private String canton;
  private String nationality;
  private boolean isBiasedApproved;
  private boolean isFairApproved;

  public AuditRecord(String id, String name, double income, int creditScore, String canton, String nationality,
      boolean isBiasedApproved, boolean isFairApproved) {
    this.id = id;
    this.name = name;
    this.income = income;
    this.creditScore = creditScore;
    this.canton = canton;
    this.nationality = nationality;
    this.isBiasedApproved = isBiasedApproved;
    this.isFairApproved = isFairApproved;
  }

  // Getters
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getIncome() {
    return income;
  }

  public int getCreditScore() {
    return creditScore;
  }

  public String getCanton() {
    return canton;
  }

  public String getNationality() {
    return nationality;
  }

  // Para las columnas de resultado
  public String getBiasedResult() {
    if (isBiasedApproved == true) {
      return "Approved";
    } else {
      return "Rejected";
    }
  }

  public String getFairResult() {
    if (isFairApproved == true) {
      return "Approved";
    } else {
      return "Rejected";
    }
  }

}
