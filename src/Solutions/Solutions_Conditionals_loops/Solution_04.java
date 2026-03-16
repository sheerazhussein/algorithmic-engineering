package Solutions.Solutions_Conditionals_loops;

import java.util.Scanner;

public class Solution_04 {

    public static double getArea(double a, double b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("Dimensions cannot be negative");
        }

        if (2 * a <= b) {
            throw new IllegalArgumentException("Invalid triangle: sides (" + a + ", " + a + ") are too short for base (" + b + ")");
        }

        double height = Math.sqrt((a * a) - ((b * b) / 4.0));
        return 0.5 * b * height;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            System.out.print("Enter equal side (a): ");
            double sideA = in.nextDouble();

            System.out.print("Enter base (b): ");
            double baseB = in.nextDouble();

            double result = getArea(sideA, baseB);
            System.out.printf("Area of Isosceles Triangle: %.2f\n", result);

        } catch (IllegalArgumentException e) {
            System.out.println("Math Error: " + e.getMessage());
        } catch (java.util.InputMismatchException e) {
            System.out.println("Error: Please enter numeric values only.");
        } finally {
            in.close();
        }
    }
}
