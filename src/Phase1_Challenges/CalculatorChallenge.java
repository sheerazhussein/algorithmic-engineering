import java.util.Scanner;

public class CalculatorChallenge {

    // ─────────────────────────────────────────────
    //  ENTRY POINT
    // ─────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printWelcome();

        System.out.print("Enter your name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Friend";

        System.out.println("\nWelcome, " + name + "! Let's do some math.\n");

        // Keep running until the user decides to quit
        boolean keepGoing = true;

        while (keepGoing) {
            printMenu();

            System.out.print("Choose an option (1-7): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": performOperation(scanner, "add");      break;
                case "2": performOperation(scanner, "subtract"); break;
                case "3": performOperation(scanner, "multiply"); break;
                case "4": performOperation(scanner, "divide");   break;
                case "5": performOperation(scanner, "power");    break;
                case "6": performModulo(scanner);                break;
                case "7":
                    System.out.println("\nGoodbye, " + name + "! Keep coding! \n");
                    keepGoing = false;
                    break;
                default:
                    System.out.println("\nInvalid option. Please enter a number between 1 and 7.\n");
            }
        }

        scanner.close();
    }

    // ─────────────────────────────────────────────
    //  MENU & WELCOME
    // ─────────────────────────────────────────────

    static void printWelcome() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      JAVA CALCULATOR  v1.0           ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
    }

    static void printMenu() {
        System.out.println("┌──────────────────────────────────┐");
        System.out.println("│         SELECT OPERATION         │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1.   Addition                 │");
        System.out.println("│  2.   Subtraction              │");
        System.out.println("│  3.    Multiplication           │");
        System.out.println("│  4.   Division                 │");
        System.out.println("│  5. Power (exponent)         │");
        System.out.println("│  6. Modulo (remainder)       │");
        System.out.println("│  7. Quit                     │");
        System.out.println("└──────────────────────────────────┘");
    }

    // ─────────────────────────────────────────────
    //  INPUT HELPER — keeps asking until valid
    // ─────────────────────────────────────────────

    /**
     * Safely reads a double from the user.
     * If they type something that isn't a number, we ask again instead of crashing.
     */
    static double readNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                // NumberFormatException = the string couldn't be turned into a number
                System.out.println("\"" + input + "\" is not a valid number. Please try again.");
            }
        }
    }

    // ─────────────────────────────────────────────
    //  CORE OPERATIONS  (add / subtract / multiply / divide / power)
    // ─────────────────────────────────────────────

    static void performOperation(Scanner scanner, String operation) {
        System.out.println();
        double a = readNumber(scanner, "Enter first number  : ");
        double b = readNumber(scanner, "Enter second number : ");

        double result;
        String symbol;

        switch (operation) {
            case "add":
                result = a + b;
                symbol = "+";
                break;
            case "subtract":
                result = a - b;
                symbol = "-";
                break;
            case "multiply":
                result = a * b;
                symbol = "×";
                break;
            case "divide":
                // Guard: division by zero is undefined — we catch this before dividing
                if (b == 0) {
                    System.out.println("\nError: Cannot divide by zero.\n");
                    return; // exit the method early, go back to the menu
                }
                result = a / b;
                symbol = "÷";
                break;
            case "power":
                // Math.pow(base, exponent) is a built-in Java method
                result = Math.pow(a, b);
                symbol = "^";
                break;
            default:
                System.out.println("Unknown operation.");
                return;
        }

        printResult(a, symbol, b, result);
        printFunFact(result);
    }

    // ─────────────────────────────────────────────
    //  MODULO — separate because it needs int inputs
    // ─────────────────────────────────────────────

    /**
     * Modulo (%) gives you the REMAINDER after division.
     * Example: 10 % 3 = 1  (because 10 ÷ 3 = 3 remainder 1)
     * Useful for: checking if a number is even/odd, cycling through lists, etc.
     */
    static void performModulo(Scanner scanner) {
        System.out.println();
        System.out.println("Modulo gives the remainder after division (e.g. 10 % 3 = 1)");

        double a = readNumber(scanner, "Enter first number  : ");
        double b = readNumber(scanner, "Enter second number : ");

        if (b == 0) {
            System.out.println("\nError: Cannot divide by zero (modulo by zero is undefined).\n");
            return;
        }

        double result = a % b;
        printResult(a, "%", b, result);

        // Extra explanation for beginners
        System.out.println("   ↳ " + (int) a + " ÷ " + (int) b
                + " = " + (int) (a / b) + " remainder " + (int) result);

        // Even/odd check — a classic use of modulo
        if (b == 2) {
            if (result == 0) {
                System.out.println("   ↳ " + (int) a + " is EVEN (divisible by 2 with no remainder).");
            } else {
                System.out.println("   ↳ " + (int) a + " is ODD (has a remainder when divided by 2).");
            }
        }
        System.out.println();
    }

    // ─────────────────────────────────────────────
    //  OUTPUT HELPERS
    // ─────────────────────────────────────────────

    /**
     * Formats and prints the result of any calculation.
     * Using formatNumber() so we show clean integers when possible (10 not 10.0).
     */
    static void printResult(double a, String symbol, double b, double result) {
        System.out.println();
        System.out.println("┌──────────────────────────────────────┐");
        System.out.printf( "│  %s %s %s = %s%n",
                formatNumber(a),
                symbol,
                formatNumber(b),
                formatNumber(result));
        System.out.println("└──────────────────────────────────────┘");
        System.out.println();
    }

    /**
     * Fun facts give beginners context about the result — a small teaching moment.
     */
    static void printFunFact(double result) {
        if (result == 0) {
            System.out.println("Result is zero — the additive identity!");
        } else if (result == 1) {
            System.out.println("Result is 1 — the multiplicative identity!");
        } else if (result < 0) {
            System.out.println("Negative result — you went below zero!");
        } else if (result > 1_000_000) {
            // 1_000_000 is just 1000000 — Java allows underscores in numbers for readability
            System.out.println("That's over a million — big number!");
        } else if (result % 2 == 0) {
            System.out.println("Result is even (divisible by 2).");
        } else {
            System.out.println("Result is odd.");
        }
        System.out.println();
    }

    /**
     * Smart number formatter:
     * - If the double has no decimal part (e.g. 5.0), show it as "5"
     * - If it has decimals (e.g. 5.75), show up to 4 decimal places
     *
     * (long) casts a double to a whole number — e.g. (long) 5.0 → 5
     */
    static String formatNumber(double n) {
        if (n == (long) n) {
            return String.valueOf((long) n); // show as integer: 5 not 5.0
        } else {
            return String.format("%.4f", n); // show up to 4 decimal places
        }
    }
}
