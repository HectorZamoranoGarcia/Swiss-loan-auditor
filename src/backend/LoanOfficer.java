package backend;

/** Applies the example approval rules with intentional bias */

public class LoanOfficer {

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


        // Demo bias rules

        // Nationality rule

        if (person.getNationality().equals("Swiss")) {
            score += 15;
        }

        // Canton rule

        if (person.getCanton().equals("TI")) {
            score -= 10;
        }

        // Age rule

        if (person.getAge() < 25) {
            score -= 5;
        }

        // Check the threshold

        return score >= 50;
    }
}
