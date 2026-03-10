package JF.src.solutions;

import java.util.Scanner;

public class Solution_02 {

    // 1. Even or Odd Program
    public static String checkEvenOdd(int num) {
        return (num % 2 == 0) ? "Even" : "Odd";
    }

    // 2. Personalized Greeting
    public static String personalizedGreet(String name) {
        return "Hello " + name + " wishing you are having a Great day!";
    }

    // 3. Simple Interest
    public static float simpleInterest(float p, float r, float t) {
        return (p * t * r) / 100;
    }

    // 4. Basic Calculator
    public static void calculate(double n1, double n2, char op) {
        System.out.print("Result: ");
        if (op == '+') {
            System.out.println(n1 + n2);
        } else if (op == '-') {
            System.out.println(n1 - n2);
        } else if (op == '*') {
            System.out.println(n1 * n2);
        } else if (op == '/') {
            if (n2 != 0) {
                System.out.println(n1 / n2);
            } else {
                System.out.println("Error: Cannot divide by zero");
            }
        } else {
            System.out.println("Invalid operator!");
        }
    }

    // 5. Largest of Two Numbers
    public static String largest(float num1, float num2) {
        /*
        Float.isNaN() checks the special NaN bit pattern directly

        Catches NaN, 0/0, Infinity/Infinity before any comparison

        Returns early to avoid downstream failures
         */

        if (Float.isNaN(num1) || Float.isNaN(num2)) {
            return "At least one input is NaN";
        }
        int cmp = Float.compare(num1, num2);
        if (cmp > 0) {
            return "Largest: " + num1;
        } else if (cmp < 0) {
            return "Largest: " + num2;
        } else {
            return "Equal: " + num1;
        }
    }
    // 5. Currency Converter
    public static String currencyConverter (double inrAmount, double exchangeRate) {
        if(exchangeRate <= 0) return "error: Amount can't be negative";

        if(inrAmount < 0) return "Error: Amount cannot be negative";

        double usdAmount = inrAmount / exchangeRate;
        return String.format("%.2f INR = %.2f USD", inrAmount, usdAmount);
    }

    // 6. Multi-Currency Converter (USD, EUR, INR, PKR)
    public static String multiCurrencyConverter(double amount, String fromCurrency, String toCurrency) {
        // Validate inputs
        if (amount < 0) return "Error: Amount cannot be negative";
        if (fromCurrency == null || toCurrency == null) return "Error: Invalid currencies";

        // Normalize to uppercase
        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        // All rates relative to USD (base currency)
        double usdAmount;

        // Convert FROM currency to USD first
        switch (fromCurrency) {
            case "USD": usdAmount = amount; break;
            case "INR": usdAmount = amount * 0.012; break;  // 1 INR = 0.012 USD
            case "EUR": usdAmount = amount * 1.08; break;   // 1 EUR = 1.08 USD
            case "PKR": usdAmount = amount * 0.0036; break; // 1 PKR = 0.0036 USD
            default: return "Error: Unsupported source currency: " + fromCurrency;
        }

        // Convert FROM USD to target currency
        double convertedAmount;
        switch (toCurrency) {
            case "USD": convertedAmount = usdAmount; break;
            case "INR": convertedAmount = usdAmount * 83; break;     // 1 USD = 83 INR
            case "EUR": convertedAmount = usdAmount * 0.92; break;   // 1 USD = 0.92 EUR
            case "PKR": convertedAmount = usdAmount * 278; break;    // 1 USD = 278 PKR
            default: return "Error: Unsupported target currency: " + toCurrency;
        }


        return String.format("%.2f %s = %.2f %s", amount, fromCurrency, convertedAmount, toCurrency);
    }

//    7. Fibonacci Series (First n terms)
    public static String facbonacciSeries(int num){

        if (num <= 0) return "Error: Number of terms must be positive";
        if (num == 1) return "0";

        StringBuilder series = new StringBuilder();
        int a = 0, b = 1;

//        first term
        series.append(a).append(" ");

//        generate n-1 more terms
        for (int i = 1; i < num; i++) {

            series.append(b).append(" ");
            int next = a + b;
            a = b;
            b = next;
        }

        return "FabonacciSeries (" + "terms): " + series.toString().trim();
    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String choice = "";  // Initialize to avoid compiler error

        // 1. solution (uncomment to test)
        // System.out.print("Enter a number: ");
        // int num = input.nextInt();
        // System.out.println("Answer: " + checkEvenOdd(num));

        // 2. solution (uncomment to test)
        // input.nextLine();  // Consume newline if needed
        // System.out.print("Enter a Name: ");
        // String name = input.nextLine();
        // System.out.println("Answer: " + personalizedGreet(name));

        // 3. solution (uncomment to test)
        // System.out.print("Enter principal (p): ");
        // float p = input.nextFloat();
        // System.out.print("Enter rate (r): ");
        // float r = input.nextFloat();
        // System.out.print("Enter time (t): ");
        // float t = input.nextFloat();
        // System.out.println("Simple Interest: " + simpleInterest(p, r, t));

        // 4. solution (uncomment to test)
        // do {
        //     System.out.print("Enter first number: ");
        //     double n1 = input.nextDouble();
        //     System.out.print("Enter second number: ");
        //     double n2 = input.nextDouble();
        //     System.out.print("Enter operator (+, -, *, /): ");
        //     char op = input.next().charAt(0);
        //     calculate(n1, n2, op);
        //     System.out.print("Perform another calculation? (yes/no): ");
        //     choice = input.next().toLowerCase();
        // } while (choice.equals("yes") || choice.equals("y"));
        // System.out.println("Goodbye!");

        // 5. solution (active; uncomment others to test)
//        System.out.print("Enter num1 and num2 (space-separated): ");
//        float num1 = input.nextFloat();
//        float num2 = input.nextFloat();
//        System.out.println("Answer: " + largest(num1, num2));

        // 6. Currency Converter Test
//        System.out.print("Enter INR amount: ");
//        double inr = input.nextDouble();
//        System.out.print("Enter exchange rate (INR per USD): ");
//        double rate = input.nextDouble();
//        System.out.println("Answer: " + currencyConverter(inr, rate));

        // 7. Multi-Currency Test
//        System.out.print("Enter amount: ");
//        double amount = input.nextDouble();
//        System.out.print("Enter FROM currency (USD/EUR/INR/PKR): ");
//        String from = input.next();
//        System.out.print("Enter TO currency (USD/EUR/INR/PKR): ");
//        String to = input.next();
//        System.out.println("Answer: " + multiCurrencyConverter(amount, from, to));

        // // 7. Fibonacci Test (uncomment to use)
        System.out.println("Enter a number of terms: ");
        int terms = input.nextInt();
        System.out.println("Answer: " + facbonacciSeries(terms));






        input.close();
    }
}
