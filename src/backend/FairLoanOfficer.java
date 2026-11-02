package backend;

// Reference rules without the demo bias adjustments

public class FairLoanOfficer {

    // Approve at 50 points

    public boolean evaluateLoan(Applicant person) {
        int score = 0;

        // Income points

        if (person.getIncome() > 100000)
            score += 30;
        else if (person.getIncome() > 60000)
            score += 15;

        // Credit points

        if (person.getCreditScore() > 700)
            score += 40;
        else if (person.getCreditScore() > 600)
            score += 20;

        // Check the threshold

        return score >= 50;
    }
}
