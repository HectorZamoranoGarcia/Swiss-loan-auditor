package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final String[] cantons = { "ZH", "GE", "BE", "TI" };
    private static final String[] nationalities = {
            "Swiss", "Swiss", "Swiss", "Swiss", "Swiss",
            "EU", "EU", "EU",
            "Other", "Other"
    };

    private final Random random = new Random();

    public List<Applicant> generateApplicants(int count) {
        List<Applicant> list = new ArrayList<>(); // Generated applicants

        for (int i = 0; i < count; i++) {
            String id = "ID:" + (2024000 + i);
            String name = "Applicant " + (i + 1);

            // Income from 40k to 140k CHF

            double income = 40000 + (random.nextDouble() * 100000);
            int creditScore = 300 + random.nextInt(551);

            String canton = cantons[random.nextInt(cantons.length)];
            String nationality = nationalities[random.nextInt(nationalities.length)];
            int age = 18 + random.nextInt(65);

            list.add(new Applicant(id, name, income, creditScore, canton, nationality, age));
        }
        return list;
    }
}
