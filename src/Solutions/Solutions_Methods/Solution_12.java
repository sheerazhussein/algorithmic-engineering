package Solutions.Solutions_Methods;

import java.util.Scanner;

public class Solution_12 {

    public static int findMax(int a, int b, int c) {
        int maxOfAB = (a >= b) ? a : b;
        return (maxOfAB >= c) ? maxOfAB : c;
    }

    public static boolean isPythagoreanTriplet(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) return false;

        int max = findMax(a, b, c);

        int sumOfSquares;
        if (max == c) {
            sumOfSquares = a * a + b * b;
        } else if (max == b) {
            sumOfSquares = a * a + c * c;
        } else {
            sumOfSquares = b * b + c * c;
        }

        return sumOfSquares == max * max;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("--- Pythagorean Triplet Checker ---");
        System.out.print("Enter three numbers (a b c): ");

        if (input.hasNextInt()) {
            int a = input.nextInt();
            int b = input.nextInt();
            int c = input.nextInt();

            if (isPythagoreanTriplet(a, b, c)) {
                System.out.println("Yes! These are a Pythagorean Triplet.");
            } else {
                System.out.println("No, these are not a triplet.");
            }
        } else {
            System.out.println("Please enter valid integers.");
        }

        input.close();
    }
}
