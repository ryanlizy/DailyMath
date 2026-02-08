import java.util.Random;
import java.util.Scanner;

public class DailyMath {

    private static final int TOTAL_QUESTIONS = 11;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int score = 0;

        System.out.println("DailyMath");
        System.out.println("Simple math practice (11 questions).");
        System.out.println("-------------------------------------");

        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            Question q = makeOneQuestion(random);

            System.out.printf("Question %d of %d   |   Score: %d%n", (i + 1), TOTAL_QUESTIONS, score);
            System.out.println(q.prompt + " __");

            Integer answer = readNumberOnly(scanner);
            if (answer == null) {
                // This shouldn't happen, but keep it safe
                System.out.println("Numbers only, please.");
                i--; // redo the question
                continue;
            }

            if (answer == q.correctAnswer) {
                score++;
                System.out.println("Great job! ✅");
            } else {
                System.out.println("Not quite. The answer is: " + q.correctAnswer);
            }

            // Next/Finish prompt
            if (i == TOTAL_QUESTIONS - 1) {
                System.out.println("[Finish] Press Enter to see your result...");
            } else {
                System.out.println("[Next] Press Enter for the next question...");
            }
            scanner.nextLine(); // wait for Enter
            System.out.println();
        }

        System.out.println("-------------------------------------");
        System.out.println("Completed!");
        System.out.println("Final Score: " + score + " / " + TOTAL_QUESTIONS);

        scanner.close();
    }

    // Read a whole line, accept only digits (no letters, no symbols).
    // Returns an Integer, or null if invalid.
    private static Integer readNumberOnly(Scanner scanner) {
        while (true) {
            System.out.print("Your answer: ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("Please enter a number.");
                continue;
            }

            // Digits only. (Grandma-friendly: no negative answers in this version)
            if (!line.matches("\\d+")) {
                System.out.println("Numbers only (0-9). Please try again.");
                continue;
            }

            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                // Very large number edge case
                System.out.println("That number is too large. Please try again.");
            }
        }
    }

    private static Question makeOneQuestion(Random random) {
        int opPick = random.nextInt(3); // 0:+, 1:-, 2:×

        int a, b, ans;
        String symbol;

        if (opPick == 0) {
            // Addition: 0..20
            a = random.nextInt(21);
            b = random.nextInt(21);
            symbol = "+";
            ans = a + b;
        } else if (opPick == 1) {
            // Subtraction: keep non-negative (easier)
            a = random.nextInt(21);
            b = random.nextInt(21);
            if (b > a) {
                int t = a;
                a = b;
                b = t;
            }
            symbol = "-";
            ans = a - b;
        } else {
            // Multiplication: 0..9
            a = random.nextInt(10);
            b = random.nextInt(10);
            symbol = "×";
            ans = a * b;
        }

        String prompt = a + " " + symbol + " " + b + " =";
        return new Question(prompt, ans);
    }

    private static class Question {
        final String prompt;
        final int correctAnswer;

        Question(String prompt, int correctAnswer) {
            this.prompt = prompt;
            this.correctAnswer = correctAnswer;
        }
    }
}
